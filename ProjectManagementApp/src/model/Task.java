package model;

import java.time.LocalDate;

public class Task extends BaseModel {
    private String id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private String projectId;
    
    public Task() {
        super();
        this.status = "Belum Selesai";
        this.priority = "Medium";
    }
    
    public Task(String id, String name, String description, String priority, LocalDate dueDate, String projectId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.status = "Belum Selesai";
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    
    public void markAsCompleted() {
        this.status = "Selesai";
    }
    
    public void markAsIncomplete() {
        this.status = "Belum Selesai";
    }
    
    @Override
    public String toString() {
        return id + "," + name + "," + description + "," + status + "," + priority + "," + dueDate + "," + projectId;
    }
}
