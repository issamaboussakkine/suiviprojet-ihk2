package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.LivrableDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.exception.LivrableNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.LivrableMapper;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import ma.projet.ihk.suiviprojet_ihk.service.LivrableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livrables")
@CrossOrigin(origins = "*")
public class LivrableController {

    @Autowired
    private LivrableService livrableService;

    @Autowired
    private LivrableMapper livrableMapper;

    @Autowired
    private PhaseRepository phaseRepository;

    private final String UPLOAD_DIR = "uploads/livrables/";

    @GetMapping
    public List<LivrableDTO> getAllLivrables() {
        List<Livrable> livrables = livrableService.getAllLivrables();
        return livrables.stream()
                .map(livrableMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivrableDTO> getLivrableById(@PathVariable Long id) {
        Livrable livrable = livrableService.getLivrableById(id)
                .orElseThrow(() -> new LivrableNotFoundException("Livrable non trouvé"));
        return ResponseEntity.ok(livrableMapper.toDto(livrable));
    }

    @PostMapping
    public ResponseEntity<LivrableDTO> createLivrable(@RequestBody LivrableDTO dto) {
        Livrable livrable = livrableMapper.toEntity(dto);
        Livrable saved = livrableService.saveLivrable(livrable);
        return new ResponseEntity<>(livrableMapper.toDto(saved), HttpStatus.CREATED);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadLivrable(
            @RequestParam("phaseId") Long phaseId,
            @RequestParam("code") String code,
            @RequestParam("libelle") String libelle,
            @RequestParam("description") String description,
            @RequestParam(value = "fichier", required = false) MultipartFile fichier) {

        try {
            Phase phase = phaseRepository.findById(Math.toIntExact(phaseId))
                    .orElseThrow(() -> new RuntimeException("Phase non trouvée"));

            Livrable livrable = new Livrable();
            livrable.setCode(code);
            livrable.setLibelle(libelle);
            livrable.setDescription(description);
            livrable.setPhase(phase);

            if (fichier != null && !fichier.isEmpty()) {
                String chemin = saveFile(fichier);
                livrable.setChemin(chemin);
            }

            Livrable saved = livrableService.saveLivrable(livrable);
            return new ResponseEntity<>(livrableMapper.toDto(saved), HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivrableDTO> updateLivrable(@PathVariable Long id, @RequestBody LivrableDTO dto) {
        Livrable existingLivrable = livrableService.getLivrableById(id)
                .orElseThrow(() -> new LivrableNotFoundException("Livrable non trouvé"));

        livrableMapper.updateEntityFromDto(dto, existingLivrable);
        Livrable updated = livrableService.saveLivrable(existingLivrable);
        return ResponseEntity.ok(livrableMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivrable(@PathVariable Long id) {
        Livrable livrable = livrableService.getLivrableById(id).orElse(null);
        if (livrable != null && livrable.getChemin() != null) {
            deleteFile(livrable.getChemin());
        }
        livrableService.deleteLivrable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/phase/{phaseId}")
    public List<LivrableDTO> getLivrablesByPhase(@PathVariable Long phaseId) {
        List<Livrable> livrables = livrableService.getLivrablesByPhase(phaseId);
        return livrables.stream()
                .map(livrableMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/phase/{phaseId}/count")
    public ResponseEntity<Long> countLivrablesByPhase(@PathVariable Long phaseId) {
        return ResponseEntity.ok(livrableService.countLivrablesByPhase(phaseId));
    }

    @GetMapping("/phase/{phaseId}/exists")
    public ResponseEntity<Boolean> hasLivrables(@PathVariable Long phaseId) {
        return ResponseEntity.ok(livrableService.hasLivrables(phaseId));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        try {
            Livrable livrable = livrableService.getLivrableById(id)
                    .orElseThrow(() -> new LivrableNotFoundException("Livrable non trouvé"));

            if (livrable.getChemin() == null) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(livrable.getChemin());
            byte[] fileBytes = Files.readAllBytes(path);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .body(fileBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String saveFile(MultipartFile fichier) throws IOException {
        String basePath = System.getProperty("user.dir");
        String uploadDir = basePath + File.separator + "uploads" + File.separator + "livrables" + File.separator;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + fichier.getOriginalFilename();
        String filePath = uploadDir + fileName;
        fichier.transferTo(new File(filePath));

        return filePath;
    }

    private void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.err.println("Erreur suppression fichier: " + e.getMessage());
        }
    }
}