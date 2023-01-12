package de.fantjastisch.cards_backend.link.repository;

import de.fantjastisch.cards_backend.link.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * Diese Klasse stellt den Teil des Persistence-Layers bereit, welcher sich mit dem Erstellen, Aktualisieren und Löschen
 * von Link-Entitäten beschäftigt.
 * <p>
 * Im Rahmen des Persistence-Layers wird die JDBC Bibliothek für die Low-Level-Interaktion mit der Datenbank genutzt.
 *
 * @author Jessica Repty, Tamari Bayer
 */
@Repository
public class LinkCommandRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LinkCommandRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Diese Funktion fügt einen übergebenen Link in die Datenbank ein.
     *
     * @param link Der Link, welcher in die Datenbank eingefügt werden soll.
     */
    public void save(Link link) {
        String command = "INSERT INTO public.links (id, name, source, target) VALUES (:id, :name, :source, :target);";
        namedParameterJdbcTemplate.update(command, toParameterSource(link));
    }

    /**
     * Diese Funktion aktualisiert einen Link in der Datenbank, in dem diese mit den Feldern des übergebenen
     * Links überschrieben wird.
     *
     * @param link Der aktualisierte Link.
     */
    public void update(final Link link) {
        String command = "UPDATE public.links SET name = :name, source = :source, target = :target WHERE id = :id;";
        namedParameterJdbcTemplate.update(command, toParameterSource(link));
    }

    /**
     * Diese Funktion löscht einen übergebenen Link aus der Datenbank.
     *
     * @param link Der Link, welcher aus der Datenbank gelöscht werden soll.
     */
    public void delete(final Link link) {
        String command = "DELETE FROM public.links WHERE id = :id;";
        namedParameterJdbcTemplate.update(command, toParameterSource(link));
    }

    private SqlParameterSource toParameterSource(Link link) {
        return new MapSqlParameterSource()
                .addValue("id", link.getId())
                .addValue("name", link.getName())
                .addValue("target", link.getTarget())
                .addValue("source", link.getSource());
    }
}