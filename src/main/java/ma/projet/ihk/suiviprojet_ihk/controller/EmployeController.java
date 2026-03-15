package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.EmployeDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.exception.EmployeNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.EmployeMapper;
import ma.projet.ihk.suiviprojet_ihk.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin(origins = "*")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeMapper employeMapper;

    // GET tous les employés
    @GetMapping
    public List<EmployeDTO> getAllEmployes() {
        List<Employe> employes = employeService.getAllEmployes();
        return employes.stream()
                .map(employeMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> getEmployeById(@PathVariable Long id) {
        Employe employe = employeService.getEmployeById(id)
                .orElseThrow(() -> new EmployeNotFoundException("Employé non trouvé avec l'ID: " + id));
        return ResponseEntity.ok(employeMapper.toDto(employe));
    }

    // POST créer un employé
    @PostMapping
    public ResponseEntity<EmployeDTO> createEmploye(@RequestBody EmployeDTO dto) {
        Employe employe = employeMapper.toEntity(dto);
        Employe saved = employeService.saveEmploye(employe);
        return new ResponseEntity<>(employeMapper.toDto(saved), HttpStatus.CREATED);
    }

    // PUT modifier un employé
    @PutMapping("/{id}")
    public ResponseEntity<EmployeDTO> updateEmploye(@PathVariable Long id, @RequestBody EmployeDTO dto) {
        Employe existingEmploye = employeService.getEmployeById(id)
                .orElseThrow(() -> new EmployeNotFoundException("Employé non trouvé avec l'ID: " + id));

        employeMapper.updateEntityFromDto(dto, existingEmploye);
        Employe updated = employeService.saveEmploye(existingEmploye);
        return ResponseEntity.ok(employeMapper.toDto(updated));
    }

    // DELETE supprimer un employé
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        employeService.deleteEmploye(id);
        return ResponseEntity.noContent().build();
    }

    // GET par matricule
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<EmployeDTO> getByMatricule(@PathVariable String matricule) {
        Employe employe = employeService.getEmployeByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec matricule: " + matricule));
        return ResponseEntity.ok(employeMapper.toDto(employe));
    }

    // GET par email
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeDTO> getByEmail(@PathVariable String email) {
        Employe employe = employeService.getEmployeByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec email: " + email));
        return ResponseEntity.ok(employeMapper.toDto(employe));
    }

    // GET par login
    @GetMapping("/login/{login}")
    public ResponseEntity<EmployeDTO> getByLogin(@PathVariable String login) {
        Employe employe = employeService.getEmployeByLogin(login)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec login: " + login));
        return ResponseEntity.ok(employeMapper.toDto(employe));
    }

    // GET recherche par nom
    @GetMapping("/search")
    public List<EmployeDTO> searchByNom(@RequestParam String nom) {
        List<Employe> employes = employeService.searchEmployesByNom(nom);
        return employes.stream()
                .map(employeMapper::toDto)
                .collect(Collectors.toList());
    }

    // POST authentification
    @PostMapping("/login")
    public ResponseEntity<EmployeDTO> login(@RequestParam String login, @RequestParam String password) {
        Employe employe = employeService.authentifier(login, password);
        if (employe != null) {
            return ResponseEntity.ok(employeMapper.toDto(employe));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}