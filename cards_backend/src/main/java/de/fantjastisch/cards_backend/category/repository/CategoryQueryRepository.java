package de.fantjastisch.cards_backend.category.repository;

import de.fantjastisch.cards_backend.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList())
                .stream().toList();
    }

    public List<Category> getList() {
        final String query = "select * from public.categories;";
        return namedParameterJdbcTemplate.query(query, CATEGORY_ROW_MAPPER);
    }

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
