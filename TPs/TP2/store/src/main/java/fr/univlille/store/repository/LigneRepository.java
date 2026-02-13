package fr.univlille.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.univlille.store.model.Ligne;

public interface LigneRepository extends JpaRepository<Ligne, Long> {
}
