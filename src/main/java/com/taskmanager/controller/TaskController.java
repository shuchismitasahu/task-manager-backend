package com.taskmanager.controller;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // GET /api/tasks — get all tasks for logged-in user
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<TaskResponse> tasks = taskService.getAllTasks(userDetails.getUsername());
        return ResponseEntity.ok(tasks);
    }

    // GET /api/tasks/{id} — get single task by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskResponse task = taskService.getTaskById(id, userDetails.getUsername());
        return ResponseEntity.ok(task);
    }

    // POST /api/tasks — create a new task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskResponse task = taskService.createTask(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    // PUT /api/tasks/{id} — update a task
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskResponse task = taskService.updateTask(id, request, userDetails.getUsername());
        return ResponseEntity.ok(task);
    }

    // DELETE /api/tasks/{id} — delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        taskService.deleteTask(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
    }

    // PATCH /api/tasks/{id}/status — update only the status
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        String status = body.get("status");
        TaskResponse task = taskService.updateTaskStatus(id, status, userDetails.getUsername());
        return ResponseEntity.ok(task);
    }
}
