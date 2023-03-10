package de.fantjastisch.cards_backend.category.repository;

import de.fantjastisch.cards_backend.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CategoryCommandRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CategoryCommandRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Diese Funktion fügt eine übergebene Kategorie in die Datenbank ein.
     *
     * @param category Die Kategorie, welche in die Datenbank eingefügt werden soll.
     */
    public void create(final Category category) {
        final String sql = "INSERT INTO public.categories (id, label, sub_category_ids) VALUES (:id, :label, :sub_category_ids)";

        namedParameterJdbcTemplate.update(sql, toParameterSource(category));
    }

    /**
     * Diese Funktion löscht eine übergebene Kategorie aus der Datenbank.
     *
     * @param categoryId Die Id der Kategorie, welche aus der Datenbank gelöscht werden soll.
     */
    public void delete(final UUID categoryId) {
        final String sql = "DELETE FROM public.categories WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource().addValue("id", categoryId));
    }

    /**
     * Diese Funktion aktualisiert eine Kategorie in der Datenbank, in dem diese mit den Feldern der übergebenen
     * Kategorie überschrieben wird.
     *
     * @param category Die aktualisierte Kategorie.
     */
    public void update(final Category category) {
        final String sql = "UPDATE public.categories SET label = :label, sub_category_ids = :sub_category_ids WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, toParameterSource(category));
    }

    //
    private SqlParameterSource toParameterSource(Category category) {
        return new MapSqlParameterSource()
                .addValue("id", category.getId())
                .addValue("label", category.getLabel())
                .addValue("sub_category_ids", category.getSubCategories().toArray());
    }
}
