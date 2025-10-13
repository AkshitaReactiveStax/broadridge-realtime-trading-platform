package com.broadridge.order_service.service;


import com.broadridge.order_service.model.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TradeProducerService {

    @Autowired
    private final KafkaTemplate<String , Object> kafkaTemplate;

    @Value("${topic.orders}")
    private String ordersTopic ;

    public TradeProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void publishTrade(Trade trade){
        kafkaTemplate.send(ordersTopic , trade.getTradeId() , trade);
    }


}
