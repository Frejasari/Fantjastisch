package de.fantjastisch.cards_backend.link.repository;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.link.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class LinkQueryRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Link> LINK_ROW_MAPPER = (rs, rowNum) -> Link.builder()
            .name((rs.getString("name")))
            .source(UUID.fromString(rs.getString("source")))
            .target(UUID.fromString(rs.getString("target"))).build();

    @Autowired
    public LinkQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Link get(String name, UUID source) {
        final String query = "select * from public.links where name = :name and source = :sourceID;";
        return namedParameterJdbcTemplate.queryForObject(query,
                new MapSqlParameterSource()
                        .addValue("name", name)
                        .addValue("sourceID", source), LINK_ROW_MAPPER);
    }

    public List<Link> getPage(UUID source) {
        final String query = "select * from public.links where source = :sourceID;";
        return namedParameterJdbcTemplate.query(query, new MapSqlParameterSource()
                .addValue("sourceID", source), LINK_ROW_MAPPER);

    }
}
