package de.fantjastisch.cards_backend.link.command;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.aggregate.CreateLink;
import de.fantjastisch.cards_backend.link.aggregate.LinkAggregate;
import de.fantjastisch.cards_backend.link.repository.LinkCommandRepository;
import de.fantjastisch.cards_backend.link.repository.LinkQueryRepository;
import de.fantjastisch.cards_backend.link.validator.LinkValidator;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.LABEL_TAKEN_VIOLATION;
import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.NOT_NULL_VIOLATION;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class LinkCommandRepositoryTest {

    private LinkCommandRepository linkCommandRepository;
    private LinkQueryRepository linkQueryRepository;
    private LinkAggregate linkAggregate;
    private LinkValidator linkValidator;
    private Card target;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        linkCommandRepository = new LinkCommandRepository(namedParameterJdbcTemplate);
        linkQueryRepository = new LinkQueryRepository(namedParameterJdbcTemplate);
        linkValidator = new LinkValidator();
        linkAggregate = new LinkAggregate(linkCommandRepository, linkQueryRepository, linkValidator);
        target = Card.builder()
                .id(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .question("Was bedeutet Cacher")
                .answer("Versteckt")
                .tag("Wichtig")
                //.categories(categories)
                .build();
    }

    @Test
    public void saveLink() {
        Link newLink = Link.builder()
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        linkCommandRepository.save(newLink);
        Link actual = linkQueryRepository.get(newLink.getName(), newLink.getSource());
        Assertions.assertEquals(newLink, actual);
    }

    @Test
    public void shouldNotCreateNoName() {
        CreateLink command = CreateLink.builder()
                .name(null)
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        Assertions.assertThrows(CommandValidationException.class, () ->
                linkAggregate.handle(command));

        CreateLink command1 = CreateLink.builder()
                .name("")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        Assertions.assertThrows(CommandValidationException.class, () ->
                linkAggregate.handle(command1));
    }

    @Test
    public void shouldNotCreateNameTaken() {
        Link newLink = Link.builder()
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        linkCommandRepository.save(newLink);

        CreateLink link = CreateLink.builder()
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();

        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(LABEL_TAKEN_VIOLATION)
                .field("label")
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(link));
        Assertions.assertTrue(exception.getErrors().contains(labelTakenError));
    }

    @Test
    public void shouldNotCreateNoSource() {
        CreateLink command = CreateLink.builder()
                .name("Cache")
                .source(null)
                .target(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .build();

        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("source")
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(command));
        Assertions.assertTrue(exception.getErrors().contains(labelTakenError));
    }

    @Test
    public void shouldNotCreateNoTarget() {
        CreateLink command = CreateLink.builder()
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(null)
                .build();

        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("target")
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(command));
        Assertions.assertTrue(exception.getErrors().contains(labelTakenError));
    }

    @Test
    public void shouldUpdate() {
        Card newTarget = Card.builder()
                .id(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b8e"))
                .question("Was ist Direct Mapped?")
                .answer("Ein Cacheart")
                .tag("Wichtig")
                //.categories(categories)
                .build();

        Link newLink = Link.builder()
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(target.getId())
                .build();
        linkCommandRepository.save(newLink);
        Link updated = Link.builder()
                .name(newLink.getName())
                .source(newLink.getSource())
                .target(newTarget.getId())
                .build();
        linkCommandRepository.update(updated);
        Assertions.assertEquals(updated, linkQueryRepository.get(newLink.getName(), newLink.getSource()));
    }

    @Test
    public void shouldNotUpdateNonExisting() {
        Link link = Link.builder()
                .name("Dijkstra")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
                linkCommandRepository.update(linkQueryRepository.get(link.getName(), link.getSource())));
    }

    @Test
    public void shouldDelete() {
        Link newLink = Link.builder()
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        linkCommandRepository.save(newLink);
        linkCommandRepository.delete(newLink);
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> linkQueryRepository.get(newLink.getName(), newLink.getSource()));
    }

    @Test
    public void shouldNotDeleteNonExisting() {
        Link link = Link.builder()
                .name("Dijkstra")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
                linkCommandRepository.delete(linkQueryRepository.get(link.getName(), link.getSource())));

    }

}
