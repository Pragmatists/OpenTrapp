package com.pragmatists.opentrapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class OpenTrappApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenTrappApplication.class, args);
    }
}
