package de.fantjastisch.cards_backend.category.controller;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.aggregate.CategoryAggregate;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
import de.fantjastisch.cards_backend.category.aggregate.DeleteCategory;
import de.fantjastisch.cards_backend.category.aggregate.UpdateCategory;
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
@Api(tags = {"Category"})
@RequestMapping("category")
public class CategoryController {

    private final CategoryAggregate categoryAggregate;

    @Autowired
    public CategoryController(CategoryAggregate categoryAggregate) {
        this.categoryAggregate = categoryAggregate;
    }

    @PostMapping(path = "create", produces = "application/json")
    @ApiOperation(
            value = "Create a new category",
            notes = "Create a new category",
            nickname = "createCategory")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public CreatedResponse createCategory(
            @RequestBody CreateCategory command)
            throws RuntimeException {
        try {
            return new CreatedResponse(categoryAggregate.handle(command));
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    @PutMapping(path = "update")
    @ApiOperation(
            value = "Update a category",
            notes = "Update a category",
            nickname = "updateCategory")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void updateCategory(@RequestBody UpdateCategory command) {
        try {
            categoryAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    @DeleteMapping(path = "delete")
    @ApiOperation(
            value = "Delete a category",
            notes = "Delete a category",
            nickname = "deleteCategory")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public void deleteCategory(@RequestBody DeleteCategory command) {
        try {
            categoryAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    @GetMapping(path = "get", produces = "application/json")
    @ApiOperation(
            value = "Get specific category",
            notes = "Get specific category",
            nickname = "get")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public Category get(@RequestParam UUID id) {
        try {
            return categoryAggregate.handle(id);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    @GetMapping(path = "getList", produces = "application/json")
    @ApiOperation(
            value = "Get all categories",
            notes = "Get all categories",
            nickname = "getList")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public List<Category> getList() {
        return categoryAggregate.handle();
    }


}
