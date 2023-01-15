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
        //Array labels = rs.getArray("box_labels");
        //Object[] obox_labels = (Object[])labels.getArray();
        //String[] box_labels = {null};
        //for(int i=0; i<obox_labels.length;i++) {
        //    box_labels[i] = (String)obox_labels[i];
        //}
        return LearningSystem.builder()
                .id(UUID.fromString(rs.getString("id")))
                .label(rs.getString("label"))
                .boxLabels((String[]) rs.getArray("box_labels").getArray()).build();
                //.boxLabels(box_labels).build();
    };


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
