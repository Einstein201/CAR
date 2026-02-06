package fr.univlille.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.univlille.store.model.Commande;
import fr.univlille.store.model.Client;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClient(Client client);
}
