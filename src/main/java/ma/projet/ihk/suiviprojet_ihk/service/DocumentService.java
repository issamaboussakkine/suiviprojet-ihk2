package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import java.util.List;
import java.util.Optional;

// Service document technique
public interface DocumentService {
    // Opérations de base
    Document saveDocument(Document document);
    Document updateDocument(Long id, Document document);
    void deleteDocument(Long id);

    // Recherches
    Optional<Document> getDocumentById(Long id);
    List<Document> getAllDocuments();
    List<Document> getDocumentsByProjet(Long projetId);

    // Statistiques
    long countDocumentsByProjet(Long projetId);
    boolean hasDocuments(Long projetId);
}