package de.fantjastisch.cards_backend.link.query;

import de.fantjastisch.cards_backend.card.repository.CardCommandRepository;
import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.repository.LinkCommandRepository;
import de.fantjastisch.cards_backend.link.repository.LinkQueryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class LinkQueryRepositoryTest {
    private LinkQueryRepository linkQueryRepository;
    private CardCommandRepository cardCommandRepository;
    private LinkCommandRepository linkCommandRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        linkQueryRepository = new LinkQueryRepository(namedParameterJdbcTemplate);
        cardCommandRepository = new CardCommandRepository(namedParameterJdbcTemplate);
        linkCommandRepository = new LinkCommandRepository(namedParameterJdbcTemplate);
    }

    @Test
    public void shouldGetOneLink() {
        Link link = Link.builder()
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        linkCommandRepository.save(link);
        Link actual = linkQueryRepository.get(link.getName(), link.getSource());
        Assertions.assertEquals(link, actual);
    }

    @Test
    public void shouldNotGetNonExistingLink() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
                linkQueryRepository.get("Bäume", UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff")));
    }

    @Test
    public void shouldNotGetNonExistingSource() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
                linkQueryRepository.get("Bäume", UUID.fromString("57da14f8-c6e1-48e4-9071-ed92f5694aff")));
    }

    @Test
    public void shouldNotGetNonExistingTarget() {

    }

    @Test
    public void shouldGetAllLinks() {
        Link link1 = Link.builder()
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        Link link2 = Link.builder()
                .name("Welche")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        linkCommandRepository.save(link1);
        linkCommandRepository.save(link2);
        List<Link> expected = List.of(link1, link2);
        List<Link> actual = linkQueryRepository.getPage(link1.getSource());
        Assertions.assertEquals(expected, actual);
    }


}
