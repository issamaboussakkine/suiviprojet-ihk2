package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.*;

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
  private double montant;
  private boolean etatRealisation;
  private boolean etatFacturation;
  private boolean etatPaiement;

  @ManyToOne
  @JoinColumn(name = "projet_id")
  private Projet projet;

  @JsonIgnore
  @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
  private List<Livrable> livrables = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
  private List<Affectation> affectations = new ArrayList<>();

  // ========== MÉTHODES EXISTANTES ==========

  public String getStatut() {
    if (this.etatRealisation) {
      return "TERMINEE";
    }
    LocalDate aujourdhui = LocalDate.now();
    if (this.dateDebut != null && !aujourdhui.isBefore(this.dateDebut)) {
      return "EN_COURS";
    }
    return "NON_COMMENCEE";
  }

  // ========== NOUVELLES MÉTHODES ==========

  // Démarrer la phase
  public void demarrer() {
    if ("NON_COMMENCEE".equals(getStatut())) {
      this.etatRealisation = false;
      this.dateDebut = LocalDate.now();
    } else {
      throw new IllegalStateException("La phase ne peut pas être démarrée. Statut: " + getStatut());
    }
  }

  // Terminer la phase
  public void terminer() {
    if ("EN_COURS".equals(getStatut())) {
      this.etatRealisation = true;
      this.dateFin = LocalDate.now();
    } else {
      throw new IllegalStateException("Seule une phase en cours peut être terminée. Statut: " + getStatut());
    }
  }

  // Vérifier si la phase est terminée
  public boolean isTerminee() {
    return this.etatRealisation;
  }

  // Vérifier si la phase est en cours
  public boolean isEnCours() {
    return "EN_COURS".equals(getStatut());
  }

  // Vérifier si la phase est non commencée
  public boolean isNonCommencee() {
    return "NON_COMMENCEE".equals(getStatut());
  }
}