package de.fantjastisch.cards_backend.card.repository;

import de.fantjastisch.cards_backend.card.Link;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse stellt das Modell einer Karteikarte dar, zur internen Kommunikation mit der Datenbank.
 *
 * @author Tamari Bayer, Freja Sender
 */
@Builder
@Data // getter, setter, toString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {
    UUID id;
    String question;
    String answer;
    String tag;
    // use Set to avoid duplicates
    Set<UUID> categories;
    // use Set to avoid duplicates
    private Set<Link> links;


}
