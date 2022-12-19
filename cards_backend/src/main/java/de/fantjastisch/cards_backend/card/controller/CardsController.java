package de.fantjastisch.cards_backend.card.controller;

import de.fantjastisch.cards_backend.CreatedResponse;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"Card"})
@RequestMapping("card")
public class CardsController {

  private final CardAggregate cardAggregate;
  private final CategoryQueryRepository cat;

  @Autowired
  public CardsController(CardAggregate cardAggregate,
      CategoryQueryRepository cat) {
    this.cardAggregate = cardAggregate;
    this.cat = cat;
  }

  @PostMapping(path = "create", produces = "application/json")
  @ApiOperation(
      value = "Create a new Card",
      notes = "Create a new Card",
      nickname = "createCard")
  @ApiResponses(
      value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
  public CreatedResponse createCard(
      @RequestBody CreateCard command)
      throws RuntimeException {
    return new CreatedResponse(cardAggregate.handle(command));
  }
}
