package ma.projet.ihk.suiviprojet_ihk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhaseDTO {
    private int id;
    private String code;
    private String libelle;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double montant;
    private boolean etatRealisation;
    private boolean etatFacturation;
    private boolean etatPaiement;
    private String projetNom;
    private int projetId;
    private long nombreLivrables;
    private long nombreEmployes;
}