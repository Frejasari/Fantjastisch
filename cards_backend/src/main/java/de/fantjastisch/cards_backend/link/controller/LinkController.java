package de.fantjastisch.cards_backend.link.controller;

import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.aggregate.CreateLink;
import de.fantjastisch.cards_backend.link.aggregate.DeleteLink;
import de.fantjastisch.cards_backend.link.aggregate.LinkAggregate;
import de.fantjastisch.cards_backend.link.aggregate.UpdateLink;
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
@Api(tags = {"Link"})
@RequestMapping("link")
public class LinkController  {

    private final LinkAggregate linkAggregate;
    private final CardQueryRepository cardQueryRepository;

    @Autowired
    public LinkController(LinkAggregate linkAggregate, CardQueryRepository cardQueryRepository) {
        this.linkAggregate = linkAggregate;
        this.cardQueryRepository = cardQueryRepository;
    }

    @PostMapping(path = "create", produces = "application/json")
    // @ApiOperation -> io.swagger generiert ein Client
    @ApiOperation(
            value = "Create a new Link",
            notes = "Create a new Link",
            nickname = "createLink")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void create(@RequestBody CreateLink command) throws RuntimeException {
        try {
            cardQueryRepository.get(command.getSource());
            cardQueryRepository.get(command.getTarget());
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }

    }

    @GetMapping(path = "get", produces = "application/json")
    @ApiOperation(
            value = "Get the Link from the given name and source",
            notes = "Get the Link from the given name and source",
            nickname = "getLink")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public Link get(@RequestParam String name, UUID from)
            throws RuntimeException {
        try {
            return linkAggregate.handle(name, from);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }


    @GetMapping(path = "getPage", produces = "application/json")
    @ApiOperation(
            value = "Get all links",
            notes = "Get all links",
            nickname = "getList")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public List<Link> getPage(@RequestParam UUID from) {
        return linkAggregate.handle(from);
    }

    @DeleteMapping(path = "delete")
    @ApiOperation(
            value = "Delete a link",
            notes = "Delete a link",
            nickname = "delete")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void delete(@RequestBody DeleteLink command) {
        try {
            linkAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    // beim updaten auch gucken, ob target und source überhaupt vorhanden sind
    @PutMapping(path = "update")
    @ApiOperation(
            value = "Update a link",
            notes = "Update a link",
            nickname = "update")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void update(@RequestBody UpdateLink command) {
        try {
            linkAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }
}