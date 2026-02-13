package fr.univ.lille.kafka_kata.kafka;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RootController {

    @GetMapping("/")
    public String home() {
        return "Kafka Kata is running. Use POST /messages to publish.";
    }
}
