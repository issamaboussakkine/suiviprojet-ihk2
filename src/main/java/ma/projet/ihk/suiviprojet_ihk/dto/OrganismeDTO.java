package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganismeDTO {
    private int id;
    private String code;
    private String nom;
    private String adresse;
    private String telephone;
    private String nomContact;
    private String emailContact;
    private String siteWeb;
    private long nombreProjets;        // Nombre de projets pour cet organisme
}