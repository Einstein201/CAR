package fr.univlille.stockservice.service;

import fr.univlille.stockservice.model.Article;
import fr.univlille.stockservice.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    private final ArticleRepository articleRepository;

    public StockService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public void initIfEmpty() {
        if (articleRepository.count() == 0) {
            articleRepository.save(new Article("stylo", 100));
            articleRepository.save(new Article("cahier", 80));
            articleRepository.save(new Article("clavier", 30));
        }
    }

    public void decrementerStock(String libelle, int quantite) {
        if (libelle == null || libelle.trim().isEmpty() || quantite <= 0) {
            logger.warn("Message stock invalide: libelle='{}', quantite={}", libelle, quantite);
            return;
        }

        String libelleNormalise = libelle.trim().toLowerCase();
        Optional<Article> articleOpt = articleRepository.findById(libelleNormalise);
        if (articleOpt.isEmpty()) {
            logger.warn("Article introuvable dans le stock: {}", libelleNormalise);
            return;
        }

        Article article = articleOpt.get();
        int nouveauStock = Math.max(0, article.getQuantiteStock() - quantite);
        article.setQuantiteStock(nouveauStock);
        articleRepository.save(article);
        logger.info("Stock mis a jour pour {}: -{} (reste {})", libelleNormalise, quantite, nouveauStock);
    }
}
