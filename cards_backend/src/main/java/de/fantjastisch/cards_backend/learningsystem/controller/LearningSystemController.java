package de.fantjastisch.cards_backend.learningsystem.controller;

import de.fantjastisch.cards_backend.util.CreatedResponse;
import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.CreateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.DeleteLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.LearningSystemAggregate;
import de.fantjastisch.cards_backend.learningsystem.aggregate.UpdateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Api(tags = {"LearningSystem"})
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
    @ApiOperation(
            value = "Create a new learning system",
            notes = "Create a new learning system",
            nickname = "createLearningSystem")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public CreatedResponse createLearningSystem(
            @RequestBody CreateLearningSystem command)
            throws RuntimeException {
        return new CreatedResponse(learningSystemAggregate.handle(command));
    }

    @PutMapping(path = "update")
    @ApiOperation(
            value = "Update a learning system",
            notes = "Update a learning system",
            nickname = "updateLearningSystem")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void updateLearningSystem(@RequestBody UpdateLearningSystem command) {
        learningSystemAggregate.handle(command);
    }

    @DeleteMapping(path = "delete")
    @ApiOperation(
            value = "Delete a learning system",
            notes = "Delete a learning system",
            nickname = "deleteLearningSystem")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void deleteLearningSystem(@RequestBody DeleteLearningSystem command) {
        learningSystemAggregate.handle(command);
    }

    @GetMapping(path = "get", produces = "application/json")
    @ApiOperation(
            value = "Get specific learning system",
            notes = "Get specific learning system",
            nickname = "get")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public LearningSystem get(@RequestParam UUID id) {
        return learningSystemAggregate.handle(id);
    }

    @GetMapping(path = "getList", produces = "application/json")
    @ApiOperation(
            value = "Get all learning systems",
            notes = "Get all learning systems",
            nickname = "getList")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public List<LearningSystem> getList() {
        return learningSystemAggregate.handle();
    }
}
