package fr.univlille.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.univlille.store.model.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
