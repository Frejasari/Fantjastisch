package de.fantjastisch.cards_backend.card.validator;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.card.aggregate.UpdateCard;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@NoArgsConstructor
public class CardValidator extends Validator {

    public void validate(CreateCard command, List<Card> cards) {
        throwIfNeeded(validateConstraints(command));

        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateQuestionTaken(command.getQuestion(), cards));
        throwIfNeeded(errors);
    }

    public void validate(UpdateCard command, List<Card> cards) {
        throwIfNeeded(validateConstraints(command));

        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateQuestionTaken(command.getQuestion(), cards));
        throwIfNeeded(errors);
    }

    // TODO: denke das sollte erlaubt sein - > 2 Personen koönnen ja dieselbe Frage mit individuellen Antworten machen,..
    private List<ErrorEntry> validateQuestionTaken(String name, List<Card> cards) {
        List<Card> withSameName = cards.stream().filter(card -> card.getQuestion().equals(name)).toList();

//        if (!withSameName.isEmpty()) {
//            return Collections.singletonList(
//                    ErrorEntry.builder()
//                            .code(QUESTION_DUPLICATE)
//                            .field("question")
//                            .build());
//        }
        return Collections.emptyList();
    }
}


