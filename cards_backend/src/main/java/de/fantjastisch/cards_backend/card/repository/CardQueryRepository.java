package de.fantjastisch.cards_backend.card.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Diese Klasse stellt den Teil des Persistence-Layers bereit, welcher sich mit dem Lesen von Karteikarte-Entitäten beschäftigt.
 * <p>
 * Im Rahmen des Persistence-Layers wird die JDBC Bibliothek für die Low-Level-Interaktion mit der Datenbank genutzt.
 *
 * @author Freja Sender, Tamari Bayer, Jessica Repty
 */
@Repository
public class CardQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<Card> CARD_ROW_MAPPER = (rs, rowNum) -> {
        ObjectMapper objectMapper = new ObjectMapper();

        Set<Card.Category> categories;
        Set<Link> links;
        try {
            categories = objectMapper.readValue(rs.getString("categories"),
                    objectMapper.getTypeFactory().constructCollectionType(Set.class, Card.Category.class));
            links = objectMapper.readValue(rs.getString("links"),
                    objectMapper.getTypeFactory().constructCollectionType(Set.class, Link.class));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Card.builder()
                .id(UUID.fromString(rs.getString("id")))
                .answer(rs.getString("answer"))
                .question(rs.getString("question"))
                .tag(rs.getString("tag"))
                .categories(categories)
                .links(links.stream().filter(Objects::nonNull).collect(Collectors.toSet()))
                .build();
    };

    @Autowired
    public CardQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Diese Funktion holt eine Karteikarte-Entität aus der Datenbank ein.
     *
     * @param id Die ID der Entität, welche aus der Datenbank ausgegeben werden soll.
     * @return Die gesuchte Entität, gekapselt in eine {@link Card}-Instanz,
     * oder null, sofern die Entität nicht gefunden werden konnte.
     * @throws EmptyResultDataAccessException Die Entität konnte nicht gefunden werden.
     */
    public Card get(UUID id) {
        final String query = "select c.id, c.answer, c.question, c.tag, " +
                "array_agg('{\"id\":\"'||cc.category_id||'\", \"label\" : \"'||cat.label||'\"}') as categories, " +
                "array_agg('{\"target\":\"'|| l.target ||'\", \"label\" : \"'||l.label||'\"}') as links " +
                "from public.cards c " +
                "join categories_to_cards cc on c.id = cc.card_id " +
                "join categories cat on cc.category_id = cat.id " +
                "left join links l on c.id = l.source " +
                "where c.id=:cardId " +
                "group by c.id";
        try {
            return namedParameterJdbcTemplate.queryForObject(query,
                    new MapSqlParameterSource().addValue("cardId", id), CARD_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Diese Funktion holt entsprechende Karteikarte-Entitäten aus der Datenbank ein.
     *
     * @param categoryFilter Die Liste der UUIDs der {@link de.fantjastisch.cards_backend.category.Category}-Entitäten.
     *                       Wenn sie nicht null ist, dann werden alle Karteikarten geholt, die zumindest einer Kategorie
     *                       aus der Liste gehören.
     * @param search         Der String, nach dem es gesucht wird.
     * @param tag            Der Tag, nach dem es gesucht wird.
     * @param sort           Der Boolean, der angibt, ob gefundene Karteikarten alphabetisch nach Tags sortiert werden sollen.
     * @return Eine Liste aller Karteikarte-Entitäten, gekapselt in {@link Card}-Instanzen.
     */
    public List<Card> getPage(final List<UUID> categoryFilter, final String search, final String tag, final boolean sort) {
        String query = "select c.id, c.answer, c.question, c.tag, " +
                "array_agg('{\"id\":\"'||cc.category_id||'\", \"label\" : \"'||cat.label||'\"}') as categories, " +
                "array_agg('{\"target\":\"'|| l.target ||'\", \"label\" : \"'||l.label||'\"}') as links " +
                "from public.cards c " +
                "join public.categories_to_cards cc on c.id = cc.card_id " +
                "left join links l on c.id = l.source " +
                "join categories cat on cc.category_id = cat.id ";

        final MapSqlParameterSource paramSource = new MapSqlParameterSource().addValue("tag", tag)
                .addValue("search", search);

        if (categoryFilter != null && !categoryFilter.isEmpty()) {
            query += "inner join (" +
                    "   select distinct(card_id) as card_id from public.categories_to_cards where ";
            List<String> orQueries = new ArrayList<>();
            for (int i = 0; i < categoryFilter.size(); i++) {
                UUID categoryID = categoryFilter.get(i);
                final String paramName = "categoryId" + i;
                orQueries.add(" category_ID =  :" + paramName);
                paramSource.addValue(paramName, categoryID);
            }
            query += String.join(" or ", orQueries) + ") as i on c.id = i.card_id ";
        }
        String queryForTag = " :tag is not null and tag = :tag ";
        String queryForSearch = " :search is not null and (question ilike '%' || :search || '%' or answer ilike '%' || :search || '%') ";
        List<String> queries = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            queries.add(queryForSearch);
        }
        if (tag != null && !tag.trim().isEmpty()) {
            queries.add(queryForTag);
        }
        if (!queries.isEmpty()) {
            query += " where" + String.join("and", queries);
        }
        query += " group by c.id";
        if (sort) {
            query += " order by tag";
        }
        query += ";";
        return namedParameterJdbcTemplate.query(query,
                paramSource, CARD_ROW_MAPPER);
    }
}