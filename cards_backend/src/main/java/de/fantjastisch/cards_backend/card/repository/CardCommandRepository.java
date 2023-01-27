package de.fantjastisch.cards_backend.card.repository;

import de.fantjastisch.cards_backend.card.Link;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Diese Klasse stellt den Teil des Persistence-Layers bereit, welcher sich mit dem Erstellen, Aktualisieren und Löschen
 * von Karteikarte-Entitäten beschäftigt.
 * <p>
 * Im Rahmen des Persistence-Layers wird die JDBC Bibliothek für die Low-Level-Interaktion mit der Datenbank genutzt.
 *
 * @author Tamari Bayer, Jessica Repty, Freja Sender
 */
@Repository
public class CardCommandRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CardCommandRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Diese Funktion fügt eine übergebene Karteikarte in die Datenbank ein.
     *
     * @param card Die Karteikarte, welche in die Datenbank eingefügt werden soll.
     */
    @Transactional
    public void create(Card card) {
        updateCategoriesOfCard(card);
        updateLinksOfCard(card);

        String command = "INSERT INTO public.cards (id, question, answer, tag) VALUES (:id, :question, :answer, :tag);";

        namedParameterJdbcTemplate.update(command, toParameterSource(card));
    }

    /**
     * Diese Funktion aktualisiert eine Karteikarte in der Datenbank, in dem diese mit den Feldern des übergebenen
     * Links überschrieben wird.
     *
     * @param card Die aktualisierte Karteikarte.
     */
    @Transactional
    public void update(final Card card) {
        updateCategoriesOfCard(card);
        updateLinksOfCard(card);
        String command = "UPDATE public.cards SET question = :question, answer = :answer, tag = :tag WHERE id = :id";
        namedParameterJdbcTemplate.update(command, toParameterSource(card));
    }

    /**
     * Diese Funktion löscht eine übergebene Karteikarte aus der Datenbank.
     *
     * @param cardId Die Id der Karteikarte, welche aus der Datenbank gelöscht werden soll.
     */
    public void delete(final UUID cardId) {
        String command = "DELETE FROM public.cards WHERE id = :id";
        namedParameterJdbcTemplate.update(command, new MapSqlParameterSource().addValue("id", cardId));
    }

    private SqlParameterSource toParameterSource(Card card) {
        return new MapSqlParameterSource()
                .addValue("id", card.getId())
                .addValue("question", card.getQuestion())
                .addValue("answer", card.getAnswer())
                .addValue("tag", card.getTag().trim())
                .addValue("categories", card.getCategories().toArray());
    }

    @Transactional
    private void updateLinksOfCard(Card card) {
        String command = "DELETE FROM public.links WHERE source=:source;";
        namedParameterJdbcTemplate.update(command, new MapSqlParameterSource()
                .addValue("source", card.getId())
        );
        for (Link link : card.getLinks()) {
            String commandCat = "INSERT INTO public.links (label, source, target) VALUES (:label, :source, :target);";
            namedParameterJdbcTemplate.update(commandCat, new MapSqlParameterSource()
                    .addValue("label", link.getLabel())
                    .addValue("source", card.getId())
                    .addValue("target", link.getTarget()));
        }
    }

    @Transactional
    private void updateCategoriesOfCard(Card card) {
        namedParameterJdbcTemplate.update("delete from public.categories_to_cards cc where cc.card_id = :card_id",
                new MapSqlParameterSource()
                        .addValue("card_id", card.getId())
        );

        for (UUID categoryId : card.getCategories()) {
            String commandCat = "INSERT INTO public.categories_to_cards (category_ID, card_ID) VALUES (:category_id, :card_id)";
            namedParameterJdbcTemplate.update(commandCat, new MapSqlParameterSource()
                    .addValue("card_id", card.getId())
                    .addValue("category_id", categoryId));
        }
    }
}