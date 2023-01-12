package de.fantjastisch.cards_backend.learningsystem.repository;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class LearningSystemQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<LearningSystem> LEARNING_SYSTEM_ROW_MAPPER = (rs, rowNum) -> LearningSystem.builder()
            .id(UUID.fromString(rs.getString("id")))
            .label(rs.getString("label"))
            .boxLabels((String[]) rs.getArray("box_labels").getArray()).build();

    @Autowired
    public LearningSystemQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

    public List<LearningSystem> getPage() {
        final String query = "select * from public.learning_systems;";
        return namedParameterJdbcTemplate.query(query, LEARNING_SYSTEM_ROW_MAPPER);
    }
}
