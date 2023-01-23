package de.fantjastisch.cards_backend.link;

import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.link.aggregate.CreateLink;
import de.fantjastisch.cards_backend.link.aggregate.LinkAggregate;
import de.fantjastisch.cards_backend.link.aggregate.UpdateLink;
import de.fantjastisch.cards_backend.link.repository.LinkCommandRepository;
import de.fantjastisch.cards_backend.link.repository.LinkQueryRepository;
import de.fantjastisch.cards_backend.link.validator.LinkValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.NOT_BLANK_VIOLATION;
import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.NOT_NULL_VIOLATION;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class LinkAggregateTest {

    private LinkAggregate linkAggregate;

    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private LinkCommandRepository linkCommandRepository;
    @Mock
    private LinkQueryRepository linkQueryRepository;
    @Mock
    private CardQueryRepository cardQueryRepository;


    @BeforeEach
    public void setUp() {
        LinkValidator linkValidator = new LinkValidator(linkQueryRepository, cardQueryRepository);
        linkAggregate = new LinkAggregate(linkCommandRepository, linkQueryRepository, linkValidator, uuidGenerator);
    }

    @Test
    public void shouldThrowWhenLinkNotFound() {
        UpdateLink toUpdate = UpdateLink.builder()
                .id(UUID.fromString("76c991e8-cc52-4fa5-ac35-1c5c8fc54ef1"))
                .name("Cache")
                .source(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();
        assertThrows(ResponseStatusException.class, () -> linkAggregate.handle(toUpdate));

        UUID id = UUID.randomUUID();
        assertThrows(ResponseStatusException.class, () -> linkAggregate.handleDelete(id));
    }

    @Test
    public void shouldNotCreateLinkWithNoNameEmptyString() {
        CreateLink command = CreateLink.builder()
                .name("")
                .source(UUID.fromString("c83ae09a-bbf9-46c1-a3d0-6447d6def43c"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();

        ErrorEntry blankName = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("name")
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(command));
        assertTrue(exception.getErrors().contains(blankName));


        CreateLink commandNull = CreateLink.builder()
                .name(null)
                .source(UUID.fromString("c83ae09a-bbf9-46c1-a3d0-6447d6def43c"))
                .target(UUID.fromString("2f1f39d3-1db0-48fc-bb3f-f7ce7831dabe"))
                .build();

        ErrorEntry nullNameError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("name")
                .build();
        CommandValidationException exceptionNull = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(commandNull));
        assertTrue(exceptionNull.getErrors().contains(nullNameError));
    }

    @Test
    public void shouldNotCreateLinkWithNonExistingSource() {
        CreateLink commandNull = CreateLink.builder()
                .name("Cache")
                .source(null)
                .target(UUID.fromString("47da14f8-c6e1-48e4-9071-ed92f5694aff"))
                .build();

        ErrorEntry blankNameError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("source")
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(commandNull));
        assertTrue(exception.getErrors().contains(blankNameError));


        CreateLink commandNonExsiting = CreateLink.builder()
                .name("Cache")
                .source(UUID.fromString("faa14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();

        assertThrows(ResponseStatusException.class, () -> linkAggregate.handle(commandNonExsiting));
    }

    @Test
    public void shouldNotCreateLinkWithNonExistingTarget() {
        CreateLink commandNull = CreateLink.builder()
                .name("Cache")
                .source(UUID.fromString("c83ae09a-bbf9-46c1-a3d0-6447d6def43c"))
                .target(null)
                .build();

        ErrorEntry blankNameError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("target")
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(commandNull));
        assertTrue(exception.getErrors().contains(blankNameError));


        CreateLink commandNonExsiting = CreateLink.builder()
                .name("Cache")
                .source(UUID.fromString("c83ae09a-bbf9-46c1-a3d0-6447d6def43c"))
                .target(UUID.fromString("bc5d66d2-83d3-4b4f-8c2d-6c16ff6c86f3"))
                .build();

        assertThrows(ResponseStatusException.class, () -> linkAggregate.handle(commandNonExsiting));
    }

    @Test
    public void shouldNotUpdateLinkWithEmptyName() {
        UpdateLink commandBlank = UpdateLink.builder()
                .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
                .name("")
                .source(UUID.fromString("c83ae09a-bbf9-46c1-a3d0-6447d6def43c"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();

        ErrorEntry blankNameError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("name")
                .build();
        CommandValidationException exceptionBlank = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(commandBlank));
        assertTrue(exceptionBlank.getErrors().contains(blankNameError));


        UpdateLink commandNull = UpdateLink.builder()
                .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
                .name(null)
                .source(UUID.fromString("c83ae09a-bbf9-46c1-a3d0-6447d6def43c"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();

        ErrorEntry nullNameError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("name")
                .build();
        CommandValidationException exceptionNull = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(commandNull));
        assertTrue(exceptionNull.getErrors().contains(nullNameError));
    }

    @Test
    public void shouldNotUpdateLinkWithNonExistingTarget() {
        UpdateLink commandNull = UpdateLink.builder()
                .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
                .name("Cache")
                .source(UUID.fromString("c83ae09a-bbf9-46c1-a3d0-6447d6def43c"))
                .target(null)
                .build();

        ErrorEntry blankNameError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("target")
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(commandNull));
        assertTrue(exception.getErrors().contains(blankNameError));


        UpdateLink commandNonExsiting = UpdateLink.builder()
                .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
                .name("Cache")
                .source(UUID.fromString("c83ae09a-bbf9-46c1-a3d0-6447d6def43c"))
                .target(UUID.fromString("bc5d66d2-83d3-4b4f-8c2d-6c16ff6c86f3"))
                .build();

        assertThrows(ResponseStatusException.class, () -> linkAggregate.handle(commandNonExsiting));
    }


    @Test
    public void shouldNotUpdateLinkWithNonExistingSource() {
        UpdateLink commandNull = UpdateLink.builder()
                .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
                .name("Cache")
                .source(null)
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();

        ErrorEntry blankNameError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("source")
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> linkAggregate.handle(commandNull));
        assertTrue(exception.getErrors().contains(blankNameError));


        UpdateLink commandNonExsiting = UpdateLink.builder()
                .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
                .name("Cache")
                .source(UUID.fromString("faa14f8-c6e1-48e4-9071-ed92f5694aff"))
                .target(UUID.fromString("12557dce-4dbf-4531-869a-fd9e20533b7e"))
                .build();

        assertThrows(ResponseStatusException.class, () -> linkAggregate.handle(commandNonExsiting));
    }
}