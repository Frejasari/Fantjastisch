package de.fantjastisch.cards_backend.card.query;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class CardQueryRepositoryTest {

    private CardQueryRepository cardQueryRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        cardQueryRepository = new CardQueryRepository(namedParameterJdbcTemplate);
    }


    @Test
    public void findOneCard() {
        Category cat = Category.builder()
                .id(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"))
                .label("Technische Informatik")
                .build();

        final UUID expectedId = UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e");

        Card expected = Card.builder()
                .id(expectedId)
                .question("Welche Verdrängungsstrategien gibt es?")
                .answer("FIFO, LRU, LFU")
                .tag("Wichtig")
                .categories(Collections.singletonList(cat.getId()))
                .build();

        Card actual = cardQueryRepository.get(expectedId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAllCards() {
        Category cat = Category.builder()
                .id(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"))
                .label("Technische Informatik")
                .build();

        Card expected1 = Card.builder()
                .id(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .question("Welche Verdrängungsstrategien gibt es?")
                .answer("FIFO, LRU, LFU")
                .tag("Wichtig")
                .categories(Collections.singletonList(cat.getId()))
                .build();
        Card expected2 = Card.builder()
                .id(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .question("Nenne zwei Cachearten.")
                .answer("Direct-Mapped und Voll-assoziativ")
                .tag("")
                .categories(Collections.singletonList(cat.getId()))
                .build();

        List<Card> expected = List.of(expected1, expected2);
        List<Card> actual = cardQueryRepository.getPage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findNonExistentCard() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            cardQueryRepository.get(UUID.randomUUID());
        });
    }

    @Test
    public void findCardWithNoCategory() {
        Card.builder()
                .id(UUID.fromString("17da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .question("Welche Cachearten gibt es?")
                .answer("Direct-Mapped und Voll-assoziativ")
                .categories(Collections.emptyList())
                .tag("")
                .build();
        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
                cardQueryRepository.get(UUID.fromString("17da14f8-c6e1-48e4-9071-ed92f5694aff")));
    }

    @Test
    public void findCardWithNoQuestion() {
        Card.builder()
                .id(UUID.fromString("17da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .question("")
                .answer("Direct-Mapped und Voll-assoziativ")
                .tag("")
                .build();
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            cardQueryRepository.get(UUID.fromString("17da14f8-c6e1-48e4-9071-ed92f5694aff"));
        });
    }

    @Test
    public void findCardWithNoAnswer() {
        Card.builder()
                .id(UUID.fromString("27da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .question("Nenne zwei Cachearten.")
                .answer("")
                .tag("")
                .build();
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            cardQueryRepository.get(UUID.fromString("27da14f8-c6e1-48e4-9071-ed92f5694aff"));
        });
    }
}