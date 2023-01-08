package de.fantjastisch.cards_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication( // = @Configuration, @EnableAutoConfiguration, @ComponentScan.
        scanBasePackages = {"de.fantjastisch.cards_backend"})
public class FantjastischApi {
    public static void main(String[] args) {
        SpringApplication.run(FantjastischApi.class, args);
    }
}