package ma.projet.ihk.suiviprojet_ihk.repositories;


import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des documents techniques.
 * Permet d'accéder aux pièces internes associées à un projet.
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

  // Récupérer tous les documents d'un projet
  List<Document> findByProjetId(int projetId);
}