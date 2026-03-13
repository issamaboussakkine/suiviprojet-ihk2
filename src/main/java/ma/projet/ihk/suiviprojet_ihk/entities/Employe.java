package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;

  // Représente un employé de l'entreprise.
 // Chaque employé possède un profil qui détermine ses permissions.

@Entity
@Table(name = "employe")
@Data @NoArgsConstructor @AllArgsConstructor
public class Employe {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  // Identifiant RH unique de l'employé
  private String matricule;
  private String nom;
  private String prenom;
  private String telephone;
  private String email;

  //  Identifiants de connexion à l'application
  private String login;
  private String password;

  // Profil fonctionnel → détermine les droits
  @ManyToOne
  @JoinColumn(name = "profil_id")
  private Profil profil;
}