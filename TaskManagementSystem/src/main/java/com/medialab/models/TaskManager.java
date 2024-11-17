package com.medialab.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TaskManager {
    private List<Task> tasks;
    private List<Reminder> reminders; 
    private static final String TASKS_FILE = "tasks.json";
    private static final String REMINDERS_FILE = "reminders.json";
    private ObjectMapper objectMapper;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.reminders = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 
        // System.out.println("Before loading: Tasks size: " + tasks.size() + ", Reminders size: " + reminders.size());
        loadTasks();
        loadReminders();
        // System.out.println("After loading: Tasks size: " + tasks.size() + ", Reminders size: " + reminders.size());
    }
        // Save tasks to a JSON file
        public void saveTasks() {
            try {
                objectMapper.writeValue(new File(TASKS_FILE), tasks);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
        // Load tasks from a JSON file
        public void loadTasks() {
            try {
                File file = new File(TASKS_FILE);
                if (file.exists() && file.length() > 0) { 
                    tasks = objectMapper.readValue(file, new TypeReference<List<Task>>() {});
                } else {
                    tasks = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                tasks = new ArrayList<>();
            }
        }
    
        // Save reminders to a JSON file
        public void saveReminders() {
            try {
                // System.out.println("inside savereminders to task " + reminders);
                objectMapper.writeValue(new File(REMINDERS_FILE), reminders);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
        // Load reminders from a JSON file
        public void loadReminders() {
            // System.out.println("Loading reminders from file...");
            try {
                File file = new File(REMINDERS_FILE);
                if (file.exists() && file.length() > 0) { 
                    reminders = objectMapper.readValue(file, new TypeReference<List<Reminder>>() {});
                    for (Reminder reminder : reminders) {
                        Task relatedTask = getTaskByTitle(reminder.getRelatedTask().getTitle());
                        if (relatedTask != null) {
                            reminder.setRelatedTask(relatedTask);  // Set the related task
                        }
                    }
                } else {
                    // System.out.println("Reminders file does not exist or is empty, initializing an empty list.");
                    reminders = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                reminders = new ArrayList<>();
                System.out.println("IOException occurred, initializing an empty list.");
            }
        }
    
    // Add a new task
    public void addTask(Task task) {
        tasks.add(task);
    }

    // Remove a task
    public void removeTask(Task task) {
        tasks.remove(task);
    }

    // Update a task's status or any other attribute
    public void updateTask(Task task, String title, String description, Priority priority, LocalDate dueDate) {
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDeadline(dueDate);
    }


    // Get all tasks
    public List<Task> getAllTasks() {
        return tasks;
    }

    // Get tasks by a specific status
    public List<Task> getTasksByStatus(TaskStatus status) {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    // Get tasks by category
    public List<Task> getTasksByCategory(String categoryName) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getCategory().getName().equalsIgnoreCase(categoryName)) {
                result.add(task);
            }
        }
        return result;
    }
    
    // Get tasks by priority
    public List<Task> getTasksByPriority(Priority priority) {
        return tasks.stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    // Get tasks with upcoming reminders
    public List<Task> getTasksWithUpcomingReminders() {
        return tasks.stream()
                .filter(task -> task.getReminders().stream()
                        .anyMatch(reminder -> reminder.getReminderDate().isAfter(LocalDate.now())))
                .collect(Collectors.toList());
    }

    // Get task by title
    public Task getTaskByTitle(String title) {
        return tasks.stream()
                .filter(task -> task.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    // Display all tasks (for testing purposes)
    public void displayAllTasks() {
        tasks.forEach(task -> System.out.println(task));
    }

    // Method to mark a task as completed
    public void markTaskAsCompleted(Task task) {
        task.setStatus(TaskStatus.COMPLETED);
    }

    // Method to update task priority
    public void updateTaskPriority(Task task, Priority newPriority) {
        task.setPriority(newPriority);
    }

    // Method to add a reminder to a task
    public void addReminderToTask(Task task, Reminder reminder) {
        task.addReminder(reminder);
        reminders.add(reminder);
    }

    // Method to remove a reminder from a task
    public void removeReminderFromTask(Reminder reminder) {
        reminders.remove(reminder);
    }

      // Method to get all reminders for a task
      public List<Reminder> getRemindersForTask(Task task) {
        return reminders.stream()
                .filter(reminder -> reminder.getRelatedTask().equals(task))
                .collect(Collectors.toList());
    }

    // Method to get all reminders
    public List<Reminder> getAllReminders() {
        return reminders;
    }
}
