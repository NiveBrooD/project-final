package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.task.to.ActivityTo;
import com.javarush.jira.common.error.DataConflictException;
import com.javarush.jira.login.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static com.javarush.jira.bugtracking.task.TaskUtil.getLatestValue;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final TaskRepository taskRepository;

    private final Handlers.ActivityHandler handler;

    private static void checkBelong(HasAuthorId activity) {
        if (activity.getAuthorId() != AuthUser.authId()) {
            throw new DataConflictException("Activity " + activity.getId() + " doesn't belong to " + AuthUser.get());
        }
    }

    //Сколько задача находилась в работе (ready_for_review минус in_progress ).
    public Duration howLongWasInProgress(Long taskId) {
        List<Activity> activities = handler.getRepository().findAllByTaskIdOrderByUpdatedDesc(taskId);
        Activity inProgress = activities.stream()
                .filter(activity -> "in_progress".equals(activity.getStatusCode()))
                .findFirst()
                .orElseThrow(() ->
                        new DataConflictException("Task was never in progress"));

        Activity readyForReview = activities.stream()
                .filter(activity -> "ready_for_review".equals(activity.getStatusCode()))
                .findFirst()
                .orElseThrow(() ->
                        new DataConflictException("Task was never ready for review"));

        if (inProgress.getUpdated() == null || readyForReview.getUpdated() == null) {
            throw new DataConflictException("Activity timestamps are not initialized");
        }
        if (readyForReview.getUpdated().isBefore(inProgress.getUpdated())) {
            throw new DataConflictException("Invalid activity order for task " + taskId);
        }
        return Duration.between(inProgress.getUpdated(), readyForReview.getUpdated());
    }

    //Сколько задача находилась на тестировании (done минус ready_for_review).
    public Duration howLongWasInTesting(Long taskId) {
        List<Activity> activities = handler.getRepository().findAllByTaskIdOrderByUpdatedDesc(taskId);
        Activity readyForReview = activities.stream()
                .filter(activity -> "ready_for_review".equals(activity.getStatusCode()))
                .findFirst()
                .orElseThrow(() ->
                        new DataConflictException("Task was never ready for review"));
        Activity finished = activities.stream()
                .filter(activity -> "done".equals(activity.getStatusCode()))
                .findFirst()
                .orElseThrow(() ->
                        new DataConflictException("Task was never done"));

        if (readyForReview.getUpdated() == null || finished.getUpdated() == null) {
            throw new DataConflictException("Activity timestamps are not initialized");
        }
        if (finished.getUpdated().isBefore(readyForReview.getUpdated())) {
            throw new DataConflictException("Invalid activity order for task " + taskId);
        }
        return Duration.between(readyForReview.getUpdated(), finished.getUpdated());
    }

    @Transactional
    public Activity create(ActivityTo activityTo) {
        checkBelong(activityTo);
        Task task = taskRepository.getExisted(activityTo.getTaskId());
        if (activityTo.getStatusCode() != null) {
            task.checkAndSetStatusCode(activityTo.getStatusCode());
        }
        if (activityTo.getTypeCode() != null) {
            task.setTypeCode(activityTo.getTypeCode());
        }
        return handler.createFromTo(activityTo);
    }

    @Transactional
    public void update(ActivityTo activityTo, long id) {
        checkBelong(handler.getRepository().getExisted(activityTo.getId()));
        handler.updateFromTo(activityTo, id);
        updateTaskIfRequired(activityTo.getTaskId(), activityTo.getStatusCode(), activityTo.getTypeCode());
    }

    @Transactional
    public void delete(long id) {
        Activity activity = handler.getRepository().getExisted(id);
        checkBelong(activity);
        handler.delete(activity.id());
        updateTaskIfRequired(activity.getTaskId(), activity.getStatusCode(), activity.getTypeCode());
    }

    private void updateTaskIfRequired(long taskId, String activityStatus, String activityType) {
        if (activityStatus != null || activityType != null) {
            Task task = taskRepository.getExisted(taskId);
            List<Activity> activities = handler.getRepository().findAllByTaskIdOrderByUpdatedDesc(task.id());
            if (activityStatus != null) {
                String latestStatus = getLatestValue(activities, Activity::getStatusCode);
                if (latestStatus == null) {
                    throw new DataConflictException("Primary activity cannot be delete or update with null values");
                }
                task.setStatusCode(latestStatus);
            }
            if (activityType != null) {
                String latestType = getLatestValue(activities, Activity::getTypeCode);
                if (latestType == null) {
                    throw new DataConflictException("Primary activity cannot be delete or update with null values");
                }
                task.setTypeCode(latestType);
            }
        }
    }
}
