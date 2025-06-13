
package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Project;
import model.Task;
import service.ProjectService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TaskDialogController {
    private Stage parentStage;
    private Stage dialogStage;
    private TextField nameField;
    private TextArea descriptionArea;
    private ComboBox<String> priorityComboBox;
    private DatePicker dueDatePicker;
    private Label statusLabel;
    private Task editingTask;
    private Project currentProject;
    private Runnable onSuccess;

    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
    
    public TaskDialogController(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void setCurrentProject(Project project) {
        this.currentProject = project;
    }
    
    public void showAddTaskDialog(Project project, Runnable onSuccess) {
        this.currentProject = project;
        this.onSuccess = onSuccess;
        this.editingTask = null;
        createDialog("Tambah Tugas Baru", "Buat Tugas");
    }
    
    public void showEditTaskDialog(Task task, Project project, Runnable onSuccess) {
        this.onSuccess = onSuccess;
        this.editingTask = task;
        this.currentProject = project;
        createDialog("Edit Tugas", "Perbarui Tugas");
        fillFields(task);
    }

    public void showEditTaskDialog(Task task, Runnable onSuccess) {
        this.onSuccess = onSuccess;
        this.editingTask = task;
        createDialog("Edit Tugas", "Perbarui Tugas");
        fillFields(task);
    }
    
    private void createDialog(String title, String buttonText) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle(title);
        dialogStage.setResizable(false);

        VBox mainContainer = new VBox(25);
        mainContainer.setPadding(new Insets(35));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f8fafc, #e2e8f0);" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        HBox titleContainer = new HBox(10);
        titleContainer.setAlignment(Pos.CENTER);
        
        Label iconLabel = new Label("📋");
        iconLabel.setFont(Font.font(28));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1a365d"));
        
        titleContainer.getChildren().addAll(iconLabel, titleLabel);
        

        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER_LEFT);
        formContainer.setPadding(new Insets(25));
        formContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 12px;" +
            "-fx-background-radius: 12px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);"
        );

        VBox nameContainer = new VBox(8);
        Label nameLabel = new Label("📝 Nama Tugas");
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.web("#2d3748"));
        
        nameField = createStyledTextField("Masukkan nama tugas...");
        nameContainer.getChildren().addAll(nameLabel, nameField);

        VBox descContainer = new VBox(8);
        Label descLabel = new Label("📄 Deskripsi");
        descLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        descLabel.setTextFill(Color.web("#2d3748"));
        
        descriptionArea = createStyledTextArea("Masukkan deskripsi tugas...");
        descContainer.getChildren().addAll(descLabel, descriptionArea);

        HBox priorityDateRow = new HBox(20);

        VBox priorityContainer = new VBox(8);
        Label priorityLabel = new Label("⚡ Prioritas");
        priorityLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        priorityLabel.setTextFill(Color.web("#2d3748"));
        
        priorityComboBox = createStyledComboBox();
        priorityComboBox.getItems().addAll("Rendah", "Sedang", "Tinggi");
        priorityComboBox.setValue("Sedang");
        
        priorityContainer.getChildren().addAll(priorityLabel, priorityComboBox);

        VBox dueDateContainer = new VBox(8);
        Label dueDateLabel = new Label("📅 Tanggal Jatuh Tempo");
        dueDateLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        dueDateLabel.setTextFill(Color.web("#2d3748"));
        
        dueDatePicker = createStyledDatePicker();
        dueDatePicker.setValue(LocalDate.now().plusDays(7));

        dueDatePicker.setConverter(new javafx.util.StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.format(DATE_FORMATTER) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.trim().isEmpty()) {
                    try {
                        return LocalDate.parse(string, DATE_FORMATTER);
                    } catch (Exception e) {
                        return LocalDate.parse(string);
                    }
                }
                return null;
            }
        });
        
        dueDateContainer.getChildren().addAll(dueDateLabel, dueDatePicker);
        
        priorityDateRow.getChildren().addAll(priorityContainer, dueDateContainer);

        statusLabel = new Label();
        statusLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        statusLabel.setWrapText(true);
        
        formContainer.getChildren().addAll(
            nameContainer,
            descContainer,
            priorityDateRow,
            statusLabel
        );

        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(10, 0, 0, 0));
        
        Button saveButton = createPrimaryButton(buttonText);
        saveButton.setOnAction(e -> handleSave());
        
        Button cancelButton = createSecondaryButton("Batal");
        cancelButton.setOnAction(e -> dialogStage.close());
        
        buttonContainer.getChildren().addAll(saveButton, cancelButton);
        
        mainContainer.getChildren().addAll(titleContainer, formContainer, buttonContainer);

        Scene scene = new Scene(mainContainer, 500, 580);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    if (event.isControlDown()) {
                        handleSave();
                    }
                    break;
                case ESCAPE:
                    dialogStage.close();
                    break;
            }
        });
        
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
    
    private TextField createStyledTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(45);
        field.setPrefWidth(420);
        field.setStyle(
            "-fx-background-color: #f8fafc;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-width: 1.5px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-padding: 10px 15px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Segoe UI';"
        );
        
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #3b82f6;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-padding: 9px 14px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-effect: dropshadow(gaussian, rgba(59,130,246,0.3), 4, 0, 0, 1);"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: #f8fafc;" +
                    "-fx-border-color: #d1d5db;" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-padding: 10px 15px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-family: 'Segoe UI';"
                );
            }
        });
        
        return field;
    }
    
    private TextArea createStyledTextArea(String placeholder) {
        TextArea area = new TextArea();
        area.setPromptText(placeholder);
        area.setPrefHeight(90);
        area.setPrefWidth(420);
        area.setWrapText(true);
        area.setStyle(
            "-fx-background-color: #f8fafc;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-width: 1.5px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-padding: 10px 15px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Segoe UI';"
        );
        
        area.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                area.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #3b82f6;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-padding: 9px 14px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-effect: dropshadow(gaussian, rgba(59,130,246,0.3), 4, 0, 0, 1);"
                );
            } else {
                area.setStyle(
                    "-fx-background-color: #f8fafc;" +
                    "-fx-border-color: #d1d5db;" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-padding: 10px 15px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-family: 'Segoe UI';"
                );
            }
        });
        
        return area;
    }
    
    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefHeight(45);
        comboBox.setPrefWidth(190);
        comboBox.setStyle(
            "-fx-background-color: #f8fafc;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-width: 1.5px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Segoe UI';"
        );
        
        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBox.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #3b82f6;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-effect: dropshadow(gaussian, rgba(59,130,246,0.3), 4, 0, 0, 1);"
                );
            } else {
                comboBox.setStyle(
                    "-fx-background-color: #f8fafc;" +
                    "-fx-border-color: #d1d5db;" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-family: 'Segoe UI';"
                );
            }
        });
        
        return comboBox;
    }
    
    private DatePicker createStyledDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefHeight(45);
        datePicker.setPrefWidth(210);
        datePicker.setStyle(
            "-fx-background-color: #f8fafc;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-width: 1.5px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Segoe UI';"
        );
        
        datePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                datePicker.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #3b82f6;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-effect: dropshadow(gaussian, rgba(59,130,246,0.3), 4, 0, 0, 1);"
                );
            } else {
                datePicker.setStyle(
                    "-fx-background-color: #f8fafc;" +
                    "-fx-border-color: #d1d5db;" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-family: 'Segoe UI';"
                );
            }
        });
        
        return datePicker;
    }
    
    private Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(140);
        button.setPrefHeight(45);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        button.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #3b82f6, #2563eb);" +
            "-fx-text-fill: white;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.3), 4, 0, 0, 2);"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2563eb, #1d4ed8);" +
            "-fx-text-fill: white;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(29,78,216,0.4), 6, 0, 0, 3);" +
            "-fx-scale-y: 1.05;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #3b82f6, #2563eb);" +
            "-fx-text-fill: white;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.3), 4, 0, 0, 2);" +
            "-fx-scale-y: 1;"
        ));
        
        return button;
    }
    
    private Button createSecondaryButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(140);
        button.setPrefHeight(45);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        button.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f3f4f6, #e5e7eb);" +
            "-fx-text-fill: #374151;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 2, 0, 0, 1);"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #e5e7eb, #d1d5db);" +
            "-fx-text-fill: #374151;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 3, 0, 0, 2);" +
            "-fx-scale-y: 1.05;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f3f4f6, #e5e7eb);" +
            "-fx-text-fill: #374151;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 2, 0, 0, 1);" +
            "-fx-scale-y: 1;"
        ));
        
        return button;
    }
    
    private void fillFields(Task task) {
        if (task != null) {
            nameField.setText(task.getName());
            descriptionArea.setText(task.getDescription());
            
            // Map English priority to Indonesian
            String priority = task.getPriority();
            switch (priority.toLowerCase()) {
                case "low":
                    priorityComboBox.setValue("Rendah");
                    break;
                case "medium":
                    priorityComboBox.setValue("Sedang");
                    break;
                case "high":
                    priorityComboBox.setValue("Tinggi");
                    break;
                default:
                    priorityComboBox.setValue("Sedang");
                    break;
            }
            
            dueDatePicker.setValue(task.getDueDate());
        }
    }

    private void handleSave() {
        statusLabel.setText("");

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setText("❌ Nama tugas harus diisi");
            return;
        }
        
        String description = descriptionArea.getText().trim();
        String priorityIndonesian = priorityComboBox.getValue();

        String priority;
        switch (priorityIndonesian) {
            case "Rendah":
                priority = "Low";
                break;
            case "Tinggi":
                priority = "High";
                break;
            default:
                priority = "Medium";
                break;
        }
        
        LocalDate dueDate = dueDatePicker.getValue();
        
        if (dueDate == null) {
            statusLabel.setText("❌ Tanggal jatuh tempo harus diisi");
            return;
        }
        
        if (dueDate.isBefore(LocalDate.now())) {
            statusLabel.setText("❌ Tanggal jatuh tempo tidak boleh di masa lalu");
            return;
        }

        if (currentProject == null) {
            statusLabel.setText("❌ Error: Tidak ada proyek yang dipilih");
            return;
        }
        
        try {
            if (editingTask == null) {
                String taskId = ProjectService.createTask(
                    name, 
                    description, 
                    priority, 
                    dueDate, 
                    currentProject.getId()  // Pastikan projectId di-set
                );
                
                System.out.println("Task created successfully with ID: " + taskId); // Debug log
                
            } else {
                editingTask.setName(name);
                editingTask.setDescription(description);
                editingTask.setPriority(priority);
                editingTask.setDueDate(dueDate);
                
                boolean updated = ProjectService.updateTask(editingTask);
                if (!updated) {
                    statusLabel.setText("❌ Error: Gagal memperbarui tugas");
                    return;
                }
                
                System.out.println("Task updated successfully: " + editingTask.getId()); // Debug log
            }

            statusLabel.setTextFill(Color.web("#059669"));
            statusLabel.setText("✅ Tugas berhasil disimpan!");

            new Thread(() -> {
                try {
                    Thread.sleep(800); 
                    javafx.application.Platform.runLater(() -> {
                        dialogStage.close();
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
            
        } catch (Exception e) {
            statusLabel.setText("❌ Error menyimpan tugas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}