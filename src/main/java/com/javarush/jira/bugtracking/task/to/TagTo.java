package com.javarush.jira.bugtracking.task.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagTo {

    @NotNull
    @Size(min = 1, max = 10, message = "Must contain 1-10 tags")
    private Set<@NotBlank @Size(min = 2, max = 50) String> tags;
}
