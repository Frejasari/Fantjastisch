package de.fantjastisch.cards_backend.card.repository;

import de.fantjastisch.cards_backend.card.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class CardQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<Card> CARD_ROW_MAPPER = (rs, rowNum) -> {
        List<UUID> resultCategoriesArr = parseSQLArrayToUUIDArray(rs.getString("categories"));
        return Card.builder()
                .id(UUID.fromString(rs.getString("id")))
                .answer(rs.getString("answer"))
                .question(rs.getString("question"))
                .tag(rs.getString("tag"))
                .categories(resultCategoriesArr).build();
    };

    private List<UUID> parseSQLArrayToUUIDArray(String arr) {
        if (arr == null) {
            return null;
        } else {
            String[] res = arr.replaceAll("\\[|\\]", "").split(", ");
            return Arrays.stream(res).map(str -> !str.isEmpty() ? UUID.fromString(str) : null)
                    .filter(Objects::nonNull).toList();
        }
    }

    @Autowired
    public CardQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // queryForObject: bei nicht gefundener ID abfangen (Fehlermeldung)
    public Card get(UUID id) {
        final String query = "select id, answer, question, tag, categories from public.cards where id = :cardId;";
        return namedParameterJdbcTemplate.queryForObject(query,
                new MapSqlParameterSource().addValue("cardId", id), CARD_ROW_MAPPER);
    }

    public List<Card> getPage() {
        final String query = "select id, answer, question, tag, categories from public.cards;";
        return namedParameterJdbcTemplate.query(query, CARD_ROW_MAPPER);
    }


    // queryForObject: bei nicht gefundener ID abfangen (Fehlermeldung)
//    TODO: Db-Test
    public Boolean isCategoryEmpty(UUID categoryId) {
        final String query = "select CASE WHEN EXISTS (SELECT 1 FROM public.cards where " +
                "ARRAY_CONTAINS ( categories, :categoryId )) THEN 'FALSE' ELSE 'TRUE' END";
        return namedParameterJdbcTemplate.queryForObject(query,
                new MapSqlParameterSource().addValue("categoryId", categoryId), Boolean.class);
    }

}