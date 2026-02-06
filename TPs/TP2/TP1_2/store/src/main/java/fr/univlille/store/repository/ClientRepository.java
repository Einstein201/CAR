package fr.univlille.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.univlille.store.model.Client;

public interface ClientRepository extends JpaRepository<Client, String> {
}
