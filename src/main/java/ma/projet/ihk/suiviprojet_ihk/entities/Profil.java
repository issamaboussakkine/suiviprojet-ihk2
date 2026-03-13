package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;


  // Représente le profil fonctionnel d'un employé (ex: Admin, Comptable, Chef de projet...)
  // Détermine les droits d'accès dans l'application.

@Entity
@Table(name = "profil")
@Data @NoArgsConstructor @AllArgsConstructor
public class Profil {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  // Code unique du profil (ex: "CP", "COMPT")
  private String code;

  // Libellé lisible (ex: "Chef de projet")
  private String libelle;
}