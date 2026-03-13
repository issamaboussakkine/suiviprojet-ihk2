package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.*;

/**
 * Entité centrale du système.
 * Un projet est rattaché à un organisme client et géré par un chef de projet.
 * Il est décomposé en phases et peut avoir des documents associés.
 */

@Entity
@Table(name = "projet")
@Data @NoArgsConstructor @AllArgsConstructor
public class Projet {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  // Code unique identifiant le projet
  private String code;
  private String nom;
  private String description;
  private LocalDate dateDebut;
  private LocalDate dateFin;

  // Montant global du contrat en MAD
  private double montant;

  // Client pour lequel le projet est réalisé
  @ManyToOne
  @JoinColumn(name = "organisme_id")
  private Organisme organisme;

  // Employé responsable du pilotage du projet
  @ManyToOne
  @JoinColumn(name = "chef_projet_id")
  private Employe chefProjet;

  /** Phases de réalisation du projet
   *  JsonIgnore évite la boucle infinie Projet→Phase→Projet */
  @JsonIgnore
  @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL)
  private List<Phase> phases = new ArrayList<>();

  // Documents techniques associés (CDC, specs, CR...)
  @JsonIgnore
  @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL)
  private List<Document> documents = new ArrayList<>();
}