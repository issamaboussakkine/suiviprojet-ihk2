package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class AffectationId implements Serializable {
  private int employeId;
  private int phaseId;

  public AffectationId(int employeId, int phaseId) {
    this.employeId = employeId;
    this.phaseId = phaseId;
  }
}