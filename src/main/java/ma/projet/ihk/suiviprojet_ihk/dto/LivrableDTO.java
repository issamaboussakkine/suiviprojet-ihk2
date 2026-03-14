package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivrableDTO {
    private int id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;
    private String phaseLibelle;        // Nom de la phase
    private int phaseId;                 // ID de la phase
    private String projetNom;            // Nom du projet
}