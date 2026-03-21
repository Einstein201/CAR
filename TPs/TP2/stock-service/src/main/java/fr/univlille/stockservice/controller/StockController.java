
package fr.univlille.stockservice.controller;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.univlille.stockservice.model.Article;
import fr.univlille.stockservice.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Endpoint REST pour la liste des articles en JSON
    @GetMapping("/api/stocks")
    @ResponseBody
    public List<Article> getStocksJson() {
        return stockService.getAllArticles();
    }

    @GetMapping("/stocks")
    public String listerStocks(Model model) {
        List<Article> articles = stockService.getAllArticles();
        model.addAttribute("articles", articles);
        return "stock";
    }
}
