package com.qzt360.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Properties;

/**
 * Created by zhaogj on 18/12/2016.
 */
@Service
@Slf4j
public class TMacService {
    public void tmac2Kafka() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.10.12:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = null;
        try {
            producer = new KafkaProducer<>(props);

            File fileTMacLogPath = new File("/home/zhaogj/java2Kafka/data/tmac");
            log.info("找到{}个文件", fileTMacLogPath.listFiles().length);
            for (File fileTMac : fileTMacLogPath.listFiles()) {
                log.info("处理文件:{}", fileTMac.getPath());
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(fileTMac.getPath())));
                    String strLine = null;
                    long lCount = 0;
                    while ((strLine = br.readLine()) != null) {
                        //producer.send(new ProducerRecord<String, String>("tmac", strLine));
                        ProducerRecord<String, String> record = new ProducerRecord<String, String>("tmac", "key" + lCount++, strLine);
                        producer.send(record,
                                new Callback() {
                                    public void onCompletion(RecordMetadata metadata, Exception e) {
                                        if (e != null) {
                                            log.error("", e);
                                        }
                                        log.info("The offset of the record we just sent is: " + metadata.offset());
                                    }
                                });
                    }
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            log.error("", e);
                        }
                    }
                }
                fileTMac.renameTo(new File("/home/zhaogj/java2Kafka/data/done/" + fileTMac.getName()));
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            producer.close();
        }

    }
}
