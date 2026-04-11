package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "projet")
@Data @NoArgsConstructor @AllArgsConstructor
public class Projet {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String code;
  private String nom;
  private String description;
  private LocalDate dateDebut;
  private LocalDate dateFin;
  private double montant;

  // Statut du projet (EN_ATTENTE, VALIDE, EN_COURS, TERMINE)
  private String statut = "EN_ATTENTE";

  @ManyToOne
  @JoinColumn(name = "organisme_id")
  private Organisme organisme;

  @ManyToOne
  @JoinColumn(name = "chef_projet_id")
  private Employe chefProjet;

  @JsonIgnore
  @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL)
  private List<Phase> phases = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL)
  private List<Document> documents = new ArrayList<>();

  // ========== MÉTHODES ==========

  // Calcul du taux d'avancement basé sur les phases
  public int getTauxAvancement() {
    if (this.phases == null || this.phases.isEmpty()) {
      return 0;
    }
    long totalPhases = this.phases.size();
    long phasesTerminees = this.phases.stream()
            .filter(phase -> phase.isEtatRealisation())
            .count();
    return (int) (phasesTerminees * 100 / totalPhases);
  }

  // Vérifier si le projet peut être validé
  public boolean peutEtreValide() {
    return "EN_ATTENTE".equals(this.statut);
  }

  // Valider le projet (par Directeur)
  public void valider() {
    if (peutEtreValide()) {
      this.statut = "VALIDE";
    } else {
      throw new IllegalStateException("Le projet ne peut pas être validé. Statut actuel: " + this.statut);
    }
  }

  // Vérifier si le projet peut démarrer
  public boolean peutDemarrer() {
    return "VALIDE".equals(this.statut);
  }

  // Démarrer le projet (par Chef de projet)
  public void demarrer() {
    if (peutDemarrer()) {
      this.statut = "EN_COURS";
    } else {
      throw new IllegalStateException("Le projet ne peut pas démarrer. Statut actuel: " + this.statut);
    }
  }

  // Vérifier si le projet peut être terminé
  public boolean peutTerminer() {
    return "EN_COURS".equals(this.statut);
  }

  // Terminer le projet
  public void terminer() {
    if (peutTerminer()) {
      this.statut = "TERMINE";
    } else {
      throw new IllegalStateException("Seul un projet en cours peut être terminé. Statut actuel: " + this.statut);
    }
  }

  // Vérifier si le projet est terminé
  public boolean isTermine() {
    return "TERMINE".equals(this.statut);
  }

  // Vérifier si le projet est en cours
  public boolean isEnCours() {
    return "EN_COURS".equals(this.statut);
  }

  // Vérifier si le projet est validé
  public boolean isValide() {
    return "VALIDE".equals(this.statut);
  }

  // Vérifier si le projet est en attente
  public boolean isEnAttente() {
    return "EN_ATTENTE".equals(this.statut);
  }
}