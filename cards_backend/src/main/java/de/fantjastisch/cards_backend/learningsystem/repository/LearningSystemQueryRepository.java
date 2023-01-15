package de.fantjastisch.cards_backend.learningsystem.repository;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.util.*;

@Repository
public class LearningSystemQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LearningSystemQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    private final RowMapper<LearningSystem> LEARNING_SYSTEM_ROW_MAPPER = (rs, rowNum) ->
    {
        String[] resultSetArr = parseSQLArrayToUUIDArray(rs.getString("box_labels")).toArray(String[]::new);
        return LearningSystem.builder()
                .id(UUID.fromString(rs.getString("id")))
                .label(rs.getString("label"))
                .boxLabels(resultSetArr).build();
    };

    private List<String> parseSQLArrayToUUIDArray(String arr) {
        if (arr == null) {
            return Collections.emptyList();
        }
        String[] res = arr.replaceAll("\\[|\\]", "").split(", ");
        return Arrays.stream(res).map(str -> !str.isEmpty() ? str : null)
                .filter(str -> !str.equals("null"))
                .toList();
    }
    public LearningSystem get(UUID id) {
        final String query = "select * from public.learning_systems where id = :id;";
        try {
            return namedParameterJdbcTemplate.queryForObject(query,
                    new MapSqlParameterSource().addValue("id", id),
                    LEARNING_SYSTEM_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<LearningSystem> getList() {
        final String query = "select * from public.learning_systems;";
        return namedParameterJdbcTemplate.query(query, LEARNING_SYSTEM_ROW_MAPPER);
    }
}
