package br.com.alura;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class EmailService {
    public static void main(String[] args) {
        //Criando um consumidor
        var consumer = new KafkaConsumer<String, String>(properties());

        //Aqui informamos qual o tópico a ser consumido, podendo também passar uma lista, sendo uma coisa muito rara
        consumer.subscribe(Collections.singletonList("ECOMMERCE_SEND_EMAIL"));

        while (true) {
            //Aqui o consumer verifica por algum tempo, se existe mais alguma mensagem
            var records = consumer.poll(Duration.ofMillis(10000));
            if (!records.isEmpty()) {
                System.out.println("Encontrei " + records.count() + " registros");
                for (var record : records) {
                    System.out.println("---------------------------------");
                    System.out.println("Email enviado");
                    System.out.println(record.key());
                    System.out.println(record.value());
                    System.out.println(record.partition());
                    System.out.println(record.offset());
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        //ignora
//                        e.printStackTrace();
//                    }

                    System.out.println("Email enviado");
                }
            }else
                System.out.println("Não há mais registros");
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //Nessa propriedade informamos o nome do grupo que receberá as mensagens
        //Se existirem vários serviços com o mesmo nome de grupo, as mensagens serão distribuidos entres esses vários serviços
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, EmailService.class.getSimpleName());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return properties;
    }
}
