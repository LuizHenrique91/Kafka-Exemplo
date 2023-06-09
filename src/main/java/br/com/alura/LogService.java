package br.com.alura;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {
        //Criando um consumidor
        var consumer = new KafkaConsumer<String, String>(properties());

        //Passamos uma expressão regular, neste caso, estamos dizendo que o método subscribe receberá todos os tópicos
        //que contenha como prefixo a palavra "ECOMMERCE"
        consumer.subscribe(Pattern.compile("ECOMMERCE.*"));

        while (true) {
            //Aqui o consumer verifica por algum tempo, se existe mais alguma mensagem
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Encontrei " + records.count() + " registros");
                for (var record : records) {
                    System.out.println("---------------------------------");
                    System.out.println("LOG: " + record.topic());
                    System.out.println(record.key());
                    System.out.println(record.value());
                    System.out.println(record.partition());
                    System.out.println(record.offset());
                }
            }
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //Nessa propriedade informamos o nome do grupo que receberá as mensagens
        //Se existirem vários serviços com o mesmo nome de grupo, as mensagens serão distribuidos entres esses vários serviços
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());
        return properties;
    }
}
