package de.fantjastisch.cards_backend.card.repository;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt das Modell einer Karteikarte dar, zur internen Kommunikation mit der Datenbank.
 *
 * @Author Tamari Bayer, Freja Sender
 */
@Builder
@Data // getter, setter, toString
public class Card {
    UUID id;
    String question;
    String answer;
    String tag;
    List<UUID> categories;
}
