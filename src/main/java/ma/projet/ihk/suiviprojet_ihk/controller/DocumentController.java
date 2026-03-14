package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import ma.projet.ihk.suiviprojet_ihk.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // Récupérer tous les documents
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    // Récupérer un document par ID
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouveau document
    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document saved = documentService.saveDocument(document);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Modifier un document
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        Document updated = documentService.updateDocument(id, document);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Supprimer un document
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer les documents d'un projet
    @GetMapping("/projet/{projetId}")
    public List<Document> getDocumentsByProjet(@PathVariable Long projetId) {
        return documentService.getDocumentsByProjet(projetId);
    }

    // Compter les documents d'un projet
    @GetMapping("/projet/{projetId}/count")
    public ResponseEntity<Long> countDocumentsByProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(documentService.countDocumentsByProjet(projetId));
    }

    // Vérifier si un projet a des documents
    @GetMapping("/projet/{projetId}/exists")
    public ResponseEntity<Boolean> hasDocuments(@PathVariable Long projetId) {
        return ResponseEntity.ok(documentService.hasDocuments(projetId));
    }
}