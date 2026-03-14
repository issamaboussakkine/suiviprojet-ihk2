package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeDTO {
    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String login;
    private String profilLibelle;  // Juste le nom du profil, pas l'objet entier
    private int profilId;           // L'ID du profil
}