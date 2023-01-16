package de.fantjastisch.cards_backend.category.repository;

import de.fantjastisch.cards_backend.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Diese Klasse stellt den Teil des Persistence-Layers bereit, welcher sich mit dem Lesen von Kategorien-Entitäten beschäftigt.
 * <p>
 * Im Rahmen des Persistence-Layers wird die JDBC Bibliothek für die Low-Level-Interaktion mit der Datenbank genutzt.
 *
 * @author Semjon Nirmann, Alexander Kück
 */
@Repository
public class CategoryQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CategoryQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<Category> CATEGORY_ROW_MAPPER = (rs, rowNum) -> {
        List<UUID> resultSetArr = parseSQLArrayToUUIDArray(rs.getString("sub_category_ids"));
        return Category.builder()
                .id(UUID.fromString(rs.getString("id")))
                .label(rs.getString("label"))
                .subCategories(resultSetArr).build();
    };

    private List<UUID> parseSQLArrayToUUIDArray(String arr) {
        if (arr == null) {
            return Collections.emptyList();
        }
        String[] res = arr.replaceAll("\\[|\\]", "").split(", ");
        return Arrays.stream(res).map(str -> !str.isEmpty() ? UUID.fromString(str) : null)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Diese Funktion holt alle Kategorien-Entitäten aus der Datenbank ein.
     *
     * @return Eine Liste aller Kategorien-Entitäten, gekapselt in {@link Category}-Instanzen.
     */
    public List<Category> getPage() {
        final String query = "select * from public.categories;";
        return namedParameterJdbcTemplate.query(query, CATEGORY_ROW_MAPPER);
    }

    /**
     * Diese Funktion holt eine Kategorien-Entität aus der Datenbank ein.
     *
     * @param id Die ID der Entität, welche aus der Datenbank ausgegeben werden soll.
     * @return Die gesuchte Entität, gekapselt in eine {@link Category}-Instanz,
     * oder null, sofern die Entität nicht gefunden werden konnte.
     * @throws EmptyResultDataAccessException Die Entität konnte nicht gefunden werden.
     */
    public Category get(UUID id) {
        final String query = "select * from public.categories where id = :id;";
        try {
            return namedParameterJdbcTemplate.queryForObject(query,
                    new MapSqlParameterSource().addValue("id", id),
                    CATEGORY_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
