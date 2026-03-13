package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.*;

/**
 * Représente une étape de réalisation d'un projet.
 * Chaque phase a ses propres états : réalisation, facturation, paiement.
 * Elle est associée à des employés via Affectation et produit des livrables.
 */
@Entity
@Table(name = "phase")
@Data @NoArgsConstructor @AllArgsConstructor
public class Phase {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String code;
  private String libelle;
  private String description;
  private LocalDate dateDebut;
  private LocalDate dateFin;

  // Montant à régler à la clôture de cette phase
  private double montant;

  // true = phase terminée
  private boolean etatRealisation;

  // true = facture émise pour cette phase
  private boolean etatFacturation;

  // true = paiement reçu pour cette phase
  private boolean etatPaiement;

  // Projet auquel appartient cette phase
  @ManyToOne
  @JoinColumn(name = "projet_id")
  private Projet projet;

  // Documents livrables produits à la fin de cette phase
  @JsonIgnore
  @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
  private List<Livrable> livrables = new ArrayList<>();

  // Affectations des employés sur cette phase
  @JsonIgnore
  @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
  private List<Affectation> affectations = new ArrayList<>();
}