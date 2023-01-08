package de.fantjastisch.cards_backend.link.repository;

import de.fantjastisch.cards_backend.link.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class LinkCommandRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public LinkCommandRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(Link link) {
        String command = "INSERT INTO public.links (name, source, target) VALUES (:name, :source, :target);";
        namedParameterJdbcTemplate.update(command, toParameterSource(link));
    }

    public void update(final Link link) {
        String command = "UPDATE public.links SET name = :name, source = :source, target = :target";
        namedParameterJdbcTemplate.update(command, new MapSqlParameterSource()
                .addValue("name", link.getName())
                .addValue("source", link.getSource())
                .addValue("target", link.getTarget()));
    }

    public void delete(final Link link) {
        String command = "DELETE FROM public.links WHERE name = :name AND target = :target";
        namedParameterJdbcTemplate.update(command, new MapSqlParameterSource()
                .addValue("name", link.getName())
                .addValue("target", link.getTarget()));
    }

    private SqlParameterSource toParameterSource(Link link) {
        return new MapSqlParameterSource()
                .addValue("name", link.getName())
                .addValue("target", link.getTarget())
                .addValue("source", link.getSource());
    }
}