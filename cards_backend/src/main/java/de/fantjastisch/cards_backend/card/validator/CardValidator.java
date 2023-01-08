package de.fantjastisch.cards_backend.card.validator;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.card.aggregate.UpdateCard;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.LABEL_TAKEN;


@Component
@NoArgsConstructor
public class CardValidator extends Validator {

    private CardQueryRepository cardQueryRepository;

    public void validate(CreateCard command, List<Card> cards) {
        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateConstraints(command));
        throwIfNeeded(errors);

        throwIfQuestionTaken(command.getQuestion(), cards);
    }

    public void validate(UpdateCard command, List<Card> cards) {
        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateConstraints(command));
        throwIfNeeded(errors);

        throwIfQuestionTaken(command.getQuestion(), cards);
    }


    private void throwIfQuestionTaken(String name, List<Card> cards) {
        List<ErrorEntry> errors = new ArrayList<>();
        List<Card> withSameName = cards.stream().filter(x -> x.getQuestion().equals(name)).toList();

        if (!withSameName.isEmpty()) {
            errors.add(
                    ErrorEntry.builder()
                            .code(LABEL_TAKEN)
                            .field("label")
                            .build());
        }

        throwIfNeeded(errors);

    }
}


