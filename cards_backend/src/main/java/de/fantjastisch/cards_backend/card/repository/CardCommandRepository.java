package de.fantjastisch.cards_backend.card.repository;

import de.fantjastisch.cards_backend.card.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


// @Repository -> wie @Component nur ein Repo -> @Component s. CardAggregate
// kein Konstruktor -> das Ende der Dependency (s. CardsController)
@Repository
public class CardCommandRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CardCommandRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void create(Card card) {
        String command = "INSERT INTO public.cards (id, question, answer, tag, categories) VALUES (:id, :question, :answer, :tag, :categories);";
        // update -> used for storing/updating objects
        namedParameterJdbcTemplate.update(command, toParameterSource(card));
    }

    public void update(final Card card) {
        String command = "UPDATE public.cards SET question = :question, answer = :answer, tag = :tag, categories = :categories WHERE id = :id";
        namedParameterJdbcTemplate.update(command, toParameterSource(card));
    }

    public void delete(final Card card) {
        String command = "DELETE FROM public.cards WHERE id = :id";
        namedParameterJdbcTemplate.update(command, toParameterSource(card));
    }

    private SqlParameterSource toParameterSource(Card card) {
        return new MapSqlParameterSource()
                .addValue("id", card.getId())
                .addValue("question", card.getQuestion())
                .addValue("answer", card.getAnswer())
                .addValue("tag", card.getTag())
                .addValue("categories", card.getCategories().toArray());
    }
}