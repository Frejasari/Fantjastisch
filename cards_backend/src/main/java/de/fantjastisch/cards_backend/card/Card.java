package de.fantjastisch.cards_backend.card;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data // getter, setter, toString
public class Card {
    UUID id;
    String question;
    String answer;
    String tag;
    List<UUID> categories;
}
