package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin(origins = "*")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeService.getAllEmployes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        return employeService.getEmployeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employe> createEmploye(@RequestBody Employe employe) {
        Employe saved = employeService.saveEmploye(employe);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody Employe employe) {
        Employe updated = employeService.updateEmploye(id, employe);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        employeService.deleteEmploye(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Employe> getByMatricule(@PathVariable String matricule) {
        return employeService.getEmployeByMatricule(matricule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Employe> getByEmail(@PathVariable String email) {
        return employeService.getEmployeByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<Employe> getByLogin(@PathVariable String login) {
        return employeService.getEmployeByLogin(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<Employe> searchByNom(@RequestParam String nom) {
        return employeService.searchEmployesByNom(nom);
    }

    @PostMapping("/login")
    public ResponseEntity<Employe> login(@RequestParam String login, @RequestParam String password) {
        Employe employe = employeService.authentifier(login, password);
        return employe != null ? ResponseEntity.ok(employe) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}