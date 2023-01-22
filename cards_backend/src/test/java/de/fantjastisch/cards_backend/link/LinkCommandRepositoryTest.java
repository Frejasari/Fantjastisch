package de.fantjastisch.cards_backend.link;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.link.repository.LinkCommandRepository;
import de.fantjastisch.cards_backend.link.repository.LinkQueryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class LinkCommandRepositoryTest {

    private LinkCommandRepository linkCommandRepository;
    private LinkQueryRepository linkQueryRepository;
    private Card target;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        linkCommandRepository = new LinkCommandRepository(namedParameterJdbcTemplate);
        linkQueryRepository = new LinkQueryRepository(namedParameterJdbcTemplate);
        target = Card.builder()
                .id(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .question("Was bedeutet Cacher")
                .answer("Versteckt")
                .tag("Wichtig")
                .categories(Collections.emptyList())
                .build();
    }

    @Test
    public void createNewLink() {
        Link newLink = Link.builder()
                .id(UUID.fromString("ac7f0438-561c-4e82-9c25-bdacc869f3b8"))
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        linkCommandRepository.save(newLink);
        Link actual = linkQueryRepository.get(newLink.getId());
        Assertions.assertEquals(newLink, actual);
    }

    @Test
    public void shouldUpdateTarget() {
        Card newTarget = Card.builder()
                .id(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b8e"))
                .question("Was ist Direct Mapped?")
                .answer("Ein Cacheart")
                .tag("Wichtig")
                .categories(Collections.emptyList())
                .build();

        Link oldLink = Link.builder()
                .id(UUID.fromString("ccafb61b-4236-4961-8e0d-ca2e5555ee7f"))
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(target.getId())
                .build();
        linkCommandRepository.save(oldLink);
        Link updated = Link.builder()
                .id(oldLink.getId())
                .name(oldLink.getName())
                .source(oldLink.getSource())
                .target(newTarget.getId())
                .build();
        linkCommandRepository.update(updated);
        Assertions.assertEquals(updated, linkQueryRepository.get(oldLink.getId()));
    }

    @Test
    public void shouldUpdateSource() {
        Card newSource = Card.builder()
                .id(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b8e"))
                .question("Was ist Direct Mapped?")
                .answer("Ein Cacheart")
                .tag("Wichtig")
                .categories(Collections.emptyList())
                .build();

        Link oldLink = Link.builder()
                .id(UUID.fromString("ccafb61b-4236-4961-8e0d-ca2e5555ee7f"))
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(target.getId())
                .build();
        linkCommandRepository.save(oldLink);
        Link updated = Link.builder()
                .id(oldLink.getId())
                .name(oldLink.getName())
                .source(newSource.getId())
                .target(oldLink.getTarget())
                .build();
        linkCommandRepository.update(updated);
        Assertions.assertEquals(updated, linkQueryRepository.get(oldLink.getId()));
    }

    @Test
    public void shouldUpdateName() {
        Link oldLink = Link.builder()
                .id(UUID.fromString("ccafb61b-4236-4961-8e0d-ca2e5555ee7f"))
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(target.getId())
                .build();
        linkCommandRepository.save(oldLink);
        Link updated = Link.builder()
                .id(oldLink.getId())
                .name("Cachearten")
                .source(oldLink.getSource())
                .target(oldLink.getTarget())
                .build();
        linkCommandRepository.update(updated);
        Assertions.assertEquals(updated.getName(), "Cachearten");
    }

    @Test
    public void shouldDeleteLink() {
        Link link = Link.builder()
                .id(UUID.fromString("ccafb61b-4236-4961-8e0d-ca2e5555ee7f"))
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        linkCommandRepository.save(link);
        linkCommandRepository.delete(link.id);
        Assertions.assertNull(linkQueryRepository.get(link.getId()));
    }


    private final Link link = Link.builder()
            .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
            .name("Lokalitätsprinzip")
            .source(UUID.fromString("693cc262-94d0-4b53-a48f-32d3cbefd4ee"))
            .target(UUID.fromString("2f1f39d3-1db0-48fc-bb3f-f7ce7831dabe"))
            .build();

    @Test
    public void shouldGetOneLink() {
        linkCommandRepository.save(link);
        Link actual = linkQueryRepository.get(link.getId());
        Assertions.assertEquals(link, actual);
    }

    @Test
    public void shouldNotGetNonExistingLink() {
        Assertions.assertNull(linkQueryRepository.get(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff")));
    }

    @Test
    public void shouldGetAllLinks() {
        Link link1 = Link.builder()
                .id(UUID.fromString("e3d001fc-586b-46f7-b82c-84489f978bf9"))
                .name("Cache")
                .source(UUID.fromString("693cc262-94d0-4b53-a48f-32d3cbefd4ee"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        linkCommandRepository.save(link);
        linkCommandRepository.save(link1);
        List<Link> expected = List.of(link, link1);
        List<Link> actual = linkQueryRepository.getPage(link.getSource());
        Assertions.assertEquals(expected, actual);
    }
}
