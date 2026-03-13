package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

/**
 * Clé composée de l'entité Affectation.
 * Identifie uniquement une affectation par la combinaison (employeId, phaseId).
 * Obligatoire pour éviter un id artificiel sur la table d'association.
 */

@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor
public class AffectationId implements Serializable {

  // ID de l'employé affecté
  private int employeId;

  // ID de la phase sur laquelle l'employé est affecté
  private int phaseId;
}