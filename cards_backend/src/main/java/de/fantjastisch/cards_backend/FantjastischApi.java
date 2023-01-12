package de.fantjastisch.cards_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Die Haupt-Klasse, aus der sich der Backend-Server inklusive aller API-Endpunkte starten lässt.
 *
 * @author Freja Sender
 */
@SpringBootApplication( // = @Configuration, @EnableAutoConfiguration, @ComponentScan.
        scanBasePackages = {"de.fantjastisch.cards_backend"})
public class FantjastischApi {
    public static void main(String[] args) {
        SpringApplication.run(FantjastischApi.class, args);
    }
}