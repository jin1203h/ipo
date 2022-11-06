package com.ipo.ipo.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipo.ipo.config.KafkaProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class IpoProducerImpl implements IpoProducer {

    private final Logger log = LoggerFactory.getLogger(IpoProducerImpl.class);

    private static final String SUBSCRIBECOMPLETED = "SubscribeCompleted";
    private static final String SUBSCRIBECANCELED = "SubscribeCanceled";

    private final KafkaProperties kafkaProperties;

    private final static Logger logger = LoggerFactory.getLogger(IpoProducerImpl.class);
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public IpoProducerImpl(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize(){
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }


    /******
     * kafka 메세지 수신 후, 결과 메세지 받도록 변경
     *
     * *******/


//    public void saveSubscribe(SubscribeCompleted subscribeCompleted) throws ExecutionException, InterruptedException, JsonProcessingException {
//        String message = objectMapper.writeValueAsString(subscribeCompleted);
//        producer.send(new ProducerRecord<>(SUBSCRIBECOMPLETED, message)).get();
//    }
//
//    public void cancelSubscribe(SubscribeCanceled subscribeCanceled) throws ExecutionException, InterruptedException,JsonProcessingException {
//        String message = objectMapper.writeValueAsString(subscribeCanceled);
//        producer.send(new ProducerRecord<>(SUBSCRIBECANCELED, message)).get();
//    }

    @PreDestroy
    public void shutdown(){
        log.info("Shutdown Kafka producer");
        producer.close();
    }



}
