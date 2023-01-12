package de.fantjastisch.cards_backend.category.command;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class CategoryCommandRepositoryTest {
    private CategoryCommandRepository categoryCommandRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private CategoryQueryRepository categoryQueryRepository;

    @BeforeEach
    public void setUp() {
        categoryCommandRepository = new CategoryCommandRepository(namedParameterJdbcTemplate);
        categoryQueryRepository = new CategoryQueryRepository(namedParameterJdbcTemplate);
    }

    @Test
    public void shouldCreateCategory() {
        UUID generatedId = UUID.fromString("2cedc8d5-cf73-4d71-bf85-8350e20527b6");
        Category toInsert = Category.builder()
                .id(generatedId)
                .label("New label")
                .subCategories(Arrays.asList(
                        UUID.fromString("45143819-012e-4b27-a2c1-67db24bf5be9"),
                        UUID.fromString("e08e5323-077a-42b7-b7e7-a3b6a4ffff18")
                ))
                .build();
        categoryCommandRepository.create(toInsert);
        Category expected = Category.builder()
                .id(generatedId)
                .label("New label")
                .subCategories(
                        Arrays.asList(
                                UUID.fromString("45143819-012e-4b27-a2c1-67db24bf5be9"),
                                UUID.fromString("e08e5323-077a-42b7-b7e7-a3b6a4ffff18")
                        ))
                .build();
        Category actual = categoryQueryRepository.get(generatedId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldSaveCategoryWithEmptySubCategories() {
        // TODO Subcategories is nulln --> sollten wir validieren & NICHT erlauben -> leeres Array!
        UUID generatedId = UUID.fromString("2cedc8d5-cf73-4d71-bf85-8350e20527b6");
        Category toInsert = Category.builder()
                .id(generatedId)
                .label("New label")
                .subCategories(Collections.emptyList())
                .build();

        categoryCommandRepository.create(toInsert);
        Category expected = Category.builder()
                .id(generatedId)
                .label("New label")
                .subCategories(Collections.emptyList())
                .build();
        Category actual = categoryQueryRepository.get(generatedId);

        Assertions.assertEquals(expected, actual);

        final UUID generatedId2 = UUID.fromString("91b6b55c-e2d9-4e23-812f-d1ba444bc80b");
        // Subcategories is an empty array
        Category toInsert2 = Category.builder()
                .id(generatedId2)
                .label("New label")
                .subCategories(Collections.emptyList())
                .build();

        categoryCommandRepository.create(toInsert2);
        Category expected2 = Category.builder()
                .id(generatedId2)
                .label("New label")
                .subCategories(Collections.emptyList())
                .build();
        Category actual2 = categoryQueryRepository.get(generatedId2);

        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    public void shouldUpdateCategoryLabel() {
        final UUID categoryId = UUID.fromString("40ac4fcc-9702-4a87-b0bd-bffe1f7f49f8");
        Category toUpdate = categoryQueryRepository.get(categoryId);
        toUpdate.setLabel("UPDATED");
        categoryCommandRepository.update(toUpdate);

        Category expected = Category.builder()
                .id(categoryId)
                .label("UPDATED")
                .subCategories(Arrays.asList(
                        UUID.fromString("c880b0a4-6106-4394-8e7d-521dae20b644"),
                        UUID.fromString("fce013e8-0282-4106-8ff9-b05dba5ba550")
                )).build();
        Assertions.assertEquals(expected, categoryQueryRepository.get(toUpdate.getId()));
    }

    @Test
    public void shouldUpdateCategorySubCategories() {
        Category toUpdate = categoryQueryRepository.get(UUID.fromString("40ac4fcc-9702-4a87-b0bd-bffe1f7f49f8"));
        ArrayList<UUID> subCategories = new ArrayList<>(toUpdate.getSubCategories());

        subCategories.add(UUID.fromString("371a3e42-0645-474a-97a4-850ed2f73406"));

        toUpdate = toUpdate.toBuilder().subCategories(subCategories).build();
        categoryCommandRepository.update(toUpdate);

        Category expected = Category.builder()
                .id(UUID.fromString("40ac4fcc-9702-4a87-b0bd-bffe1f7f49f8"))
                .label("Praktische Informatik")
                .subCategories(Arrays.asList(
                        UUID.fromString("c880b0a4-6106-4394-8e7d-521dae20b644"),
                        UUID.fromString("fce013e8-0282-4106-8ff9-b05dba5ba550"),
                        UUID.fromString("371a3e42-0645-474a-97a4-850ed2f73406")
                )).build();
        Assertions.assertEquals(expected, categoryQueryRepository.get(toUpdate.getId()));
    }

    @Test
    public void shouldDeleteCategory() {
        final UUID id = UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323");
        categoryCommandRepository.delete(id);
        Assertions.assertNull(categoryQueryRepository.get(id));
    }
}
