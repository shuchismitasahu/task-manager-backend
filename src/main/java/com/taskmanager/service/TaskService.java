package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.TaskStatus;
import com.taskmanager.exception.BadRequestException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all tasks for the logged-in user
    public List<TaskResponse> getAllTasks(String username) {
        User user = getUser(username);
        return taskRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get a single task by ID (only if it belongs to the user)
    public TaskResponse getTaskById(Long taskId, String username) {
        User user = getUser(username);
        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task not found with id: " + taskId));
        return mapToResponse(task);
    }

    // Create a new task
    public TaskResponse createTask(TaskRequest request, String username) {
        User user = getUser(username);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    // Update an existing task
    public TaskResponse updateTask(Long taskId, TaskRequest request, String username) {
        User user = getUser(username);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task not found with id: " + taskId));

        // Update fields
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    // Delete a task
    public void deleteTask(Long taskId, String username) {
        User user = getUser(username);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task not found with id: " + taskId));

        taskRepository.delete(task);
    }

    // Update only the status of a task
    public TaskResponse updateTaskStatus(Long taskId, String statusStr, String username) {
        User user = getUser(username);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task not found with id: " + taskId));

        try {
            TaskStatus newStatus = TaskStatus.valueOf(statusStr.toUpperCase());
            task.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + statusStr +
                    ". Valid values are: TODO, IN_PROGRESS, DONE");
        }

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    // Helper: get User entity from username
    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));
    }

    // Helper: map Task entity to TaskResponse DTO
    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setUsername(task.getUser().getUsername());
        return response;
    }
}
