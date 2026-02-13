package fr.univ.lille.kafka_kata.kafka;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/messages")
    public ResponseEntity<Void> publishMessage(@RequestBody String message) {
        kafkaProducer.sendMessage(message);
        return ResponseEntity.accepted().build();
    }
}
