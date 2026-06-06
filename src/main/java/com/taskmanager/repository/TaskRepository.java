package com.taskmanager.repository;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Get all tasks for a specific user
    List<Task> findByUserId(Long userId);

    // Get tasks by user and status
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);

    // Find a specific task by id and user id (security: ensure task belongs to user)
    Optional<Task> findByIdAndUserId(Long id, Long userId);
}
