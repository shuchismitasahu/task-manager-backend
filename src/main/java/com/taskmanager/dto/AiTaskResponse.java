package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiTaskResponse {

    private String description;
    private String priority;
    private String estimatedTime;
    private boolean aiGenerated;
}
