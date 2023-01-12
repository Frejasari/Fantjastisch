package de.fantjastisch.cards_backend.category;

import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

/**
 * Test Klasse für die Category Repositories
 *
 * @author Semjon Nirmann, Freja Sender
 */
@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class CategoryRepositoryTest {
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
        Category toInsert = Category.builder()
                .id(UUID.fromString("2cedc8d5-cf73-4d71-bf85-8350e20527b6"))
                .label("New label")
                .subCategories(Collections.emptyList())
                .build();

        categoryCommandRepository.create(toInsert);

        Category actual = categoryQueryRepository.get(toInsert.getId());

        Assertions.assertEquals(toInsert, actual);

        // Subcategories is an empty array
        Category toInsert2 = Category.builder()
                .id(UUID.fromString("91b6b55c-e2d9-4e23-812f-d1ba444bc80b"))
                .label("New label")
                .subCategories(Collections.emptyList())
                .build();

        categoryCommandRepository.create(toInsert2);

        Category actual2 = categoryQueryRepository.get(toInsert2.getId());

        Assertions.assertEquals(toInsert2, actual2);
    }

    @Test
    public void shouldUpdateCategoryLabel() {

        Category category = Category.builder()
                .id(UUID.fromString("40ac4fcc-9702-4a87-b0bd-bffe1f7f49f8"))
                .label("UPDATED")
                .subCategories(Arrays.asList(
                        UUID.fromString("c880b0a4-6106-4394-8e7d-521dae20b644"),
                        UUID.fromString("fce013e8-0282-4106-8ff9-b05dba5ba550")
                )).build();

        categoryCommandRepository.create(category);

        category.setLabel("UPDATED");
        categoryCommandRepository.update(category);

        Assertions.assertEquals(category, categoryQueryRepository.get(category.getId()));
    }

    @Test
    public void shouldUpdateCategorySubCategories() {


        Category subCategory1 = Category
                .builder()
                .subCategories(Collections.emptyList())
                .label("sub1")
                .id(UUID.fromString("c880b0a4-6106-4394-8e7d-521dae20b644"))
                .build();

        Category category = Category
                .builder()
                .subCategories(Collections.singletonList(subCategory1.getId()))
                .label("Praktische Informatik")
                .id(UUID.fromString("40ac4fcc-9702-4a87-b0bd-bffe1f7f49f8"))
                .build();

        Category subCategory = Category
                .builder()
                .subCategories(Collections.emptyList())
                .label("sub")
                .id(UUID.fromString("371a3e42-0645-474a-97a4-850ed2f73406"))
                .build();

        categoryCommandRepository.create(subCategory1);
        categoryCommandRepository.create(category);
        categoryCommandRepository.create(subCategory);

        ArrayList<UUID> subCategories = new ArrayList<>(category.getSubCategories());
        subCategories.add(subCategory.getId());

        category = category.toBuilder().subCategories(subCategories).build();
        categoryCommandRepository.update(category);


        Category expected = Category.builder()
                .id(category.getId())
                .label("Praktische Informatik")
                .subCategories(Arrays.asList(
                        subCategory1.getId(),
                        subCategory.getId()
                )).build();
        Assertions.assertEquals(expected, categoryQueryRepository.get(category.getId()));
    }

    @Test
    public void shouldDeleteCategory() {
        final UUID id = UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323");
        categoryCommandRepository.delete(id);
        Assertions.assertNull(categoryQueryRepository.get(id));
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
