package com.example.pfm.budgetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class BudgetServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgetServiceApplication.class, args);
    }
    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
