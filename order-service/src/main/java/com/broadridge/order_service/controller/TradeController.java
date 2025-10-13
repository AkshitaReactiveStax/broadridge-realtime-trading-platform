package com.broadridge.order_service.controller;

import com.broadridge.order_service.model.Trade;
import com.broadridge.order_service.service.TradeProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class TradeController {

    private final TradeProducerService tradeProducerService;

    public TradeController(TradeProducerService tradeProducerService) {
        this.tradeProducerService = tradeProducerService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitTrade(@RequestBody Trade trade) {
        System.out.println("API working");
//       tradeProducerService.publishTrade(trade);
        return ResponseEntity.ok("Trade submitted successfully: " + trade.getTradeId());
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Order Service is up");
    }
}