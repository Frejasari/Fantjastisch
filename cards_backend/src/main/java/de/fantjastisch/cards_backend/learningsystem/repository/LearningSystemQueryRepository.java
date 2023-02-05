package de.fantjastisch.cards_backend.learningsystem.repository;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Diese Klasse stellt den Teil des Persistence-Layers bereit, welcher sich mit dem Lesen von Lernsystem-Entitäten beschäftigt.
 * <p>
 * Im Rahmen des Persistence-Layers wird die JDBC Bibliothek für die Low-Level-Interaktion mit der Datenbank genutzt.
 *
 * @author Semjon Nirmann, Alex Kück, Jessica Repty
 */
@Repository
public class LearningSystemQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LearningSystemQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<LearningSystem> LEARNING_SYSTEM_ROW_MAPPER = (rs, rowNum) -> {
        List<String> resultSetArr = parseSQLArrayToStringArray(rs.getString("box_labels"));
        return LearningSystem.builder()
                .id(UUID.fromString(rs.getString("id")))
                .label(rs.getString("label"))
                .boxLabels(resultSetArr).build();
    };

    private List<String> parseSQLArrayToStringArray(final String arr) {
        if (arr == null) {
            return Collections.emptyList();
        }
        String[] res = arr.replaceAll("\\[|\\]", "").split(", ");
        return Arrays.stream(res).map(str -> !str.isEmpty() ? str : null)
                .filter(Objects::nonNull)
                .filter(str -> !str.equals("null"))
                .toList();
    }

    /**
     * Diese Funktion holt eine Lernsystem-Entität aus der Datenbank ein.
     *
     * @param id Die ID der Entität, welche aus der Datenbank ausgegeben werden soll.
     * @return Die gesuchte Entität, gekapselt in eine {@link LearningSystem}-Instanz,
     * oder null, sofern die Entität nicht gefunden werden konnte.
     * @throws EmptyResultDataAccessException Die Entität konnte nicht gefunden werden.
     */
    public LearningSystem get(final UUID id) {
        final String query = "SELECT * FROM public.learning_systems WHERE id = :id;";
        try {
            return namedParameterJdbcTemplate.queryForObject(query,
                    new MapSqlParameterSource().addValue("id", id),
                    LEARNING_SYSTEM_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Diese Funktion holt alle Lernsystem-Entitäten aus der Datenbank ein.
     *
     * @return Eine Liste aller Lernsystem-Entitäten, gekapselt in {@link LearningSystem}-Instanzen.
     */
    public List<LearningSystem> getPage() {
        final String query = "SELECT * FROM public.learning_systems;";
        return namedParameterJdbcTemplate.query(query, LEARNING_SYSTEM_ROW_MAPPER);
    }
}
