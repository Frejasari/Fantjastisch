package de.fantjastisch.cards_backend.util;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Ein UUID-Generator für verschiedene CRUD-Kommando-Objekte welcher die statische randomUUID() - Methode der {@link UUID}-Klasse
 * in eine die Methode randomUUID() kapselt.
 *
 * Aus dem UUID-Generator wird eine Bean erzeugt, welche mit Hilfe von Spring im Rahmen der Dependency-Injection verwendet
 * werden kann. Das erleichtert das Testen mit dem Mockito-Framework.
 *
 * @author Freja Sender
 */
@Component
@NoArgsConstructor
public class UUIDGenerator {
    public UUID randomUUID() {
        return UUID.randomUUID();
    }
}
