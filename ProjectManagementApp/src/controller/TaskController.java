package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Project;
import model.Task;
import service.ProjectService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskController {
    private Stage stage;
    private DashboardController dashboardController;
    private BorderPane mainLayout;
    private VBox taskContainer;
    private Project currentProject;
    private Label projectTitleLabel;
    
    public TaskController(Stage stage, DashboardController dashboardController) {
        this.stage = stage;
        this.dashboardController = dashboardController;
    }
    
    public void showTaskView(Project project) {
        this.currentProject = project;
        createTaskUI();
        loadTasks();
        Scene scene = new Scene(mainLayout, 667, 667);
        stage.setScene(scene);
        stage.setTitle("Schedulo - " + project.getName() + " Tugas");
    }
    
    private void createTaskUI() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f8fafc;");
        
        createHeader();
        createMainContent();
        createSidebar();
    }
    
    private void createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-width: 0 0 1 0;"
        );

        Button backBtn = createModernButton("← Kembali ke Dashboard", "#718096");
        backBtn.setOnAction(_ -> dashboardController.showDashboard());
        
        // Project title
        projectTitleLabel = new Label(currentProject.getName() + " - Tugas");
        projectTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        projectTitleLabel.setTextFill(Color.web("#2d3748"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addTaskBtn = createModernButton("+ Tugas Baru", "#667eea");
        addTaskBtn.setOnAction(_ -> showAddTaskDialog());
        
        header.getChildren().addAll(backBtn, projectTitleLabel, spacer, addTaskBtn);
        mainLayout.setTop(header);
    }
    
    private void createMainContent() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f8fafc;");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        
        // Project info card
        VBox projectInfo = createProjectInfoCard();
        
        HBox sectionHeader = new HBox(10);
        sectionHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label tasksTitle = new Label("Daftar Tugas");
        tasksTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        tasksTitle.setTextFill(Color.web("#2d3748"));
        
        sectionHeader.getChildren().add(tasksTitle);
   
        taskContainer = new VBox(12);
        
        content.getChildren().addAll(projectInfo, sectionHeader, taskContainer);
        scrollPane.setContent(content);
        mainLayout.setCenter(scrollPane);
    }
    
    private VBox createProjectInfoCard() {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;"
        );
        
        Label descLabel = new Label(currentProject.getDescription());
        descLabel.setFont(Font.font("System", 14));
        descLabel.setTextFill(Color.web("#4a5568"));
        descLabel.setWrapText(true);
        
        HBox datesBox = new HBox(20);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        
        Label startLabel = new Label("Mulai: " + currentProject.getStartDate().format(formatter));
        startLabel.setFont(Font.font("System", 12));
        startLabel.setTextFill(Color.web("#718096"));
        
        Label endLabel = new Label("Selesai: " + currentProject.getEndDate().format(formatter));
        endLabel.setFont(Font.font("System", 12));
        endLabel.setTextFill(Color.web("#718096"));
        
        datesBox.getChildren().addAll(startLabel, endLabel);
        
        card.getChildren().addAll(descLabel, datesBox);
        return card;
    }
    
    private void createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(250);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-width: 0 1 0 0;"
        );
        
        Label sidebarTitle = new Label("Ringkasan Tugas");
        sidebarTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sidebarTitle.setTextFill(Color.web("#2d3748"));
        
        VBox statsCard = createTaskStatsCard();
        
        sidebar.getChildren().addAll(sidebarTitle, statsCard);
        mainLayout.setLeft(sidebar);
    }
    
    private VBox createTaskStatsCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: #f7fafc;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-radius: 10;"
        );
        
        List<Task> tasks = currentProject.getTasks();
        
        Label totalTasks = new Label("Total Tugas: " + tasks.size());
        totalTasks.setFont(Font.font("System", FontWeight.BOLD, 14));
        totalTasks.setTextFill(Color.web("#4a5568"));
        
        long completedTasks = tasks.stream().filter(t -> "Selesai".equals(t.getStatus())).count();
        Label completedLabel = new Label("Selesai: " + completedTasks);
        completedLabel.setFont(Font.font("System", 12));
        completedLabel.setTextFill(Color.web("#38a169"));
        
        long pendingTasks = tasks.size() - completedTasks;
        Label pendingLabel = new Label("Belum Selesai: " + pendingTasks);
        pendingLabel.setFont(Font.font("System", 12));
        pendingLabel.setTextFill(Color.web("#e53e3e"));
        
        card.getChildren().addAll(totalTasks, completedLabel, pendingLabel);
        return card;
    }
    
    private void loadTasks() {
        taskContainer.getChildren().clear();
        
        // Refresh project data to get latest tasks
        currentProject = ProjectService.getProjectById(currentProject.getId());
        
        List<Task> tasks = currentProject.getTasks();
        
        if (tasks.isEmpty()) {
            Label emptyLabel = new Label("Belum ada tugas. Buat tugas pertama Anda!");
            emptyLabel.setFont(Font.font("System", 16));
            emptyLabel.setTextFill(Color.web("#718096"));
            taskContainer.getChildren().add(emptyLabel);
        } else {
            for (Task task : tasks) {
                taskContainer.getChildren().add(createTaskCard(task));
            }
        }

        createSidebar();
    }
    
    private HBox createTaskCard(Task task) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 2, 0, 0, 1);"
        );

        CheckBox statusCheckBox = new CheckBox();
        statusCheckBox.setSelected("Selesai".equals(task.getStatus()));
        statusCheckBox.setOnAction(_ -> {
            String newStatus = statusCheckBox.isSelected() ? "Selesai" : "Belum Selesai";
            ProjectService.updateTaskStatus(task.getId(), newStatus);
            loadTasks(); // Refresh to update stats
        });

        VBox taskDetails = new VBox(5);
        HBox.setHgrow(taskDetails, Priority.ALWAYS);
        
        Label nameLabel = new Label(task.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#2d3748"));
        
        if ("Selesai".equals(task.getStatus())) {
            nameLabel.setStyle("-fx-strikethrough: true;");
            nameLabel.setTextFill(Color.web("#718096"));
        }
        
        Label descLabel = new Label(task.getDescription());
        descLabel.setFont(Font.font("System", 12));
        descLabel.setTextFill(Color.web("#4a5568"));
        descLabel.setWrapText(true);
        
        HBox taskMeta = new HBox(15);
        taskMeta.setAlignment(Pos.CENTER_LEFT);

        String priorityText = task.getPriority();
        switch (task.getPriority().toLowerCase()) {
            case "high":
                priorityText = "Tinggi";
                break;
            case "medium":
                priorityText = "Sedang";
                break;
            case "low":
                priorityText = "Rendah";
                break;
        }
        
        Label priorityBadge = new Label(priorityText);
        priorityBadge.setPadding(new Insets(3, 8, 3, 8));
        priorityBadge.setFont(Font.font("System", FontWeight.BOLD, 10));
        priorityBadge.setTextFill(Color.WHITE);
        
        String priorityColor = "#718096"; 
        switch (task.getPriority().toLowerCase()) {
            case "high":
                priorityColor = "#e53e3e";
                break;
            case "medium":
                priorityColor = "#d69e2e";
                break;
            case "low":
                priorityColor = "#38a169";
                break;
        }
        
        priorityBadge.setStyle(
            "-fx-background-color: " + priorityColor + ";" +
            "-fx-background-radius: 10;"
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        Label dueDateLabel = new Label("Deadline: " + task.getDueDate().format(formatter));
        dueDateLabel.setFont(Font.font("System", 11));
        dueDateLabel.setTextFill(Color.web("#718096"));
        
        taskMeta.getChildren().addAll(priorityBadge, dueDateLabel);
        
        taskDetails.getChildren().addAll(nameLabel, descLabel, taskMeta);

        VBox actionButtons = new VBox(5);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        Button editBtn = createSmallButton("Edit", "#38a169");
        editBtn.setOnAction(_ -> showEditTaskDialog(task));
        
        Button deleteBtn = createSmallButton("Hapus", "#e53e3e");
        deleteBtn.setOnAction(_ -> confirmDeleteTask(task));
        
        actionButtons.getChildren().addAll(editBtn, deleteBtn);
        
        card.getChildren().addAll(statusCheckBox, taskDetails, actionButtons);

        card.setOnMouseEntered(_ -> card.setStyle(card.getStyle() + "-fx-border-color: #667eea;"));
        card.setOnMouseExited(_ -> card.setStyle(card.getStyle().replace("-fx-border-color: #667eea;", "-fx-border-color: #e2e8f0;")));
        
        return card;
    }
    
    private Button createModernButton(String text, String color) {
        Button button = new Button(text);
        button.setPadding(new Insets(10, 20, 10, 20));
        button.setFont(Font.font("System", FontWeight.BOLD, 12));
        button.setTextFill(Color.WHITE);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(_ -> button.setOpacity(0.8));
        button.setOnMouseExited(_ -> button.setOpacity(1.0));
        
        return button;
    }
    
    private Button createSmallButton(String text, String color) {
        Button button = new Button(text);
        button.setPadding(new Insets(4, 8, 4, 8));
        button.setFont(Font.font("System", 10));
        button.setTextFill(Color.WHITE);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(_ -> button.setOpacity(0.8));
        button.setOnMouseExited(_ -> button.setOpacity(1.0));
        
        return button;
    }
    
    private void showAddTaskDialog() {
        TaskDialogController dialogController = new TaskDialogController(stage);
        dialogController.showAddTaskDialog(currentProject, this::loadTasks);
    }
    
    private void showEditTaskDialog(Task task) {
        TaskDialogController dialogController = new TaskDialogController(stage);
        // FIXED: Added currentProject as the second parameter
        dialogController.showEditTaskDialog(task, currentProject, this::loadTasks);
    }
    
    private void confirmDeleteTask(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hapus Tugas");
        alert.setHeaderText("Apakah Anda yakin ingin menghapus tugas ini?");
        alert.setContentText("Ini akan menghapus tugas '" + task.getName() + "'. Tindakan ini tidak dapat dibatalkan.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ProjectService.deleteTask(task.getId());
                loadTasks();
            }
        });
    }
}
