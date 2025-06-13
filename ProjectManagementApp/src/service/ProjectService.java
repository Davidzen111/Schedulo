package service;

import model.Project;
import model.Task;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProjectService {
    private static final String PROJECT_FILE = "ProjectManagementApp/src/data/rojects.txt";
    private static final String TASK_FILE = "ProjectManagementApp/src/data/tasks.txt";
    
    public static String createProject(String name, String description, LocalDate startDate, LocalDate endDate, String ownerUsername) {
        String id = UUID.randomUUID().toString();
        Project project = new Project(id, name, description, startDate, endDate, ownerUsername);
        saveProject(project);
        return id;
    }
    
    public static List<Project> getUserProjects(String username) {
        List<Project> allProjects = loadProjects();
        List<Project> userProjects = new ArrayList<>();
        
        for (Project project : allProjects) {
            if (project.getOwnerUsername().equals(username)) {
                project.setTasks(getProjectTasks(project.getId()));
                userProjects.add(project);
            }
        }
        
        return userProjects;
    }
    
    public static Project getProjectById(String projectId) {
        List<Project> projects = loadProjects();
        for (Project project : projects) {
            if (project.getId().equals(projectId)) {
                project.setTasks(getProjectTasks(projectId));
                return project;
            }
        }
        return null;
    }
    
    public static boolean deleteProject(String projectId) {
        List<Project> projects = loadProjects();
        projects.removeIf(p -> p.getId().equals(projectId));
        saveAllProjects(projects);
        
        // Also delete all tasks for this project
        deleteAllTasksForProject(projectId);
        return true;
    }

    public static String createTask(String name, String description, String priority, LocalDate dueDate, String projectId) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, name, description, priority, dueDate, projectId);
        task.setStatus("Belum Selesai"); // Set default status
        saveTask(task);
        System.out.println("Task saved to file: " + task.getName() + " for project: " + projectId); // Debug log
        return id;
    }

    public static boolean updateTask(Task updatedTask) {
        List<Task> tasks = loadTasks();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getId().equals(updatedTask.getId())) {
                tasks.set(i, updatedTask);
                saveAllTasks(tasks);
                System.out.println("Task updated in file: " + updatedTask.getName()); // Debug log
                return true;
            }
        }
        return false;
    }
    
    public static List<Task> getProjectTasks(String projectId) {
        List<Task> allTasks = loadTasks();
        List<Task> projectTasks = new ArrayList<>();
        
        for (Task task : allTasks) {
            if (task.getProjectId().equals(projectId)) {
                projectTasks.add(task);
            }
        }
        
        System.out.println("Loaded " + projectTasks.size() + " tasks for project: " + projectId); // Debug log
        return projectTasks;
    }
    
    public static boolean updateTaskStatus(String taskId, String status) {
        List<Task> tasks = loadTasks();
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                task.setStatus(status);
                saveAllTasks(tasks);
                return true;
            }
        }
        return false;
    }
    
    public static boolean deleteTask(String taskId) {
        List<Task> tasks = loadTasks();
        tasks.removeIf(t -> t.getId().equals(taskId));
        saveAllTasks(tasks);
        return true;
    }
    
    private static void deleteAllTasksForProject(String projectId) {
        List<Task> tasks = loadTasks();
        tasks.removeIf(t -> t.getProjectId().equals(projectId));
        saveAllTasks(tasks);
    }

    private static void saveProject(Project project) {
        try (FileWriter fw = new FileWriter(PROJECT_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(project.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void saveAllProjects(List<Project> projects) {
        try (FileWriter fw = new FileWriter(PROJECT_FILE);
             PrintWriter pw = new PrintWriter(fw)) {
            for (Project project : projects) {
                pw.println(project.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static List<Project> loadProjects() {
        List<Project> projects = new ArrayList<>();
        File file = new File(PROJECT_FILE);
        
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return projects;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    Project project = new Project();
                    project.setId(data[0]);
                    project.setName(data[1]);
                    project.setDescription(data[2]);
                    project.setStartDate(LocalDate.parse(data[3]));
                    project.setEndDate(LocalDate.parse(data[4]));
                    project.setStatus(data[5]);
                    project.setOwnerUsername(data[6]);
                    projects.add(project);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return projects;
    }
    
    // File I/O methods for Tasks
    private static void saveTask(Task task) {
        try (FileWriter fw = new FileWriter(TASK_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(task.toString());
            System.out.println("Task written to file: " + task.toString()); // Debug log
        } catch (IOException e) {
            System.err.println("Error saving task to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void saveAllTasks(List<Task> tasks) {
        try (FileWriter fw = new FileWriter(TASK_FILE);
             PrintWriter pw = new PrintWriter(fw)) {
            for (Task task : tasks) {
                pw.println(task.toString());
            }
            System.out.println("All tasks saved to file. Total: " + tasks.size()); // Debug log
        } catch (IOException e) {
            System.err.println("Error saving all tasks to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

public static boolean updateProject(Project updatedProject) {
    try {
        List<Project> projects = loadProjects();
        boolean found = false;
        
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            if (project.getId().equals(updatedProject.getId())) {
                projects.set(i, updatedProject);
                found = true;
                break;
            }
        }
        
        if (found) {
            saveAllProjects(projects);
            System.out.println("✓ Project updated successfully: " + updatedProject.getName() + " - Status: " + updatedProject.getStatus());
            return true;
        } else {
            System.out.println("✗ Project not found for update: " + updatedProject.getId());
            return false;
        }
        
    } catch (Exception e) {
        System.err.println("✗ Error updating project: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

    public static boolean updateProjectStatus(String projectId, String newStatus) {
        try {
            List<Project> projects = loadProjects();
            boolean found = false;
            
            for (Project project : projects) {
                if (project.getId().equals(projectId)) {
                    project.setStatus(newStatus);
                    found = true;
                    break;
                }
            }
            
            if (found) {
                saveAllProjects(projects);
                System.out.println("✓ Project status updated: " + projectId + " -> " + newStatus);
                return true;
            } else {
                System.out.println("✗ Project not found for status update: " + projectId);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error updating project status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all projects (helper method)
     */
    public static List<Project> getAllProjects() {
        return loadProjects();
    }
    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(TASK_FILE);
        
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("Created new task file: " + TASK_FILE);
            } catch (IOException e) {
                System.err.println("Error creating task file: " + e.getMessage());
                e.printStackTrace();
            }
            return tasks;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Skip empty lines
                    String[] data = line.split(",");
                    if (data.length >= 7) {
                        Task task = new Task();
                        task.setId(data[0]);
                        task.setName(data[1]);
                        task.setDescription(data[2]);
                        task.setStatus(data[3]);
                        task.setPriority(data[4]);
                        task.setDueDate(LocalDate.parse(data[5]));
                        task.setProjectId(data[6]);
                        tasks.add(task);
                    }
                }
            }
            System.out.println("Loaded " + tasks.size() + " tasks from file"); // Debug log
        } catch (IOException e) {
            System.err.println("Error loading tasks from file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tasks;
    }
}