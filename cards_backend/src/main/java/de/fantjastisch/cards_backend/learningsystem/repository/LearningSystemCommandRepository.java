package de.fantjastisch.cards_backend.learningsystem.repository;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class LearningSystemCommandRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LearningSystemCommandRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public UUID save(LearningSystem learningSystem) {
        final String sql = "INSERT INTO public.learning_systems (id, label, box_labels) VALUES (:id, :label, :box_labels)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql,
                toParameterSource(learningSystem), keyHolder, new String[]{"id"});

        return UUID.fromString(keyHolder.getKeys().get("id").toString());
    }

    public void delete(LearningSystem learningSystem) {
        final String sql = "DELETE FROM public.learning_systems WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, toParameterSource(learningSystem));
    }

    public void update(LearningSystem learningSystem) {
        final String sql = "UPDATE public.learning_systems SET label = :label, box_Labels = :box_labels WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, toParameterSource(learningSystem));
    }

    private SqlParameterSource toParameterSource(LearningSystem learningSystem) {
        return new MapSqlParameterSource()
                .addValue("id", learningSystem.getId())
                .addValue("label", learningSystem.getLabel())
                .addValue("box_labels", learningSystem.getBoxLabels());
    }
}
