package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Project;
import service.ProjectService;
import service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {
    private Stage stage;
    private BorderPane mainLayout;
    private VBox projectContainer;
    private ScrollPane scrollPane;
    
    public DashboardController(Stage stage) {
        this.stage = stage;
    }
    
    public void showDashboard() {
        createDashboardUI();
        loadProjects();
        Scene scene = new Scene(mainLayout, 667, 667);
        stage.setScene(scene);
        stage.setTitle("Schedulo - Pengelola Tugas");
    }
    
    private void createDashboardUI() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        createHeader();
        createMainContent();
        createSidebar();
    }
    
    private void createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");

        Label brand = new Label("📅 Schedulo");
        brand.setFont(Font.font("System", FontWeight.BOLD, 18));
        brand.setTextFill(Color.web("#667eea"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label welcome = new Label("Selamat datang, " + UserService.getCurrentUser().getUsername());
        welcome.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button addBtn = createButton("➕ Baru", "#667eea");
        addBtn.setOnAction(e -> showAddProjectDialog());
        
        Button logoutBtn = createButton("Keluar", "#e53e3e");
        logoutBtn.setOnAction(e -> handleLogout());
        
        header.getChildren().addAll(brand, spacer, welcome, addBtn, logoutBtn);
        mainLayout.setTop(header);
    }
    
    private void createMainContent() {
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Proyek Anda");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        projectContainer = new VBox(15);
        
        content.getChildren().addAll(title, projectContainer);
        scrollPane.setContent(content);
        mainLayout.setCenter(scrollPane);
    }
    
    private void createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(200);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        Label statsTitle = new Label("📊 Statistik");
        statsTitle.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        VBox statsBox = createStatsBox();
        
        sidebar.getChildren().addAll(statsTitle, statsBox);
        mainLayout.setRight(sidebar);
    }
    
    private VBox createStatsBox() {
        VBox stats = new VBox(10);
        stats.setPadding(new Insets(15));
        stats.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        List<Project> projects = ProjectService.getUserProjects(UserService.getCurrentUser().getUsername());
        
        Label total = new Label("Total: " + projects.size());
        total.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        long active = projects.stream().filter(p -> "Aktif".equals(p.getStatus())).count();
        Label activeLabel = new Label("🟢 Aktif: " + active);
        activeLabel.setFont(Font.font("System", 11));
        
        long completed = projects.stream().filter(p -> "Selesai".equals(p.getStatus())).count();
        Label completedLabel = new Label("✅ Selesai: " + completed);
        completedLabel.setFont(Font.font("System", 11));
        
        stats.getChildren().addAll(total, activeLabel, completedLabel);
        return stats;
    }
    
    private void loadProjects() {
        projectContainer.getChildren().clear();
        List<Project> projects = ProjectService.getUserProjects(UserService.getCurrentUser().getUsername());
        
        if (projects.isEmpty()) {
            VBox empty = createEmptyState();
            projectContainer.getChildren().add(empty);
        } else {
            for (Project project : projects) {
                VBox card = createProjectCard(project);
                projectContainer.getChildren().add(card);
            }
        }
    }
    
    private VBox createEmptyState() {
        VBox empty = new VBox(15);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(40));
        empty.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-border-width: 1; -fx-border-style: dashed;");
        
        Label icon = new Label("📋");
        icon.setFont(Font.font("System", 32));
        
        Label text = new Label("Belum ada proyek. Buat proyek pertama Anda!");
        text.setFont(Font.font("System", 14));
        text.setTextFill(Color.web("#666"));
        
        Button createBtn = createButton("🚀 Buat Proyek", "#667eea");
        createBtn.setOnAction(e -> showAddProjectDialog());
        
        empty.getChildren().addAll(icon, text, createBtn);
        return empty;
    }
    
    private VBox createProjectCard(Project project) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-border-width: 1;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label name = new Label(project.getName());
        name.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        String statusEmoji = "Aktif".equals(project.getStatus()) ? "🟢" : 
                           "Selesai".equals(project.getStatus()) ? "✅" : "⏸️";
        Label status = new Label(statusEmoji + " " + project.getStatus());
        status.setFont(Font.font("System", 10));
        status.setPadding(new Insets(2, 6, 2, 6));
        status.setStyle("-fx-background-color: " + getStatusColor(project.getStatus()) + "; -fx-background-radius: 10; -fx-text-fill: white;");
        
        header.getChildren().addAll(name, spacer, status);
        
        // Description
        Label desc = new Label(project.getDescription());
        desc.setFont(Font.font("System", 12));
        desc.setTextFill(Color.web("#666"));
        desc.setWrapText(true);
        
        // Dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");
        Label dates = new Label("📅 " + project.getStartDate().format(formatter) + " - " + project.getEndDate().format(formatter));
        dates.setFont(Font.font("System", 11));
        dates.setTextFill(Color.web("#888"));
        
        // Tasks count
        Label tasks = new Label("📋 " + project.getTasks().size() + " tugas");
        tasks.setFont(Font.font("System", 11));
        tasks.setTextFill(Color.web("#667eea"));

        HBox actions = new HBox(8);
        
        Button editBtn = createSmallButton("✏️ Edit", "#667eea");
        editBtn.setOnAction(e -> showEditProjectDialog(project));
        
        Button deleteBtn = createSmallButton("🗑️ Hapus", "#e53e3e");
        deleteBtn.setOnAction(e -> confirmDeleteProject(project));
        
        Button toggleBtn = createToggleButton(project);
        
        actions.getChildren().addAll(editBtn, deleteBtn, toggleBtn);

        card.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && !isClickOnButton(e.getTarget())) {
                openProjectTasks(project);
            }
        });
        
        card.getChildren().addAll(header, desc, dates, tasks, actions);
        return card;
    }
    
    private Button createToggleButton(Project project) {
        String currentStatus = project.getStatus();
        String toggleText = "Aktif".equals(currentStatus) ? "✅ Tandai Selesai" : "🔄 Tandai Aktif";
        String toggleColor = "Aktif".equals(currentStatus) ? "#38a169" : "#667eea";
        
        Button toggleBtn = createSmallButton(toggleText, toggleColor);

        toggleBtn.setOnAction(e -> {
            e.consume(); 
            toggleProjectStatus(project);
        });

        toggleBtn.setOnMouseClicked(e -> e.consume());
        
        return toggleBtn;
    }
    
    private void toggleProjectStatus(Project project) {
        try {
            String currentStatus = project.getStatus();
            String newStatus = "Aktif".equals(currentStatus) ? "Selesai" : "Aktif";
            
            System.out.println("=== UBAH STATUS ===");
            System.out.println("Proyek: " + project.getName());
            System.out.println("Dari: " + currentStatus + " -> Ke: " + newStatus);

            project.setStatus(newStatus);

            boolean success = ProjectService.updateProject(project);
            
            if (success) {
                System.out.println("✓ Status berhasil diperbarui!");

                loadProjects();  // Reload project cards

                mainLayout.setRight(null);
                createSidebar();

                String message = "Selesai".equals(newStatus) ? 
                    "✅ Proyek ditandai sebagai selesai!" : 
                    "🔄 Proyek ditandai sebagai aktif!";
                showNotification("Berhasil", message, Alert.AlertType.INFORMATION);
                
            } else {
                System.out.println("✗ Gagal memperbarui status!");
                
                // Rollback the status change
                project.setStatus(currentStatus);
                showNotification("Error", "Gagal memperbarui status proyek!", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error dalam toggleProjectStatus: " + e.getMessage());
            e.printStackTrace();
            showNotification("Error", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean isClickOnButton(Object target) {
        return target instanceof Button || 
               (target instanceof javafx.scene.Node && 
                ((javafx.scene.Node) target).getParent() instanceof Button);
    }
    
    private String getStatusColor(String status) {
        switch (status) {
            case "Aktif": return "#38a169";
            case "Selesai": return "#667eea";
            default: return "#718096";
        }
    }
    
    private Button createButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPadding(new Insets(8, 16, 8, 16));
        btn.setFont(Font.font("System", FontWeight.NORMAL, 12));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 4; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
        return btn;
    }
    
    private Button createSmallButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPadding(new Insets(4, 8, 4, 8));
        btn.setFont(Font.font("System", 10));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 4; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
        return btn;
    }
    
    private void showNotification(String title, String message, Alert.AlertType type) {
        try {
            showCustomNotification(message, type);
        } catch (Exception e) {
            System.out.println("Gagal menampilkan notifikasi: " + e.getMessage());
            // Fallback to console
            System.out.println("NOTIFIKASI: " + title + " - " + message);
        }
    }
    
    private void showCustomNotification(String message, Alert.AlertType type) {
        Stage notificationStage = new Stage();
        notificationStage.initOwner(stage);
        notificationStage.initModality(Modality.NONE);
        notificationStage.setResizable(false);
        notificationStage.setTitle("");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        content.setPrefWidth(300);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 8;");

        String icon = type == Alert.AlertType.INFORMATION ? "✅" : 
                     type == Alert.AlertType.ERROR ? "❌" : "ℹ️";
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("System", 32));

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        messageLabel.setAlignment(Pos.CENTER);

        Button okButton = createButton("OK", "#667eea");
        okButton.setPrefWidth(80);
        okButton.setOnAction(e -> notificationStage.close());
        
        content.getChildren().addAll(iconLabel, messageLabel, okButton);

        Scene scene = new Scene(content);
        scene.setFill(Color.TRANSPARENT);
        
        notificationStage.setScene(scene);
        notificationStage.sizeToScene();
        
        notificationStage.setX(stage.getX() + (stage.getWidth() - 300) / 2);
        notificationStage.setY(stage.getY() + (stage.getHeight() - 150) / 2);
        
        if (type == Alert.AlertType.INFORMATION) {
            javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(3), 
                    e -> notificationStage.close())
            );
            timeline.play();
        }
        
        notificationStage.showAndWait();
    }
    
    private void showAddProjectDialog() {
        ProjectDialogController dialogController = new ProjectDialogController(stage);
        dialogController.showAddProjectDialog(() -> {
            loadProjects();
            // Refresh sidebar
            mainLayout.setRight(null);
            createSidebar();
        });
    }
    
    private void showEditProjectDialog(Project project) {
        ProjectDialogController dialogController = new ProjectDialogController(stage);
        dialogController.showEditProjectDialog(project, () -> {
            loadProjects();
            // Refresh sidebar
            mainLayout.setRight(null);
            createSidebar();
        });
    }
    
    private void confirmDeleteProject(Project project) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hapus Proyek");
        alert.setHeaderText("Hapus \"" + project.getName() + "\"?");
        alert.setContentText("Tindakan ini tidak dapat dibatalkan.");
        alert.initOwner(stage);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ProjectService.deleteProject(project.getId());
                    loadProjects();
                    // Refresh sidebar
                    mainLayout.setRight(null);
                    createSidebar();
                    showNotification("Berhasil", "Proyek berhasil dihapus!", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showNotification("Error", "Gagal menghapus proyek: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }
    
    private void openProjectTasks(Project project) {
        TaskController taskController = new TaskController(stage, this);
        taskController.showTaskView(project);
    }
    
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Keluar");
        alert.setHeaderText("Apakah Anda yakin ingin keluar?");
        alert.initOwner(stage);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                UserService.logout();
                LoginController loginController = new LoginController(stage);
                loginController.showLoginScreen();
            }
        });
    }
}
