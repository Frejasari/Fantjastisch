package de.fantjastisch.cards_backend.link.validator;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.aggregate.CreateLink;
import de.fantjastisch.cards_backend.link.aggregate.UpdateLink;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import lombok.NoArgsConstructor;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import de.fantjastisch.cards_backend.util.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.LABEL_TAKEN_VIOLATION;
import static de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry.mapErrorsToString;


@Component
@NoArgsConstructor
public class LinkValidator extends Validator{

    private CardQueryRepository cardQueryRepository;

    public void validate(CreateLink command, List<Link> links) {
        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateConstraints(command));
        throwIfNeeded(errors);

        throwIfNameTaken(command.getName(), links);
        throwIfNoNameOnCard(command.getName(), command.getSource());
    }

    public void validate(UpdateLink command, List<Link> link) {
        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateConstraints(command));
        throwIfNeeded(errors);

        throwIfNameTaken(command.getName(), link);
        throwIfNoNameOnCard(command.getName(), command.getSource());
    }


    private void throwIfNameTaken(String name, List<Link> allLinks) {
        List<ErrorEntry> errors = new ArrayList<>();
        List<Link> withSameName = allLinks.stream().filter(link -> link.getName().equals(name)).toList();

        if (!withSameName.isEmpty()) {
            errors.add(
                    ErrorEntry.builder()
                            .code(LABEL_TAKEN_VIOLATION)
                            .field("label")
                            .build());
        }

        throwIfNeeded(errors);

    }


    private void throwIfNoNameOnCard(String name, UUID cardID) {
        List<ErrorEntry> errors = new ArrayList<>();
        String question = cardQueryRepository.get(cardID).getQuestion();
        String answer = cardQueryRepository.get(cardID).getAnswer();

        if (!question.contains(name) || !answer.contains(name)){
            errors.add(
                    ErrorEntry.builder()
                            .code(LABEL_TAKEN_VIOLATION)
                            .field("name")
                            .build());
        }

        throwIfNeeded(errors);
    }

    private void throwIfCardsDoNotExist(UUID source, UUID target) {
        List<ErrorEntry> errors = new ArrayList<>();
        try {
            Card from = cardQueryRepository.get(source);
            Card to = cardQueryRepository.get(target);
        } catch (CommandValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(e.getErrors()));
        }

    }




}


