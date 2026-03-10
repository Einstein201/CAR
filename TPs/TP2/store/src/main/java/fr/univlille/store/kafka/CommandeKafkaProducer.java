package fr.univlille.store.kafka;

import fr.univlille.store.model.Commande;
import fr.univlille.store.model.Ligne;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommandeKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic.order-submitted}")
    private String topic;

    public CommandeKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void envoyerCommandeSoumise(Commande commande) {
        for (Ligne ligne : commande.getLignes()) {
            String message = commande.getId() + ";" + ligne.getLibelle() + ";" + ligne.getQuantite();
            kafkaTemplate.send(topic, message);
        }
    }
}
