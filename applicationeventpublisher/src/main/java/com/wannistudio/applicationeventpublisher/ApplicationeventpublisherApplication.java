package com.wannistudio.applicationeventpublisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApplicationeventpublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationeventpublisherApplication.class, args);
    }

}
