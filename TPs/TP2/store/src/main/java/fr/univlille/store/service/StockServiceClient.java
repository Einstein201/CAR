package fr.univlille.store.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String stockServiceUrl = "http://localhost:8082/api/stocks";

    public List<Map<String, Object>> getArticles() {
        Map<String, Object>[] articles = restTemplate.getForObject(stockServiceUrl, Map[].class);
        return Arrays.asList(articles);
    }
}
