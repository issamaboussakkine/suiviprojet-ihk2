package ma.projet.ihk.suiviprojet_ihk.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;


  // Représente un organisme client externe.
  // Un même client peut être lié à plusieurs projets.

@Entity
@Table(name = "organisme")
@Data @NoArgsConstructor @AllArgsConstructor
public class Organisme {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  // Code unique de l'organisme
  private String code;
  private String nom;
  private String adresse;
  private String telephone;

  // Informations du contact principal chez le client
  private String nomContact;
  private String emailContact;
  private String siteWeb;

  /** Liste des projets réalisés pour cet organisme
   *  JsonIgnore évite la boucle infinie Organisme→Projet→Organisme */
  @JsonIgnore
  @OneToMany(mappedBy = "organisme", cascade = CascadeType.ALL)
  private List<Projet> projets = new ArrayList<>();
}