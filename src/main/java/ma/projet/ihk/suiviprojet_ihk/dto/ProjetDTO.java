package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetDTO {
    private int id;
    private String code;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double montant;
    private String statut;           // EN_ATTENTE | VALIDE | EN_COURS | TERMINE
    private int tauxAvancement;      // Calculé depuis les phases (phases terminées / total × 100)
    private String organismeNom;     // Juste le nom de l'organisme
    private int organismeId;         // L'ID de l'organisme
    private String chefProjetNom;    // Nom du chef de projet
    private int chefProjetId;        // ID du chef de projet
    private long nombrePhases;       // Compteur de phases
}