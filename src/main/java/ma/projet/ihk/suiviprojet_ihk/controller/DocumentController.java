package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.DocumentDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.exception.DocumentNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.DocumentMapper;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProjetRepository;
import ma.projet.ihk.suiviprojet_ihk.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    private ProjetRepository projetRepository;

    private final String UPLOAD_DIR = "uploads/documents/";

    @GetMapping
    public List<DocumentDTO> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return documents.stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document non trouvé"));
        return ResponseEntity.ok(documentMapper.toDto(document));
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody DocumentDTO dto) {
        Document document = documentMapper.toEntity(dto);
        Document saved = documentService.saveDocument(document);
        return new ResponseEntity<>(documentMapper.toDto(saved), HttpStatus.CREATED);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(
            @RequestParam("projetId") Long projetId,
            @RequestParam("code") String code,
            @RequestParam("libelle") String libelle,
            @RequestParam("description") String description,
            @RequestParam(value = "fichier", required = false) MultipartFile fichier) {

        try {
            Projet projet = projetRepository.findById(Math.toIntExact(projetId))
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

            Document document = new Document();
            document.setCode(code);
            document.setLibelle(libelle);
            document.setDescription(description);
            document.setProjet(projet);

            if (fichier != null && !fichier.isEmpty()) {
                String chemin = saveFile(fichier);
                document.setChemin(chemin);
            }

            Document saved = documentService.saveDocument(document);
            return new ResponseEntity<>(documentMapper.toDto(saved), HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @RequestBody DocumentDTO dto) {
        Document existingDocument = documentService.getDocumentById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document non trouvé"));

        documentMapper.updateEntityFromDto(dto, existingDocument);
        Document updated = documentService.saveDocument(existingDocument);
        return ResponseEntity.ok(documentMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id).orElse(null);
        if (document != null && document.getChemin() != null) {
            deleteFile(document.getChemin());
        }
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projet/{projetId}")
    public List<DocumentDTO> getDocumentsByProjet(@PathVariable Long projetId) {
        List<Document> documents = documentService.getDocumentsByProjet(projetId);
        return documents.stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/projet/{projetId}/count")
    public ResponseEntity<Long> countDocumentsByProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(documentService.countDocumentsByProjet(projetId));
    }

    @GetMapping("/projet/{projetId}/exists")
    public ResponseEntity<Boolean> hasDocuments(@PathVariable Long projetId) {
        return ResponseEntity.ok(documentService.hasDocuments(projetId));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        try {
            Document document = documentService.getDocumentById(id)
                    .orElseThrow(() -> new DocumentNotFoundException("Document non trouvé"));

            if (document.getChemin() == null) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(document.getChemin());
            byte[] fileBytes = Files.readAllBytes(path);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .body(fileBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String saveFile(MultipartFile fichier) throws IOException {
        String basePath = System.getProperty("user.dir");
        String uploadDir = basePath + File.separator + "uploads" + File.separator + "documents" + File.separator;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + fichier.getOriginalFilename();
        String filePath = uploadDir + fileName;
        fichier.transferTo(new File(filePath));

        return filePath;
    }

    private void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.err.println("Erreur suppression fichier: " + e.getMessage());
        }
    }
}