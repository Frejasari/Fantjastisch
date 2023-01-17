package de.fantjastisch.cards_backend.link.repository;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.link.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt den Teil des Persistence-Layers bereit, welcher sich mit dem Lesen von Link-Entitäten beschäftigt.
 * <p>
 * Im Rahmen des Persistence-Layers wird die JDBC Bibliothek für die Low-Level-Interaktion mit der Datenbank genutzt.
 *
 * @author Jessica Repty, Tamari Bayer
 */
@Repository
public class LinkQueryRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Link> LINK_ROW_MAPPER = (rs, rowNum) -> Link.builder()
            .id((UUID.fromString(rs.getString("id"))))
            .name((rs.getString("name")))
            .source(UUID.fromString(rs.getString("source")))
            .target(UUID.fromString(rs.getString("target"))).build();

    @Autowired
    public LinkQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Diese Funktion holt eine Link-Entität aus der Datenbank ein.
     *
     * @param id Die ID der Entität, welche aus der Datenbank ausgegeben werden soll.
     * @return Die gesuchte Entität, gekapselt in eine {@link Link}-Instanz,
     * oder null, sofern die Entität nicht gefunden werden konnte.
     * @throws EmptyResultDataAccessException Die Entität konnte nicht gefunden werden.
     */
    public Link get(UUID id) {
        final String query = "select * from public.links where id = :id;";
        try {
            return namedParameterJdbcTemplate.queryForObject(query,
                    new MapSqlParameterSource()
                            .addValue("id", id), LINK_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Diese Funktion holt alle ausgehenden Link-Entitäten, einer {@link de.fantjastisch.cards_backend.card.Card}
     * aus der Datenbank ein.
     *
     * @param soureId Die UUID einer {@link Card}, von welcher die ausgehenden Links angefordert werden.
     * @return Eine Liste der entsprechenden Entitäten vom Typ Link, gekapselt in entsprechenden {@link Link}-Instanzen.
     * @throws EmptyResultDataAccessException Die {@link de.fantjastisch.cards_backend.card.Card}-Entität konnte nicht gefunden werden.
     */
    public List<Link> getPage(UUID soureId) {
        final String query = "select * from public.links where source = :sourceID;";
        try {
            return namedParameterJdbcTemplate.query(query, new MapSqlParameterSource()
                    .addValue("sourceID", soureId), LINK_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
