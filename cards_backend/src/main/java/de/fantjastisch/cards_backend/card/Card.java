package de.fantjastisch.cards_backend.card;

import lombok.Builder;
import lombok.Data;

@Builder
@Data // getter, setter, toString
public class Card {

  String id;
  String question;
  String answer;
}
