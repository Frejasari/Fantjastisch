package de.fantjastisch.cards_backend.learningsystem.repository;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


/**
 * Diese Klasse stellt den Teil des Persistence-Layers bereit, welcher sich mit dem Erstellen, Aktualisieren und Löschen
 * von Lernsystem-Entitäten beschäftigt.
 * <p>
 * Im Rahmen des Persistence-Layers wird die JDBC Bibliothek für die Low-Level-Interaktion mit der Datenbank genutzt.
 *
 * @author Semjon Nirmann, Alex Kück, Jessica Repty
 */
@Repository
public class LearningSystemCommandRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LearningSystemCommandRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Diese Funktion fügt ein übergebenes Lernsystem in die Datenbank ein.
     *
     * @param learningSystem Das Lernsystem, welches in die Datenbank eingefügt werden soll.
     */
    public void save(LearningSystem learningSystem) {
        final String sql = "INSERT INTO public.learning_systems (id, label, box_labels) VALUES (:id, :label, :box_labels)";
        namedParameterJdbcTemplate.update(sql, toParameterSource(learningSystem));
    }

    /**
     * Diese Funktion löscht ein übergebenes Lernsystem aus der Datenbank.
     *
     * @param learningSystem Das Lernsystem, welches aus der Datenbank gelöscht werden soll.
     */
    public void delete(LearningSystem learningSystem) {
        final String sql = "DELETE FROM public.learning_systems WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, toParameterSource(learningSystem));
    }

    /**
     * Diese Funktion aktualisiert ein Lernsystem in der Datenbank, in dem diese mit den Feldern des übergebenen
     * Lernsystems überschrieben wird.
     *
     * @param learningSystem Das aktualisierte Lernsystem.
     */
    public void update(LearningSystem learningSystem) {
        final String sql = "UPDATE public.learning_systems SET label = :label, box_Labels = :box_labels WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, toParameterSource(learningSystem));
    }

    private SqlParameterSource toParameterSource(LearningSystem learningSystem) {
        return new MapSqlParameterSource()
                .addValue("id", learningSystem.getId())
                .addValue("label", learningSystem.getLabel())
                .addValue("box_labels", learningSystem.getBoxLabels().toArray());
    }
}
