package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import ma.projet.ihk.suiviprojet_ihk.repositories.LivrableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LivrableServiceImpl implements LivrableService {

    @Autowired
    private LivrableRepository livrableRepository;

    @Override
    public Livrable saveLivrable(Livrable livrable) {
        return livrableRepository.save(livrable);
    }

    @Override
    public Livrable updateLivrable(Long id, Livrable livrable) {
        if (livrableRepository.existsById(Math.toIntExact(id))) {
            livrable.setId(Math.toIntExact(id));
            return livrableRepository.save(livrable);
        }
        return null;
    }

    @Override
    public void deleteLivrable(Long id) {
        livrableRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Optional<Livrable> getLivrableById(Long id) {
        return livrableRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<Livrable> getAllLivrables() {
        return livrableRepository.findAll();
    }

    @Override
    public List<Livrable> getLivrablesByPhase(Long phaseId) {
        return livrableRepository.findByPhaseId(Math.toIntExact(phaseId));
    }

    @Override
    public long countLivrablesByPhase(Long phaseId) {
        return livrableRepository.findByPhaseId(Math.toIntExact(phaseId)).size();
    }

    @Override
    public boolean hasLivrables(Long phaseId) {
        return !livrableRepository.findByPhaseId(Math.toIntExact(phaseId)).isEmpty();
    }
}