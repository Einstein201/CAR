package fr.univlille.stockservice.repository;

import fr.univlille.stockservice.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
