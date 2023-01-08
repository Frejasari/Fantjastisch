package de.fantjastisch.cards_backend.learningsystem.aggregate;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemCommandRepository;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import de.fantjastisch.cards_backend.learningsystem.validator.LearningSystemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class LearningSystemAggregate {
    private final LearningSystemCommandRepository learningSystemCommandRepository;
    private final LearningSystemValidator learningSystemValidator;
    private LearningSystemQueryRepository learningSystemQueryRepository;

    @Autowired
    public LearningSystemAggregate(
            LearningSystemCommandRepository learningSystemCommandRepository,
            LearningSystemValidator learningSystemValidator,
            LearningSystemQueryRepository learningSystemQueryRepository) {
        this.learningSystemCommandRepository = learningSystemCommandRepository;
        this.learningSystemValidator = learningSystemValidator;
        this.learningSystemQueryRepository = learningSystemQueryRepository;
    }

    public UUID handle(final CreateLearningSystem command) {
        return learningSystemCommandRepository.save(learningSystemValidator.validate(command));
    }

    public void handle(final UpdateLearningSystem command) {
        learningSystemCommandRepository.update(learningSystemValidator.validate(command));
    }

    public void handle(final DeleteLearningSystem command) {
        learningSystemCommandRepository.delete(learningSystemValidator.validate(command.getId()));
    }


    public LearningSystem handle(UUID id) {
        return learningSystemValidator.validate(id);
    }

    public List<LearningSystem> handle() {
        return learningSystemQueryRepository.getList();
    }
}
