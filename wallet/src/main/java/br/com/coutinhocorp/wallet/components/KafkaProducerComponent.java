package br.com.coutinhocorp.wallet.components;

import br.com.coutinhocorp.wallet.model.Transaction;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerComponent {
    private final static int PARTITION_COUNT = 0;
    private final static String TOPIC = "br.com.coutinhocorp.transaction";
    private final static short REPLICATION_FACTOR = 1;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducerComponent(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void configureTopic(KafkaAdmin kafkaAdmin) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(TOPIC, PARTITION_COUNT, REPLICATION_FACTOR));
    }

    public void sendTransactionMessage(Transaction transaction) {
        kafkaTemplate.send(TOPIC, PARTITION_COUNT, "key", transaction);
    }
}