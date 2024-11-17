package com.medialab.models;

import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Reminder {
    private String message;
    private LocalDate reminderDate;
    private Task relatedTask;
    private ReminderType reminderType;

    @JsonCreator
    public Reminder(@JsonProperty("reminderDate") LocalDate reminderDate,
                    @JsonProperty("relatedTask") Task relatedTask,
                    @JsonProperty("reminderType") ReminderType reminderType,
                    @JsonProperty("message") String message) {
        this.reminderDate = reminderDate;
        this.relatedTask = relatedTask;
        this.reminderType = reminderType;
        this.message = message;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Task getRelatedTask() {
        return relatedTask;
    }

    public void setRelatedTask(Task relatedTask) {
        this.relatedTask = relatedTask;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }

    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reminder reminder = (Reminder) obj;
        return Objects.equals(reminderDate, reminder.reminderDate) &&
                Objects.equals(relatedTask, reminder.relatedTask) &&
                reminderType == reminder.reminderType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reminderDate, relatedTask, reminderType);
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderDate=" + reminderDate +
                ", relatedTask=" + relatedTask.getTitle() +
                ", reminderType=" + reminderType +
                ", message='" + message + '\'' +
                '}';
    }

    // Reminder Types
    public enum ReminderType {
        ONE_DAY_BEFORE,
        ONE_WEEK_BEFORE,
        ONE_MONTH_BEFORE,
        SPECIFIC_DATE
    }
}
