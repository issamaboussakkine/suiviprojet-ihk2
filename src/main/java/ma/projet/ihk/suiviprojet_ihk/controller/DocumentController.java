package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.DocumentDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import ma.projet.ihk.suiviprojet_ihk.exception.DocumentNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.DocumentMapper;
import ma.projet.ihk.suiviprojet_ihk.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentMapper documentMapper;

    // GET tous les documents
    @GetMapping
    public List<DocumentDTO> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return documents.stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document non trouvé avec l'ID: " + id));
        return ResponseEntity.ok(documentMapper.toDto(document));
    }

    // POST créer un document
    @PostMapping
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody DocumentDTO dto) {
        Document document = documentMapper.toEntity(dto);
        Document saved = documentService.saveDocument(document);
        return new ResponseEntity<>(documentMapper.toDto(saved), HttpStatus.CREATED);
    }

    // PUT modifier un document
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @RequestBody DocumentDTO dto) {
        Document existingDocument = documentService.getDocumentById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document non trouvé avec l'ID: " + id));

        documentMapper.updateEntityFromDto(dto, existingDocument);
        Document updated = documentService.saveDocument(existingDocument);
        return ResponseEntity.ok(documentMapper.toDto(updated));
    }

    // DELETE supprimer un document
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    // GET documents par projet
    @GetMapping("/projet/{projetId}")
    public List<DocumentDTO> getDocumentsByProjet(@PathVariable Long projetId) {
        List<Document> documents = documentService.getDocumentsByProjet(projetId);
        return documents.stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET count documents par projet
    @GetMapping("/projet/{projetId}/count")
    public ResponseEntity<Long> countDocumentsByProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(documentService.countDocumentsByProjet(projetId));
    }

    // GET vérifier si projet a des documents
    @GetMapping("/projet/{projetId}/exists")
    public ResponseEntity<Boolean> hasDocuments(@PathVariable Long projetId) {
        return ResponseEntity.ok(documentService.hasDocuments(projetId));
    }
}