package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private int id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;
    private String projetNom;            // Nom du projet
    private int projetId;                 // ID du projet
}