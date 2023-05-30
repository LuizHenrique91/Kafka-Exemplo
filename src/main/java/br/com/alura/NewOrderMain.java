package br.com.alura;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //Cria um novo produtor de mensagens passando em seu construtor o objeto properties
        //Properties é utilizado para setar quais configurações o KafkaProducer receberá
        var producer = new KafkaProducer<String, String>(properties());
        var key = UUID.randomUUID().toString();
        var value = "Teste de produto";

        var email = "Bem-Vindo, Vamos processar sua ordem";

        //Cria um registro com o nome do tópico, passando também sua chave e valor.
        var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", key, value);
        var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", key, email);

        //Aqui utilizamos uma função callback para informar se a mensagem foi enviado com sucesso ou não.
        Callback callback = (data, exc) -> {
            if (Objects.nonNull(exc)) {
                exc.printStackTrace();
                return;
            }
            System.out.println("Sucesso enviando " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
        };
        for(int x = 0; x <= 10; x++) {
            //Envia um registro para ser armazenado no Kafka
            producer.send(record, callback).get();
            producer.send(emailRecord, callback).get();
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); //Onde esta rodando o broker(Kafka)
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); //Aqui informa que a chave do Producer será serializada
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); //Aqui informa que o valor do Producer será serializado
        return properties;
    }
}
