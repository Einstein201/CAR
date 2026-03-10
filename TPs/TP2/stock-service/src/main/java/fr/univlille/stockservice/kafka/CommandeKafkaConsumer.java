package fr.univlille.stockservice.kafka;

import fr.univlille.stockservice.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CommandeKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CommandeKafkaConsumer.class);

    private final StockService stockService;

    public CommandeKafkaConsumer(StockService stockService) {
        this.stockService = stockService;
    }

    @KafkaListener(topics = "${app.kafka.topic.order-submitted}", groupId = "${spring.kafka.consumer.group-id}")
    public void consommerCommandeSoumise(String message) {
        String[] parts = message.split(";");
        if (parts.length != 3) {
            logger.warn("Message Kafka invalide: {}", message);
            return;
        }

        String libelle = parts[1];
        int quantite;
        try {
            quantite = Integer.parseInt(parts[2]);
        } catch (NumberFormatException ex) {
            logger.warn("Quantite invalide dans le message Kafka: {}", message);
            return;
        }

        stockService.decrementerStock(libelle, quantite);
    }
}
