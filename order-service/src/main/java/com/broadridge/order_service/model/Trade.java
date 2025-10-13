package com.broadridge.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {
    private String tradeId;
    private String customerId;
    private String symbol;
    private String side;      // BUY or SELL
    private double quantity;
    private double price;
    private long timestamp;



    public String getTradeId() {
        return tradeId;
    }

}