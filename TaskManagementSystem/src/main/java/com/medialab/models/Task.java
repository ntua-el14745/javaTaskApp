package com.medialab.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    // private String title;
    // private String description;
    private Category category;
    private Priority priority;
    private LocalDate deadline;
    private TaskStatus status;
    private List<Reminder> reminders;
    private StringProperty title = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
     // Constructor for Jackson
     @JsonCreator
     public Task(@JsonProperty("title") String title, 
                 @JsonProperty("description") String description, 
                 @JsonProperty("category") Category category, 
                 @JsonProperty("priority") Priority priority, 
                 @JsonProperty("deadline") LocalDate deadline) {
         this.title.set(title);
         this.description.set(description);
         this.category = category;
         this.priority = priority;
         this.deadline = deadline;
         this.status = TaskStatus.OPEN;
         this.reminders = new ArrayList<>(); 
     }
    public String getTitle() {
        return title.get();
    }
    public StringProperty getTitleProp() {
        return title;
    }
    public StringProperty getDescriptionProp() {
        return description;
    }
    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);    
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    // This is just an example to get a reminder date from the first reminder
    public LocalDate getReminderDate() {
        if (reminders != null && !reminders.isEmpty()) {
            return reminders.get(0).getReminderDate(); // Assuming Reminder has a getReminderDate method
        }
        return null; // Return null or an appropriate default
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", deadline=" + deadline +
                ", status=" + status +
                '}';
    }

   
}
