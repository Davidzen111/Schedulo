package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private List<Project> projects;
    
    public User() {
        this.projects = new ArrayList<>();
    }
    
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.projects = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }
    
    public void addProject(Project project) {
        this.projects.add(project);
    }
    
    public void removeProject(Project project) {
        this.projects.remove(project);
    }
    
    @Override
    public String toString() {
        return username + "," + password + "," + email;
    }
}
