package de.fantjastisch.cards_backend.category.controller;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryAggregate {

  private final CategoryCommandRepository categoryCommandRepository;

  @Autowired
  public CategoryAggregate(
      CategoryCommandRepository categoryCommandRepository) {
    this.categoryCommandRepository = categoryCommandRepository;
  }

  public String handle(final CreateCategory command) {
    return categoryCommandRepository
        .save(Category.builder().label(command.getLabel()).build());
  }
}
