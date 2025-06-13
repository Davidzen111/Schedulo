package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project extends BaseModel {
    private String id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String ownerUsername;
    private List<Task> tasks;
    
    public Project() {
        super();
        this.tasks = new ArrayList<>();
        this.status = "Active";
    }
    
    public Project(String id, String name, String description, LocalDate startDate, LocalDate endDate, String ownerUsername) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ownerUsername = ownerUsername;
        this.status = "Active";
        this.tasks = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
    
    public void addTask(Task task) {
        this.tasks.add(task);
    }
    
    public void removeTask(Task task) {
        this.tasks.remove(task);
    }
    
    @Override
    public String toString() {
        return id + "," + name + "," + description + "," + startDate + "," + endDate + "," + status + "," + ownerUsername;
    }
}