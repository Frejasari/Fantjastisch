package de.fantjastisch.cards_backend.category.repository;

import de.fantjastisch.cards_backend.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryCommandRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CategoryCommandRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void create(Category category) {
        final String sql = "INSERT INTO public.categories (id, label, sub_category_ids) VALUES (:id, :label, :sub_category_ids)";

        namedParameterJdbcTemplate.update(sql, toParameterSource(category));
    }

    public void delete(Category category) {
        final String sql = "DELETE FROM public.categories WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, toParameterSource(category));
    }

    public void update(Category category) {
        final String sql = "UPDATE public.categories SET label = :label, sub_category_ids = :sub_category_ids WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, toParameterSource(category));
    }

    private SqlParameterSource toParameterSource(Category category) {
        return new MapSqlParameterSource()
                .addValue("id", category.getId())
                .addValue("label", category.getLabel())
                .addValue("sub_category_ids", category.getSubCategories().toArray());
    }
}
