package com.javarush.jira.bugtracking.task;

import com.javarush.jira.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = "classpath:activity-data.sql")
class ActivityServiceTest extends BaseIntegrationTest {
    private final Long TASK_ID = 1L;
    @Autowired
    private ActivityService activityService;

    @Test
    void howLongWasInProgress() {
        Duration duration = activityService.howLongWasInProgress(TASK_ID);

        assertNotNull(duration);
        assertTrue(duration.toMillis() > 0);
        assertEquals(3, duration.toHours());
    }

    @Test
    void howLongWasInTesting() {
        Duration duration = activityService.howLongWasInTesting(TASK_ID);

        assertNotNull(duration);
        assertTrue(duration.toMillis() > 0);
        assertEquals(2, duration.toHours());
    }
}