package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import ma.projet.ihk.suiviprojet_ihk.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Document updateDocument(Long id, Document document) {
        if (documentRepository.existsById(Math.toIntExact(id))) {
            document.setId(Math.toIntExact(id));
            return documentRepository.save(document);
        }
        return null;
    }

    @Override
    public void deleteDocument(Long id) {
        documentRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public List<Document> getDocumentsByProjet(Long projetId) {
        return documentRepository.findByProjetId(Math.toIntExact(projetId));
    }

    @Override
    public long countDocumentsByProjet(Long projetId) {
        return documentRepository.findByProjetId(Math.toIntExact(projetId)).size();
    }

    @Override
    public boolean hasDocuments(Long projetId) {
        return !documentRepository.findByProjetId(Math.toIntExact(projetId)).isEmpty();
    }
}