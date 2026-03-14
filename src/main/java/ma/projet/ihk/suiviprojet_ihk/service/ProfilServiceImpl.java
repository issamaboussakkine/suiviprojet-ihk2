package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfilServiceImpl implements ProfilService {

    @Autowired
    private ProfilRepository profilRepository;

    @Override
    public Profil saveProfil(Profil profil) {
        return profilRepository.save(profil);
    }

    @Override
    public Profil updateProfil(Long id, Profil profil) {
        if (profilRepository.existsById(Math.toIntExact(id))) {
            profil.setId(Math.toIntExact(id));
            return profilRepository.save(profil);
        }
        return null;
    }

    @Override
    public void deleteProfil(Long id) {
        profilRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Optional<Profil> getProfilById(Long id) {
        return profilRepository.findById(Math.toIntExact(id));
    }

    @Override
    public Optional<Profil> getProfilByCode(String code) {
        return Optional.ofNullable(profilRepository.findByCode(code));
    }

    @Override
    public List<Profil> getAllProfils() {
        return profilRepository.findAll();
    }

    @Override
    public boolean isCodeUnique(String code) {
        return profilRepository.findByCode(code) == null;
    }

    @Override
    public List<String> getAllCodes() {
        return profilRepository.findAll()
                .stream()
                .map(Profil::getCode)
                .collect(Collectors.toList());
    }
}