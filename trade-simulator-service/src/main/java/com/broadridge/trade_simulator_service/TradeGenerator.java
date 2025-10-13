package com.broadridge.trade_simulator_service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

public class TradeGenerator {

    private static final String TARGET_URL = "http://localhost:8080/api/orders";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random();

    public static void main(String[] args) throws Exception {
        while (true) {
            String tradeId = UUID.randomUUID().toString();
            String[] symbols = {"AAPL", "GOOGL", "AMZN", "MSFT", "TSLA"};
            String[] sides = {"BUY", "SELL"};

            Trade trade = new Trade(
                    tradeId,
                    "CUST-" + (1000 + random.nextInt(9000)),
                    symbols[random.nextInt(symbols.length)],
                    sides[random.nextInt(sides.length)],
                    50 + random.nextInt(450),
                    Math.round((100 + random.nextDouble() * 500) * 100.0) / 100.0,
                    System.currentTimeMillis()
            );

            sendTrade(trade);
            Thread.sleep(2000); // every 2 seconds
        }
    }

    private static void sendTrade(Trade trade) {
        try {
            URL url = new URL(TARGET_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = mapper.writeValueAsString(trade);
            conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));

            int responseCode = conn.getResponseCode();
            System.out.println("Sent Trade " + trade.tradeId + " => Response: " + responseCode);

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simple POJO for trades
    static class Trade {
        public String tradeId;
        public String customerId;
        public String symbol;
        public String side;
        public double quantity;
        public double price;
        public long timestamp;

        public Trade(String tradeId, String customerId, String symbol,
                     String side, double quantity, double price, long timestamp) {
            this.tradeId = tradeId;
            this.customerId = customerId;
            this.symbol = symbol;
            this.side = side;
            this.quantity = quantity;
            this.price = price;
            this.timestamp = timestamp;
        }
    }
}