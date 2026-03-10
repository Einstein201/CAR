package fr.univlille.stockservice.kafka;

import fr.univlille.stockservice.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CommandeKafkaConsumerTest {

    @Mock
    private StockService stockService;

    @InjectMocks
    private CommandeKafkaConsumer consumer;

    @Test
    void consommerCommandeSoumise_shouldForwardValidMessageToStockService() {
        consumer.consommerCommandeSoumise("cmd-2026-03-10;casque-audio;4");

        verify(stockService).decrementerStock("casque-audio", 4);
    }

    @Test
    void consommerCommandeSoumise_shouldIgnoreMessageWithInvalidFormat() {
        consumer.consommerCommandeSoumise("cmd-2026-03-10|casque-audio|4");

        verifyNoInteractions(stockService);
    }

    @Test
    void consommerCommandeSoumise_shouldIgnoreMessageWithInvalidQuantity() {
        consumer.consommerCommandeSoumise("cmd-2026-03-10;casque-audio;quatre");

        verifyNoInteractions(stockService);
    }
}
