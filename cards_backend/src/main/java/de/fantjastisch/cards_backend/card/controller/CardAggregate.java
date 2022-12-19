package de.fantjastisch.cards_backend.card.controller;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.repository.CardCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardAggregate {

  private final CardCommandRepository cardCommandRepository;

  @Autowired
  public CardAggregate(
      CardCommandRepository cardCommandRepository) {
    this.cardCommandRepository = cardCommandRepository;
  }

  public String handle(final CreateCard command) {
    return cardCommandRepository
        .save(Card.builder().question(command.getQuestion()).answer(command.getAnswer()).build());
  }
}
