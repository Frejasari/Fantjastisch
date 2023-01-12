package de.fantjastisch.cards_backend.category.query;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class CategoryQueryRepositoryTest {
    private CategoryQueryRepository categoryQueryRepository;
    private CategoryCommandRepository categoryCommandRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        categoryCommandRepository = new CategoryCommandRepository(namedParameterJdbcTemplate);
        categoryQueryRepository = new CategoryQueryRepository(namedParameterJdbcTemplate);
    }

    @Test
    public void shouldFindCategory() {
        Category expected = Category.builder()
                .id(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"))
                .label("Technische Informatik")
                .subCategories(Arrays.asList(
                        UUID.fromString("dbfa51dd-e8e9-4cc6-ae34-ce62e12ab2c2"),
                        UUID.fromString("fce013e8-0282-4106-8ff9-b05dba5ba550")))
                .build();
        categoryCommandRepository.create(expected);
        Category actual = categoryQueryRepository.get(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCategoryWithEmptySubCategories() {
        Category expected = Category.builder()
                .id(UUID.fromString("b1f5e79c-2f2b-4a48-bf93-5a2439f2301e"))
                .label("Mathematik")
                .subCategories(Collections.emptyList())
                .build();
        categoryCommandRepository.create(expected);
        Category actual = categoryQueryRepository.get(UUID.fromString("b1f5e79c-2f2b-4a48-bf93-5a2439f2301e"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotFindNotExistingCategory() {
        Assertions.assertNull(categoryQueryRepository.get(UUID.fromString("e44cf38d-6599-4f84-9fe1-6f24e1200052")));
    }

    @Test
    public void shouldFindAllCategories() {
        Category expected1 = Category.builder()
                .id(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"))
                .label("Technische Informatik")
                .subCategories(Arrays.asList(
                        UUID.fromString("dbfa51dd-e8e9-4cc6-ae34-ce62e12ab2c2"),
                        UUID.fromString("fce013e8-0282-4106-8ff9-b05dba5ba550")))
                .build();
        Category expected2 = Category.builder()
                .id(UUID.fromString("40ac4fcc-9702-4a87-b0bd-bffe1f7f49f8"))
                .label("Praktische Informatik")
                .subCategories(Arrays.asList(
                        UUID.fromString("c880b0a4-6106-4394-8e7d-521dae20b644"),
                        UUID.fromString("fce013e8-0282-4106-8ff9-b05dba5ba550")))
                .build();
        categoryCommandRepository.create(expected1);
        categoryCommandRepository.create(expected2);
        List<Category> expected = List.of(expected1, expected2);
        List<Category> actual = categoryQueryRepository.getList();
        Assertions.assertEquals(expected, actual);
    }
}
