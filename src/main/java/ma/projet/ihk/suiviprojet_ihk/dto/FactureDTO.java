package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO {
    private int id;
    private LocalDate dateFacture;
    private String phaseLibelle;       // Nom de la phase
    private int phaseId;                // ID de la phase
    private String projetNom;           // Nom du projet
    private double montant;              // Montant de la phase
}