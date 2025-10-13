//package com.broadridge.gateway_service;
//
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GlobalFilterConfig {
//    @Bean
//    public GlobalFilter customGlobalFilter() {
//        return (exchange, chain) -> {
//            System.out.println("Request received at: " + exchange.getRequest().getURI());
//            return chain.filter(exchange);
//        };
//    }
//}
