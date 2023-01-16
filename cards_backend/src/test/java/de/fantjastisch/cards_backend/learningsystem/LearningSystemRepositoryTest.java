package de.fantjastisch.cards_backend.learningsystem;

//import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemCommandRepository;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Test Klasse für die LearningSystem Repositories
 *
 * @author Freja Sender, Alexander Kück
 */

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class LearningSystemRepositoryTest {
    private LearningSystemCommandRepository learningSystemCommandRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private LearningSystemQueryRepository learningSystemQueryRepository;

    @BeforeEach
    public void setUp() {
        learningSystemCommandRepository = new LearningSystemCommandRepository(namedParameterJdbcTemplate);
        learningSystemQueryRepository = new LearningSystemQueryRepository(namedParameterJdbcTemplate);
    }

    @Test
    public void shouldSaveLearningSystem() {
        LearningSystem tosave = LearningSystem.builder()
                .id(UUID.fromString("866092d7-b90c-4976-8fea-08012ab01b68"))
                .label("2box")
                .boxLabels(Arrays.asList("box1", "box2"))
                .build();

        learningSystemCommandRepository.save(tosave);

        LearningSystem actual = learningSystemQueryRepository.get(tosave.getId());

        Assertions.assertEquals(tosave, actual);

    }

    @Test
    public void shouldUpdateLearningSystemLabel() {
        LearningSystem toupdate = LearningSystem.builder()
                .id(UUID.fromString("8cdd43f0-4d5c-44d6-972f-b18ed068e9a6"))
                .label("2box")
                .boxLabels(Arrays.asList("box1", "box2"))
                .build();

        learningSystemCommandRepository.save(toupdate);

        toupdate.setLabel("3box");
        learningSystemCommandRepository.update(toupdate);

        LearningSystem actual = learningSystemQueryRepository.get(toupdate.getId());

        Assertions.assertEquals(toupdate, actual);
    }

    @Test
    public void shouldUpdateLearningSystemBoxLabels() {
        LearningSystem toupdate = LearningSystem.builder()
                .id(UUID.fromString("49b00687-bd00-4a87-9c37-5d8645a44c2a"))
                .label("2box")
                .boxLabels(Arrays.asList("box1", "box2"))
                .build();

        learningSystemCommandRepository.save(toupdate);

        toupdate.setBoxLabels(Arrays.asList("box1", "box2", "box3"));
        learningSystemCommandRepository.update(toupdate);

        LearningSystem actual = learningSystemQueryRepository.get(toupdate.getId());

        Assertions.assertEquals(toupdate, actual);
    }

    @Test
    public void shouldDeleteLearningSystem() {
        LearningSystem todelete = LearningSystem.builder()
                .id(UUID.fromString("098e616d-4a45-4813-900b-6aa0b2ef7ebf"))
                .label("2box")
                .boxLabels(Arrays.asList("box1", "box2"))
                .build();

        learningSystemCommandRepository.save(todelete);

        learningSystemCommandRepository.delete(learningSystemQueryRepository.get(todelete.getId()));

        Assertions.assertNull(learningSystemQueryRepository.get(todelete.getId()));

    }


    @Test
    public void shouldFindLearningSystem() {

        LearningSystem expected = LearningSystem.builder()
                .id(UUID.fromString("faf58948-c0a6-467b-b3ac-3097877bc235"))
                .label("2box")
                .boxLabels(Arrays.asList("box1", "box2"))
                .build();

        learningSystemCommandRepository.save(expected);

        LearningSystem actual = learningSystemQueryRepository.get(UUID.fromString("faf58948-c0a6-467b-b3ac-3097877bc235"));

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void shouldNotFindLearningSystem() {

        Assertions.assertNull(learningSystemQueryRepository.get(UUID.fromString("7953f9cc-f919-4c17-8b68-840be804d922")));

    }

    @Test
    public void shouldFindAllLearningSystems() {
        LearningSystem expected1 = LearningSystem.builder()
                .id(UUID.fromString("6c24fc2b-edab-4928-a4e4-fbd73f4341c7"))
                .label("2box")
                .boxLabels(Arrays.asList("box1", "box2"))
                .build();

        LearningSystem expected2 = LearningSystem.builder()
                .id(UUID.fromString("8f041566-cbd6-4f24-bc75-e83e027e7d71"))
                .label("2box")
                .boxLabels(Arrays.asList("box1", "box2"))
                .build();

        learningSystemCommandRepository.save(expected1);
        learningSystemCommandRepository.save(expected2);

        List<LearningSystem> expected = List.of(expected1, expected2);

        List<LearningSystem> actual = learningSystemQueryRepository.getPage();

        Assertions.assertEquals(expected, actual);

    }

}
