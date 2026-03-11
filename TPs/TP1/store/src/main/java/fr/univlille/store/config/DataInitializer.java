package fr.univlille.store.config;

import fr.univlille.store.model.Produit;
import fr.univlille.store.repository.ProduitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initProduits(ProduitRepository produitRepository) {
        return args -> {
            if (produitRepository.count() == 0) {
                produitRepository.save(new Produit("Stylo", 1.50, 100));
                produitRepository.save(new Produit("Cahier", 3.00, 50));
                produitRepository.save(new Produit("Calculatrice", 12.99, 20));
                produitRepository.save(new Produit("Règle", 0.80, 75));
                produitRepository.save(new Produit("Sac à dos", 25.00, 10));
            }
        };
    }
}
