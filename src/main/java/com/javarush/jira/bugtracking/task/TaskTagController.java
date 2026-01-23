package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.task.to.TagTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/tags")
@RequiredArgsConstructor
public class TaskTagController {

    private final TaskService taskService;

    @GetMapping
    public TagTo getTags(@PathVariable Long taskId) {
        return taskService.getTags(taskId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replaceTags(@PathVariable Long taskId,
                            @Valid @RequestBody TagTo tagTo) {
        taskService.replaceTags(taskId, tagTo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTags(@PathVariable Long taskId,
                        @Valid @RequestBody TagTo tagTo) {
        taskService.addTags(taskId, tagTo);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTags(@PathVariable Long taskId) {
        taskService.clearTags(taskId);
    }
}
