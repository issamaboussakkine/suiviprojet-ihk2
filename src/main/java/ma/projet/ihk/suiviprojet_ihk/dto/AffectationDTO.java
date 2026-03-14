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
    private String employeNom;           // Nom de l'employé
    private String phaseLibelle;          // Nom de la phase
    private String projetNom;             // Nom du projet
}