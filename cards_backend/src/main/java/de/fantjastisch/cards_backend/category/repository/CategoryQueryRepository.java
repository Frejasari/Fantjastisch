package de.fantjastisch.cards_backend.category.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fantjastisch.cards_backend.card.Card;
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
        ObjectMapper objectMapper = new ObjectMapper();

        List<Category> subCategoryList;
        try {
            subCategoryList = objectMapper.readValue(rs.getString("subcategories"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<UUID> resultSetArr = parseSQLArrayToUUIDArray(rs.getString("sub_category_ids"));
        return Category.builder()
                .id(UUID.fromString(rs.getString("id")))
                .label(rs.getString("label"))
                .subCategories(subCategoryList).build();
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
        final String queryForSub =  "select c.id, c.label, array_agg('{\"id\":\"'||cc.cat_id||'\", \"label\" : \"'||cat.label||'\"}') as categories " +
                "from public.categories c join  cat_to_subcat cc on c.id = cc.cat_id join categories cat on cc.subcat_id = cat.id " +
                "where c.id = :id  " +
                "group by c.id";
        try {
           Category cat = namedParameterJdbcTemplate.queryForObject(query,
                    new MapSqlParameterSource().addValue("id", id), CATEGORY_ROW_MAPPER);
            if (cat != null && !cat.getSubCategories().isEmpty()) {
                return namedParameterJdbcTemplate.queryForObject(queryForSub,
                        new MapSqlParameterSource().addValue("id", id),
                        CATEGORY_ROW_MAPPER);
            } else {
                return cat;
        }} catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<String> getLabels(UUID id) {
        final String query = "select id, label, array_agg('{\"id\":\"'||cc.c1||'\", \"label\" : \"'||label||'\"}')" +
                " as subcategories from categories " +
                "inner join (select cc.c1 from " +
                "unnest(select sub_category_ids from categories where id =:catId)) cc " +
                "on id = cc.c1 group by id;";
        final String query1 = "select c.id, c.label, array_agg('{\"id\":\"'||cc.cat_id||'\", \"label\" : \"'||cat.label||'\"}') as categories " +
                "from public.categories c join public.cat_to_subcat cc on c.id = cc.cat_id join public.categories cat on cc.subcat_id = cat.id " +
                "where c.id = :categoryId " +
                "group by c.id";
        final String query2 = "select label from public.categories inner join (select c1 as subcat from " +
                "unnest(select sub_category_ids from public.categories where id = :categoryId))  " +
                "on id = subcat group by id";
        try {
            List<Category> result = namedParameterJdbcTemplate.query(query2,
                    new MapSqlParameterSource().addValue("categoryId", id),
                    CATEGORY_ROW_MAPPER);
            return result.stream().map(Category::getLabel).toList();
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
