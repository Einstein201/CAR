package fr.univlille.stockservice.controller;

import fr.univlille.stockservice.model.Article;
import fr.univlille.stockservice.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stocks")
    public List<Article> listerStocks() {
        return stockService.getAllArticles();
    }
}
