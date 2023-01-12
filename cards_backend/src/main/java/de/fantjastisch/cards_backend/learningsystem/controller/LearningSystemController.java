package de.fantjastisch.cards_backend.learningsystem.controller;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.CreateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.DeleteLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.LearningSystemAggregate;
import de.fantjastisch.cards_backend.learningsystem.aggregate.UpdateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import de.fantjastisch.cards_backend.util.CreatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "LearningSystem")
@RequestMapping("learningSystem")
public class LearningSystemController {

    private final LearningSystemAggregate learningSystemAggregate;
    private final LearningSystemQueryRepository learningSystemQueryRepository;

    @Autowired
    public LearningSystemController(LearningSystemAggregate learningSystemAggregate,
                                    LearningSystemQueryRepository learningSystemQueryRepository) {
        this.learningSystemAggregate = learningSystemAggregate;
        this.learningSystemQueryRepository = learningSystemQueryRepository;
    }

    @PostMapping(path = "create", produces = "application/json")
    @Operation(
            summary = "Create a new learning system",
            operationId = "createLearningSystem")
    public CreatedResponse createLearningSystem(
            @RequestBody CreateLearningSystem command)
            throws RuntimeException {
        return new CreatedResponse(learningSystemAggregate.handle(command));
    }

    @PutMapping(path = "update")
    @Operation(
            summary = "Update a learning system",
            operationId = "updateLearningSystem")
    public void updateLearningSystem(@RequestBody UpdateLearningSystem command) {
        learningSystemAggregate.handle(command);
    }

    @DeleteMapping(path = "delete")
    @Operation(
            summary = "Delete a learning system",
            operationId = "deleteLearningSystem")
    public void deleteLearningSystem(@RequestBody DeleteLearningSystem command) {
        learningSystemAggregate.handle(command);
    }

    @GetMapping(path = "get", produces = "application/json")
    @Operation(
            summary = "Get specific learning system",
            operationId = "getLearningSystem")
    public LearningSystem get(@RequestParam UUID id) {
        return learningSystemAggregate.handle(id);
    }

    @GetMapping(path = "getPage", produces = "application/json")
    @Operation(
            summary = "Get all learning systems",
            operationId = "getLearningSystemList")
    public List<LearningSystem> getPage() {
        return learningSystemAggregate.handle();
    }
}
