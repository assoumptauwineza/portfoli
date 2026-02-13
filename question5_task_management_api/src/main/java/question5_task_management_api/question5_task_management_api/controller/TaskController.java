package question5_task_management_api.question5_task_management_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import question5_task_management_api.question5_task_management_api.model.Task;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();

    public TaskController() {
        tasks.add(new Task(1L, "Complete project report", "Finish the final report for the project", false, "HIGH", "2024-12-31"));
        tasks.add(new Task(2L, "Buy groceries", "Get milk, eggs, and bread", false, "LOW", "2024-12-20"));
        tasks.add(new Task(3L, "Team meeting", "Discuss project progress with team", true, "MEDIUM", "2024-12-15"));
        tasks.add(new Task(4L, "Code review", "Review pull requests from team members", false, "HIGH", "2024-12-18"));
        tasks.add(new Task(5L, "Update documentation", "Update API documentation", false, "MEDIUM", "2024-12-25"));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        return tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam boolean completed) {
        List<Task> result = tasks.stream()
                .filter(t -> t.isCompleted() == completed)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority) {
        List<Task> result = tasks.stream()
                .filter(t -> t.getPriority().equalsIgnoreCase(priority))
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskId().equals(taskId)) {
                updatedTask.setTaskId(taskId);
                tasks.set(i, updatedTask);
                return ResponseEntity.ok(updatedTask);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long taskId) {
        for (Task task : tasks) {
            if (task.getTaskId().equals(taskId)) {
                task.setCompleted(true);
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long taskId) {
        Task deleted = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
        if (deleted != null) {
            tasks.remove(deleted);
            return ResponseEntity.ok(deleted);
        }
        return ResponseEntity.notFound().build();
    }
}
