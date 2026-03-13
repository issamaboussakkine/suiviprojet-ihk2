package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Représente une facture émise pour une phase terminée.
 * Une facture est liée à UNE SEULE phase (relation OneToOne).
 * Seul le comptable peut créer et gérer les factures.
 */

@Entity
@Table(name = "facture")
@Data @NoArgsConstructor @AllArgsConstructor
public class Facture {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  // Numéro unique de la facture

  // Date d'émission de la facture
  private LocalDate dateFacture;

  // Phase concernée — une phase ne peut avoir qu'une seule facture
  @OneToOne
  @JoinColumn(name = "phase_id")
  private Phase phase;
}