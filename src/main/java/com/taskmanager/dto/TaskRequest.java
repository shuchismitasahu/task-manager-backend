package com.taskmanager.dto;

import com.taskmanager.entity.enums.Priority;
import com.taskmanager.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private LocalDate dueDate;

    @NotNull(message = "Status is required")
    private TaskStatus status;
}
