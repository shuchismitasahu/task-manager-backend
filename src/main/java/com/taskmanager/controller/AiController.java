package com.taskmanager.controller;

import com.taskmanager.dto.AiTaskRequest;
import com.taskmanager.dto.AiTaskResponse;
import com.taskmanager.service.AiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    // POST /api/ai/generate — generate task details using AI
    @PostMapping("/generate")
    public ResponseEntity<AiTaskResponse> generateTaskDetails(
            @Valid @RequestBody AiTaskRequest request) {
        AiTaskResponse response = aiService.generateTaskDetails(request.getTitle());
        return ResponseEntity.ok(response);
    }
}
