package de.fantjastisch.cards_backend.card.aggregate;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.repository.CardCommandRepository;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.card.validator.CardValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

// @Component -> erzeugt ein Bean von der Klasse -> s. CardController
@Component
public class CardAggregate {

    private final CardCommandRepository cardCommandRepository;
    private final CardQueryRepository cardQueryRepository;
    private final CardValidator cardValidator;
    private final UUIDGenerator uuidGenerator;

    @Autowired
    public CardAggregate(
            CardCommandRepository cardCommandRepository, CardQueryRepository cardQueryRepository,
            CardValidator cardValidator, UUIDGenerator uuidGenerator) {
        this.cardCommandRepository = cardCommandRepository;
        this.cardQueryRepository = cardQueryRepository;
        this.cardValidator = cardValidator;
        this.uuidGenerator = uuidGenerator;
    }


    public UUID handle(final CreateCard command) {
        List<Card> allCards = cardQueryRepository.getPage();
        cardValidator.validate(command, allCards);

        Card newCard = Card.builder()
                .id(uuidGenerator.randomUUID())
                .question(command.getQuestion())
                .answer(command.getAnswer())
                .tag(command.getTag())
                .categories(command.getCategories())
                .build();
        cardCommandRepository.create(newCard);
        return newCard.getId();
    }

    public Card handle(final UUID id) {
        return throwOrGet(id);
    }

    public List<Card> handle() {
        return cardQueryRepository.getPage();
    }

    public void handle(final DeleteCard command) {
        Card card = throwOrGet(command.getId());
        cardCommandRepository.delete(card);
    }

    public void handle(final UpdateCard command) {
        List<Card> allCategories = cardQueryRepository.getPage();
        cardValidator.validate(command, allCategories);
        Card category = throwOrGet(command.getId());

        cardCommandRepository.update(category);
    }

    private Card throwOrGet(UUID id) {
        Card card = cardQueryRepository.get(id);
        if (card == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
        return card;
    }

}
