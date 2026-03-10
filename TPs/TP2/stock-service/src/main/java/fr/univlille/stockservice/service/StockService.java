package fr.univlille.stockservice.service;

import fr.univlille.stockservice.model.Article;
import fr.univlille.stockservice.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

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
}
