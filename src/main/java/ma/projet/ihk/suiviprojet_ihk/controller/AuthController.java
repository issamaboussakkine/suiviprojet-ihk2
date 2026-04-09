package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.security.JwtUtil;
import ma.projet.ihk.suiviprojet_ihk.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String login = loginRequest.get("login");
        String password = loginRequest.get("password");

        Employe employe = employeService.authentifier(login, password);

        if (employe != null) {
            String token = jwtUtil.generateToken(employe.getLogin(), "ADMIN");
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("login", employe.getLogin());
            response.put("role", "ADMIN");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Login ou mot de passe incorrect");
    }
}