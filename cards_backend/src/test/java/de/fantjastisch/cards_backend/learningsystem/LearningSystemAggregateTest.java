package de.fantjastisch.cards_backend.learningsystem;

import de.fantjastisch.cards_backend.learningsystem.aggregate.CreateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.LearningSystemAggregate;
import de.fantjastisch.cards_backend.learningsystem.aggregate.UpdateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemCommandRepository;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import de.fantjastisch.cards_backend.learningsystem.validator.LearningSystemValidator;
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

import java.util.Arrays;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test Klasse für die LearningSystem Repositories
 *
 * @author Freja Sender, Alexander Kück
 */
@ExtendWith(MockitoExtension.class)
public class LearningSystemAggregateTest {
    private LearningSystemAggregate learningSystemAggregate;
    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private LearningSystemCommandRepository learningSystemCommandRepository;
    @Mock
    private LearningSystemQueryRepository learningSystemQueryRepository;


    private final LearningSystem ls = LearningSystem.builder()
            .id(UUID.fromString("2be2989b-215f-49ca-ae95-366b2a3db03d"))
            .label("Testlabel")
            .boxLabels(Arrays.asList("Box1", "Box2"))
            .build();



    @BeforeEach
    public void setUp() {
        LearningSystemValidator learningSystemValidator = new LearningSystemValidator(learningSystemQueryRepository);
        learningSystemAggregate = new LearningSystemAggregate(learningSystemCommandRepository, learningSystemValidator, learningSystemQueryRepository,uuidGenerator);
    }
    @Test
    public void shouldCreate(){
        when(uuidGenerator.randomUUID()).thenReturn(ls.getId());
        CreateLearningSystem toCreate = CreateLearningSystem.builder()
                .label(ls.getLabel())
                .boxLabels(ls.getBoxLabels())
                .build();
        learningSystemAggregate.handle(toCreate);
        verify(learningSystemCommandRepository, times(1)).save(ls);

    }
    @Test
    public void shouldUpdate(){
        when(learningSystemQueryRepository.get(ls.getId())).thenReturn(ls);
        UpdateLearningSystem toUpdate = UpdateLearningSystem.builder()
                .id(ls.getId())
                .label("Testlabel2")
                .boxLabels(ls.getBoxLabels())
                .build();
        learningSystemAggregate.handle(toUpdate);
        verify(learningSystemCommandRepository, times(1)).update(LearningSystem.builder()
                .id(ls.getId())
                .label(toUpdate.getLabel())
                .boxLabels(ls.getBoxLabels())
                .build());

    }
    @Test
    public void shouldDelete()
    {
        when(learningSystemQueryRepository.get(ls.getId())).thenReturn(ls);
        learningSystemAggregate.handleDelete(ls.getId());
        verify(learningSystemCommandRepository,times(1)).delete(ls.getId());
    }
    @Test
    public void shouldGet()
    {
        when(learningSystemQueryRepository.get(ls.getId())).thenReturn(ls);
        learningSystemAggregate.handleDelete(ls.getId());
        verify(learningSystemQueryRepository,times (2)).get(ls.getId());
    }
    @Test
    public void shouldGetPage()
    {
        learningSystemAggregate.handle();
        verify(learningSystemQueryRepository,times (1)).getPage();
    }

    @Test
    public void shouldThrowWhenCreatingWithEmptyLabel() {
        CreateLearningSystem toCreate = CreateLearningSystem.builder()
                .label("")
                .boxLabels(ls.getBoxLabels())
                .build();

        CommandValidationException exceptionblank = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toCreate));

        ErrorEntry error = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("label")
                .build();
        assertTrue(exceptionblank.getErrors().contains(error));

        CreateLearningSystem toCreate2 = CreateLearningSystem.builder()
                .label(null)
                .boxLabels(ls.getBoxLabels())
                .build();

        CommandValidationException exceptionnull = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toCreate2));

        ErrorEntry error2 = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("label")
                .build();
        assertTrue(exceptionnull.getErrors().contains(error2));
    }


    @Test
    public void shouldThrowWhenUpdatingWithEmptyLabel() {
        UpdateLearningSystem toUpdate = UpdateLearningSystem.builder()
                .id(ls.getId())
                .label("")
                .boxLabels(ls.getBoxLabels())
                .build();

        CommandValidationException exceptionblank = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toUpdate));

        ErrorEntry error = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("label")
                .build();
        assertTrue(exceptionblank.getErrors().contains(error));

        UpdateLearningSystem toUpdate2 = UpdateLearningSystem.builder()
                .id(ls.getId())
                .label(null)
                .boxLabels(ls.getBoxLabels())
                .build();

        CommandValidationException exceptionnull = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toUpdate2));

        ErrorEntry error2 = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("label")
                .build();
        assertTrue(exceptionnull.getErrors().contains(error2));
    }

    @Test
    public void shouldThrowWhenCreatingWithEmptyBoxLabels() {
        CreateLearningSystem toCreate = CreateLearningSystem.builder()
                .label(ls.getLabel())
                .boxLabels(null)
                .build();

        CommandValidationException exceptionnull = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toCreate));

        ErrorEntry error = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("boxLabels")
                .build();
        assertTrue(exceptionnull.getErrors().contains(error));



        CreateLearningSystem toCreate2 = CreateLearningSystem.builder()
                .label(ls.getLabel())
                .boxLabels(Arrays.asList(""))
                .build();

        CommandValidationException exceptionblank = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toCreate2));

        ErrorEntry error2 = ErrorEntry.builder()
                .code(BOX_LABELS_IS_NULL_VIOLATION)
                .field("boxLabels")
                .build();
        assertTrue(exceptionblank.getErrors().contains(error2));

        CreateLearningSystem toCreate3 = CreateLearningSystem.builder()
                .label(ls.getLabel())
                .boxLabels(Arrays.asList("Test",""))
                .build();

        CommandValidationException exceptionblank2 = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toCreate3));

        ErrorEntry error3 = ErrorEntry.builder()
                .code(BOX_LABELS_IS_NULL_VIOLATION)
                .field("boxLabels")
                .build();
        assertTrue(exceptionblank2.getErrors().contains(error3));

        CreateLearningSystem toCreate4 = CreateLearningSystem.builder()
                .label(ls.getLabel())
                .boxLabels(Arrays.asList("Test",null))
                .build();

        CommandValidationException exceptionnull2 = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toCreate4));

        ErrorEntry error4 = ErrorEntry.builder()
                .code(BOX_LABELS_IS_NULL_VIOLATION)
                .field("boxLabels")
                .build();
        assertTrue(exceptionnull2.getErrors().contains(error4));
    }

    @Test
    public void shouldThrowWhenUpdatingWithEmptyBoxLabels() {
        UpdateLearningSystem toUpdate = UpdateLearningSystem.builder()
                .id(ls.getId())
                .label(ls.getLabel())
                .boxLabels(null)
                .build();

        CommandValidationException exceptionnull = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toUpdate));

        ErrorEntry error = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("boxLabels")
                .build();
        assertTrue(exceptionnull.getErrors().contains(error));



        UpdateLearningSystem toUpdate2 = UpdateLearningSystem.builder()
                .id(ls.getId())
                .label(ls.getLabel())
                .boxLabels(Arrays.asList(""))
                .build();

        CommandValidationException exceptionblank = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toUpdate2));

        ErrorEntry error2 = ErrorEntry.builder()
                .code(BOX_LABELS_IS_NULL_VIOLATION)
                .field("boxLabels")
                .build();
        assertTrue(exceptionblank.getErrors().contains(error2));

        UpdateLearningSystem toUpdate3 = UpdateLearningSystem.builder()
                .id(ls.getId())
                .label(ls.getLabel())
                .boxLabels(Arrays.asList("Test",""))
                .build();

        CommandValidationException exceptionblank2 = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toUpdate3));

        ErrorEntry error3 = ErrorEntry.builder()
                .code(BOX_LABELS_IS_NULL_VIOLATION)
                .field("boxLabels")
                .build();
        assertTrue(exceptionblank2.getErrors().contains(error3));

        UpdateLearningSystem toUpdate4 = UpdateLearningSystem.builder()
                .id(ls.getId())
                .label(ls.getLabel())
                .boxLabels(Arrays.asList("Test",null))
                .build();

        CommandValidationException exceptionnull2 = assertThrows(CommandValidationException.class, ()-> learningSystemAggregate.handle(toUpdate4));

        ErrorEntry error4 = ErrorEntry.builder()
                .code(BOX_LABELS_IS_NULL_VIOLATION)
                .field("boxLabels")
                .build();
        assertTrue(exceptionnull2.getErrors().contains(error4));
    }

    @Test
    public void shouldThrowWhenUpdatingNotExisting() {
        UpdateLearningSystem toUpdate = UpdateLearningSystem.builder()
                .id(UUID.fromString("48039f58-ad1e-434e-a2e4-b8179ffa1c68"))
                .label("2Box")
                .boxLabels(Arrays.asList("Box1", "Box2"))
                .build();
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> learningSystemAggregate.handle(toUpdate));
    }

    @Test
    public void shouldThrowWhenDeletingNotExisting() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> learningSystemAggregate.handleDelete(UUID.fromString("f5d15dd3-4670-45dc-a2bc-eed985c3a8be")));
    }
}
