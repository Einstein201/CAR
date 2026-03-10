package fr.univlille.stockservice.service;

import fr.univlille.stockservice.model.Article;
import fr.univlille.stockservice.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    void initIfEmpty_shouldSeedArticles_whenRepositoryIsEmpty() {
        when(articleRepository.count()).thenReturn(0L);

        stockService.initIfEmpty();

        verify(articleRepository, times(3)).save(any(Article.class));
    }

    @Test
    void initIfEmpty_shouldDoNothing_whenRepositoryIsNotEmpty() {
        when(articleRepository.count()).thenReturn(1L);

        stockService.initIfEmpty();

        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void decrementerStock_shouldUpdateExistingArticle() {
        Article article = new Article("lampe-bureau", 17);
        when(articleRepository.findById("lampe-bureau")).thenReturn(Optional.of(article));

        stockService.decrementerStock(" Lampe-Bureau ", 5);

        ArgumentCaptor<Article> captor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).save(captor.capture());
        assertEquals("lampe-bureau", captor.getValue().getLibelle());
        assertEquals(12, captor.getValue().getQuantiteStock());
    }

    @Test
    void decrementerStock_shouldClampToZero_whenQuantityIsTooHigh() {
        Article article = new Article("souris-ergonomique", 2);
        when(articleRepository.findById("souris-ergonomique")).thenReturn(Optional.of(article));

        stockService.decrementerStock("souris-ergonomique", 9);

        ArgumentCaptor<Article> captor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).save(captor.capture());
        assertEquals(0, captor.getValue().getQuantiteStock());
    }

    @Test
    void decrementerStock_shouldDoNothing_whenArticleDoesNotExist() {
        when(articleRepository.findById("carnet-a5")).thenReturn(Optional.empty());

        stockService.decrementerStock("carnet-a5", 3);

        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void decrementerStock_shouldIgnoreInvalidInput() {
        stockService.decrementerStock(" ", 3);
        stockService.decrementerStock("ecran-4k", 0);
        stockService.decrementerStock(null, 3);

        verifyNoInteractions(articleRepository);
    }
}
