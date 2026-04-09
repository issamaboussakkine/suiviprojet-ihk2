package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectationDTO {
    private int employeId;
    private int phaseId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String employeNom;
    private String phaseLibelle;
    private String projetNom;
}