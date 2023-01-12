package de.fantjastisch.cards_backend.learningsystem.validator;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
import de.fantjastisch.cards_backend.category.aggregate.DeleteCategory;
import de.fantjastisch.cards_backend.category.aggregate.UpdateCategory;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.CreateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.DeleteLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.LearningSystemAggregate;
import de.fantjastisch.cards_backend.learningsystem.aggregate.UpdateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class LearningSystemValidator {

    private final LearningSystemQueryRepository learningSystemQueryRepository;

    @Autowired
    public LearningSystemValidator(LearningSystemQueryRepository learningSystemQueryRepository) {
        this.learningSystemQueryRepository = learningSystemQueryRepository;
    }
    public LearningSystem validate(CreateLearningSystem command) {
        if (command.getLabel().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "Learning system label must not be empty"
            );
        }
        return LearningSystem.builder()
                .label(command.getLabel())
                .boxLabels(command.getBoxLabels())
                .build();
    }

    public LearningSystem validate(UpdateLearningSystem command) {
        LearningSystem learningSystem = learningSystemQueryRepository.get(command.getId());
        if (learningSystem == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
        return learningSystem;
    }

    public LearningSystem validate(UUID id) {
        LearningSystem learningSystem = learningSystemQueryRepository.get(id);
        if (learningSystem == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
        return learningSystem;
    }
}
