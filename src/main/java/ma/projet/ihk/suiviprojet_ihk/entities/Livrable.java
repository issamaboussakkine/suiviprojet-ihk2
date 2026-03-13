package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Document livrable produit à l'issue d'une phase.
 * Justifie la clôture d'une phase et constitue le référentiel documentaire du projet.
 * Différent de Document : un livrable est un RÉSULTAT de phase destiné au client,
 * un Document est une pièce technique interne au projet.
 */

@Entity
@Table(name = "livrable")
@Data @NoArgsConstructor @AllArgsConstructor
public class Livrable {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String code;
  private String libelle;
  private String description;

  // Chemin vers le fichier sur le serveur
  private String chemin;

  // Phase qui a produit ce livrable
  @ManyToOne
  @JoinColumn(name = "phase_id")
  private Phase phase;
}