package de.fantjastisch.cards_backend.card.controller;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.aggregate.CardAggregate;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.card.aggregate.DeleteCard;
import de.fantjastisch.cards_backend.card.aggregate.UpdateCard;
import de.fantjastisch.cards_backend.util.CreatedResponse;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry.mapErrorsToString;

@RestController
@Api(tags = {"Card"})
@RequestMapping("card")
public class CardsController {

    private final CardAggregate cardAggregate;

    // dependency injection -> die parameter werden von dem System eingesetzt -> SpringBeans
    @Autowired
    public CardsController(CardAggregate cardAggregate) {
        this.cardAggregate = cardAggregate;
    }

    // PostMapping -> post something to API -> in API POST
    // GetMapping -> request from API -> in API GET
    // Put -> replace entity
    // Patch -> Partielle Updates
    // Delete
    @PostMapping(path = "create", produces = "application/json")
    // @ApiOperation -> io.swagger generiert ein Client
    @ApiOperation(
            value = "Create a new Card",
            notes = "Create a new Card",
            nickname = "createCard")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})

    public CreatedResponse createCard(
            @RequestBody CreateCard command)
            throws RuntimeException {
        try {
            return new CreatedResponse(cardAggregate.handle(command));
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    @PutMapping(path = "update")
    @ApiOperation(
            value = "Update a card",
            notes = "Update a card",
            nickname = "updateCard")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void updateCategory(@RequestBody UpdateCard command) {
        try {
            cardAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    @DeleteMapping(path = "delete")
    @ApiOperation(
            value = "Delete a card",
            notes = "Delete a card",
            nickname = "delete")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void delete(@RequestBody DeleteCard command) {
        try {
            cardAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    @GetMapping(path = "get", produces = "application/json")
    @ApiOperation(
            value = "Get the Card from the given Id",
            notes = "Get the Card from the given Id",
            nickname = "getCard")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public Card get(@RequestParam UUID id)
            throws RuntimeException {
        try {
            return cardAggregate.handle(id);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    //TODO : Filter + Sort!
    @GetMapping(path = "getList", produces = "application/json")
    @ApiOperation(
            value = "Get all cards",
            notes = "Get all cards",
            nickname = "getList")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public List<Card> getPage() {
        return cardAggregate.handle();
    }

}