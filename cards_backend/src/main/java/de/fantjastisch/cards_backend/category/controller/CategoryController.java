package de.fantjastisch.cards_backend.category.controller;

import de.fantjastisch.cards_backend.CreatedResponse;
import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"Category"})
@RequestMapping("category")
public class CategoryController {

  private final CategoryAggregate categoryAggregate;
  private final CategoryQueryRepository categoryQueryRepository;

  @Autowired
  public CategoryController(CategoryAggregate categoryAggregate,
      CategoryQueryRepository categoryQueryRepository) {
    this.categoryAggregate = categoryAggregate;
    this.categoryQueryRepository = categoryQueryRepository;
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
    return new CreatedResponse(categoryAggregate.handle(command));
  }

  @GetMapping(path = "getList", produces = "application/json")
  @ApiOperation(
      value = "Get all categories",
      notes = "Get all categories",
      nickname = "getList")
  @ApiResponses(
      value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
  public List<Category> getList() {
    return categoryQueryRepository.getList();
  }
}
