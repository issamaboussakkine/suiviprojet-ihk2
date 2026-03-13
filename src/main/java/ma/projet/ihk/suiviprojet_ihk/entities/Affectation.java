package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Table d'association entre Employe et Phase.
 * Représente l'affectation d'un employé à une phase du projet.
 * Utilise une clé composée (employeId + phaseId) sans id artificiel.
 */
@Entity
@Table(name = "affectation")
@Data @NoArgsConstructor @AllArgsConstructor
public class Affectation {

  // Clé composée : identifie l'affectation de façon unique
  @EmbeddedId
  private AffectationId id;

  // Période d'intervention de l'employé sur la phase
  private LocalDate dateDebut;
  private LocalDate dateFin;

  // Employé affecté à la phase
  @ManyToOne
  @MapsId("employeId")
  @JoinColumn(name = "employe_id")
  private Employe employe;

  // Phase sur laquelle l'employé intervient
  @ManyToOne
  @MapsId("phaseId")
  @JoinColumn(name = "phase_id")
  private Phase phase;
}