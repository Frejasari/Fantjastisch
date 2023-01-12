package de.fantjastisch.cards_backend.card;


import de.fantjastisch.cards_backend.card.repository.CardCommandRepository;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.card.validator.CardValidator;
import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class CardRepositoryTests {

    private CardQueryRepository cardQueryRepository;
    private CardCommandRepository cardCommandRepository;
    private CategoryCommandRepository categoryCommandRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String cat1Id = "8f0b6267-d2b7-43ea-827d-aed9a538a93d";

    private final Category cat0 = Category.builder()
            .id(UUID.fromString("eee73e4a-8b9d-4e4f-b77e-ef5703b4f06a"))
            .label("Technische Informatik 1")
            .subCategories(Collections.emptyList())
            .build();

    private final Category cat1 = Category.builder()
            .id(UUID.fromString(cat1Id))
            .label("Technische Informatik 2")
            .subCategories(Collections.emptyList())
            .build();

    private final Card card1 = Card.builder()
            .id(UUID.fromString("a55e9fdc-5a51-4cfb-9a99-19c653ace0ea"))
            .question("Welche Cachearten existieren?")
            .answer("Vollassoziativ, Direct mapped")
            .tag("unwichtig")
            .categories(Arrays.asList(cat1.getId(), cat0.getId()))
            .build();

    private final Card card2 = Card.builder()
            .id(UUID.fromString("6bdbf018-95e2-4a1c-ab9d-8f6df40ded44"))
            .question("Welche Davio-Zerlegungtypen existieren?")
            .answer("positiv und negativ Davio")
            .tag("sehr wichtig")
            .categories(Collections.singletonList(cat0.getId()))
            .build();

    private final Card card3 = Card.builder()
            .id(UUID.fromString("a34dda23-1028-4f58-9e4c-b04676ea0fbf"))
            .question("Welche Wahrheitswerte gibt es?")
            .answer("True und False")
            .tag("wichtig")
            .categories(Collections.singletonList(cat1.getId()))
            .build();

    @BeforeEach
    public void setUp() {
        cardQueryRepository = new CardQueryRepository(namedParameterJdbcTemplate);
        cardCommandRepository = new CardCommandRepository(namedParameterJdbcTemplate);
        categoryCommandRepository = new CategoryCommandRepository(namedParameterJdbcTemplate);
        CategoryQueryRepository categoryQueryRepository = new CategoryQueryRepository(namedParameterJdbcTemplate);
        CardValidator cardValidator = new CardValidator(cardQueryRepository, categoryQueryRepository);
        UUIDGenerator uuidGenerator = new UUIDGenerator();


        categoryCommandRepository.create(cat0);
        categoryCommandRepository.create(cat1);

        cardCommandRepository.create(card1);
        cardCommandRepository.create(card2);
        cardCommandRepository.create(card3);
    }

    //cardCommandTest
    @Test
    public void createCard() {
        Card actual = cardQueryRepository.get(card1.getId());
        Assertions.assertEquals(card1, actual);
    }

    @Test
    public void deleteCard() {
        cardCommandRepository.delete(card1.getId());
        Assertions.assertNull(cardQueryRepository.get(card1.getId()));
    }

    @Test
    public void updateCard() {
        Assertions.assertEquals(card1, cardQueryRepository.get(card1.getId()));

        Card updated = Card.builder()
                .id(card1.getId())
                .answer("Rudolf Bayer")
                .question("?")
                .categories(Collections.emptyList())
                .tag("wichtig")
                .build();
        cardCommandRepository.update(updated);
        Assertions.assertEquals(updated, cardQueryRepository.get(card1.getId()));
    }

    @Test
    public void updateNonExistentCard() {
        Assertions.assertNull(cardQueryRepository.get(UUID.randomUUID()));
    }

    //cardQueryTest
    @Test
    public void findOneCard() {
        Category cat = Category.builder()
                .id(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"))
                .label("Technische Informatik")
                .subCategories(Collections.emptyList())
                .build();

        Card expected = Card.builder()
                .id(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .question("Welche Verdrängungsstrategien gibt es?")
                .answer("FIFO, LRU, LFU")
                .tag("Wichtig")
                .categories(Collections.singletonList(cat.getId()))
                .build();

        categoryCommandRepository.create(cat);
        cardCommandRepository.create(expected);

        Card actual = cardQueryRepository.get(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void findAllCards() {
        List<Card> expected = List.of(card2, card3, card1);
        List<Card> actual = cardQueryRepository.getPage(null, null, null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void findNonExistentCard() {
        Assertions.assertNull(cardQueryRepository.get(UUID.randomUUID()));
    }

    @Test
    public void findOnlyCardsWithTag() {
        List<Card> actual = cardQueryRepository.getPage(Collections.emptyList(), null, "sehr wichtig", false);
        List<Card> expected = Arrays.asList(card2);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotFindCardWithNonExistentTag() {
        assertEquals(Collections.emptyList(),
                cardQueryRepository.getPage(Collections.emptyList(), null, "bye", false));
    }

    @Test
    public void shouldNotFindCardWithNonExistentSubstring() {
        assertEquals(Collections.emptyList(),
                cardQueryRepository.getPage(Collections.emptyList(), "bye", null, false));
    }

    @Test
    public void sortTagsAlphabetically() {
        List<Card> actual = cardQueryRepository.getPage(Collections.emptyList(), null, null, true);

        assertEquals(Arrays.asList(card2, card1, card3), actual);
    }

    @Test
    public void findOnlyCardsWithString() {
        List<Card> actual = cardQueryRepository.getPage(Collections.emptyList(), "existieren", null, false);
        List<Card> expected = Arrays.asList(card2, card1);

        assertEquals(expected, actual);
    }

    @Test
    public void filterCardsWithCategory() {
        List<UUID> categoryFilter = List.of(cat0.getId());
        List<Card> actual = cardQueryRepository.getPage(categoryFilter, null, null, false);
        List<Card> expected = Arrays.asList(card2, card1);

        assertEquals(expected, actual);
    }

    @Test
    public void filterCardsWithCategories() {
        Card card4 = Card.builder()
                .id(UUID.fromString("bc63f480-8a44-4f31-b964-30b44934f473"))
                .question("Finde mich nicht.")
                .answer("Ok.")
                .tag("wichtig")
                .categories(Collections.emptyList())
                .build();
        cardCommandRepository.create(card4);

        List<UUID> categoryFilter = List.of(cat0.getId(), cat1.getId());
        List<Card> actual = cardQueryRepository.getPage(categoryFilter, null, null, false);
        List<Card> expected = Arrays.asList(card2, card3, card1);

        assertEquals(expected, actual);
    }

    @Test
    public void filterCardsAndSort() {
        List<UUID> categoryFilter = List.of(cat0.getId());
        List<Card> actual = cardQueryRepository.getPage(categoryFilter, null, null, true);

        List<Card> expected = Arrays.asList(card2, card1);

        assertEquals(expected, actual);
    }

    @Test
    public void filterCardsAndFindTag() {
        List<UUID> categoryFilter = List.of(cat0.getId());
        List<Card> actual = cardQueryRepository.getPage(categoryFilter, null, "unwichtig", true);

        List<Card> expected = List.of(card1);

        assertEquals(expected, actual);
    }

    @Test
    public void filterCardsAndFindString() {
        List<UUID> categoryFilter = List.of(cat0.getId());
        List<Card> actual = cardQueryRepository.getPage(categoryFilter, "Davio", null, false);

        List<Card> expected = List.of(card2);

        assertEquals(expected, actual);
    }

    @Test
    public void filterSearchTagSort() {
        Card card4 = Card.builder()
                .id(UUID.fromString("c801de3c-736e-423f-a24d-5917eba854a0"))
                .question("Was ist negativ Davio?")
                .answer("Ein Zerlegungstyp")
                .tag("sehr wichtig")
                .categories(Collections.singletonList(cat0.getId()))
                .build();
        cardCommandRepository.create(card4);

        List<UUID> categoryFilter = List.of(cat0.getId());
        List<Card> actual = cardQueryRepository.getPage(categoryFilter, "Davio", "sehr wichtig", true);

        List<Card> expected = Arrays.asList(card2, card4);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void filterSearchSort() {
        List<UUID> categoryFilter = List.of(cat0.getId());

        List<Card> actual = Arrays.asList(card2, card1);
        List<Card> expected = cardQueryRepository.getPage(categoryFilter, "existieren", null, true);

        Assertions.assertEquals(expected, actual);
    }
}
