package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Document technique interne associé à un projet (CDC, specs, architecture, CR...).
 * Différent de Livrable : un Document est une pièce de travail interne,
 * un Livrable est un résultat de phase livré au client.
 */

@Entity
@Table(name = "document")
@Data @NoArgsConstructor @AllArgsConstructor
public class Document {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String code;
  private String libelle;
  private String description;

  // Chemin d'accès au fichier stocké sur le serveur
  private String chemin;

  // Projet auquel ce document est rattaché
  @ManyToOne
  @JoinColumn(name = "projet_id")
  private Projet projet;
}