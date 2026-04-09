package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String login;
  private String email;
  private String password;
  private String nom;
  private String prenom;
  private String matricule;
  private String telephone;

  @ManyToOne
  @JoinColumn(name = "profil_id")
  private Profil profil;

  // Les getters et setters sont générés par Lombok (@Data)
}