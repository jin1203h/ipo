package com.ipo.ipo.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipo.ipo.config.KafkaProperties;
import com.ipo.ipo.domain.event.SubscribeCanceled;
import com.ipo.ipo.domain.event.SubscribeCompleted;
import com.ipo.ipo.service.IpoService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class IpoConsumer {
    private final Logger log = LoggerFactory.getLogger(IpoConsumer.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static final String SUBSCRIBECOMPLETED = "SubscribeCompleted";
    public static final String SUBSCRIBECANCELED = "SubscribeCanceled";

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, String> kafkaConsumer;

    private final IpoService ipoService;

    private ExecutorService executorService = Executors.newCachedThreadPool();


    public IpoConsumer(KafkaProperties kafkaProperties, IpoService ipoService) {
        this.kafkaProperties = kafkaProperties;
        this.ipoService = ipoService;
    }

    @PostConstruct
    public void start(){
        log.info("Kafka consumer starting ...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        kafkaConsumer.subscribe(Collections.singleton(SUBSCRIBECOMPLETED));
        log.info("Kafka consumer started");

        executorService.execute(()-> {
                try {

                    while (!closed.get()){
                        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                        for(ConsumerRecord<String, String> record: records) {
//                            if (kafkaConsumer SUBSCRIBECOMPLETED) {
                                log.info("Consumed message in {} : {}", SUBSCRIBECOMPLETED, record.value());
                                ObjectMapper objectMapper = new ObjectMapper();
                                SubscribeCompleted subscribeCompleted = objectMapper.readValue(record.value(), SubscribeCompleted.class);
                                ipoService.addIpo(subscribeCompleted);
//                            } else if (SUBSCRIBECANCELED) {
//                                log.info("Consumed message in {} : {}", SUBSCRIBECANCELED, record.value());
//                                ObjectMapper objectMapper = new ObjectMapper();
//                                SubscribeCanceled subscribeCanceled = objectMapper.readValue(record.value(), SubscribeCanceled.class);
//                                ipoService.subtractIpo(subscribeCanceled);
//                            }

                        }

                    }
                    kafkaConsumer.commitSync();

                }catch (WakeupException e){
                    if(!closed.get()){
                        throw e;
                    }

                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }finally {
                    log.info("kafka consumer close");
                    kafkaConsumer.close();
                }

            }



        );
    }


    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}
