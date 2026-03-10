package fr.univlille.stockservice;

import fr.univlille.stockservice.service.StockService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class StockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initStock(StockService stockService) {
        return args -> stockService.initIfEmpty();
    }
}
