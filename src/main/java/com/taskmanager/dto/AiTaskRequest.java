package com.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiTaskRequest {

    @NotBlank(message = "Task title is required for AI generation")
    private String title;
}
