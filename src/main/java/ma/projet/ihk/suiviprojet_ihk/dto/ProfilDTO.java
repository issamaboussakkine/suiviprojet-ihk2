package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilDTO {
    private int id;
    private String code;
    private String libelle;
    private long nombreEmployes;          // Nombre d'employés avec ce profil
}