package com.transactionprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TransactionApplication {

        public static void main(String[] args) {
                SpringApplication.run(TransactionApplication.class, args);
        }


}
