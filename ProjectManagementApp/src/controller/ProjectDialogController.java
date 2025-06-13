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
import service.ProjectService;
import service.UserService;

import java.time.LocalDate;

public class ProjectDialogController {
    private Stage parentStage;
    private Stage dialogStage;
    private TextField nameField;
    private TextArea descriptionArea;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Label statusLabel;
    private Project editingProject;
    private Runnable onSuccess;

    public ProjectDialogController(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void showAddProjectDialog(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        this.editingProject = null;
        createDialog("Tambah Proyek Baru", "Buat");
    }

    public void showEditProjectDialog(Project project, Runnable onSuccess) {
        this.onSuccess = onSuccess;
        this.editingProject = project;
        createDialog("Edit Proyek", "Perbarui");
        fillFields(project);
    }

    private void createDialog(String title, String buttonText) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle(title);
        dialogStage.setResizable(false);

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2d3748"));

        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label("Nama Proyek");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.web("#4a5568"));

        nameField = createStyledTextField("Masukkan nama proyek");

        Label descLabel = new Label("Deskripsi");
        descLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        descLabel.setTextFill(Color.web("#4a5568"));

        descriptionArea = createStyledTextArea("Masukkan deskripsi proyek");

        HBox dateContainer = new HBox(15);
        dateContainer.setAlignment(Pos.CENTER_LEFT);

        VBox startDateContainer = new VBox(5);
        Label startLabel = new Label("Tanggal Mulai");
        startLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        startLabel.setTextFill(Color.web("#4a5568"));
        startDatePicker = createStyledDatePicker();
        startDatePicker.setValue(LocalDate.now());
        startDateContainer.getChildren().addAll(startLabel, startDatePicker);

        VBox endDateContainer = new VBox(5);
        Label endLabel = new Label("Tanggal Selesai");
        endLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        endLabel.setTextFill(Color.web("#4a5568"));
        endDatePicker = createStyledDatePicker();
        endDatePicker.setValue(LocalDate.now().plusDays(30));
        endDateContainer.getChildren().addAll(endLabel, endDatePicker);

        dateContainer.getChildren().addAll(startDateContainer, endDateContainer);

        statusLabel = new Label();
        statusLabel.setFont(Font.font("System", 12));
        statusLabel.setWrapText(true);

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        Button saveButton = createPrimaryButton(buttonText);
        saveButton.setOnAction(e -> handleSave());

        Button cancelButton = createSecondaryButton("Batal");
        cancelButton.setOnAction(e -> dialogStage.close());

        buttonContainer.getChildren().addAll(saveButton, cancelButton);

        formContainer.getChildren().addAll(
                nameLabel, nameField,
                descLabel, descriptionArea,
                dateContainer,
                statusLabel
        );

        mainContainer.getChildren().addAll(titleLabel, formContainer, buttonContainer);

        Scene scene = new Scene(mainContainer, 450, 500);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    private TextField createStyledTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(40);
        field.setPrefWidth(380);
        field.setStyle(
                "-fx-background-color: #f7fafc;" +
                        "-fx-border-color: #e2e8f0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 0 12;" +
                        "-fx-font-size: 14;"
        );
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle() + "-fx-border-color: #667eea; -fx-border-width: 2;");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: #667eea; -fx-border-width: 2;", ""));
            }
        });
        return field;
    }

    private TextArea createStyledTextArea(String placeholder) {
        TextArea area = new TextArea();
        area.setPromptText(placeholder);
        area.setPrefHeight(80);
        area.setPrefWidth(380);
        area.setWrapText(true);
        area.setStyle(
                "-fx-background-color: #f7fafc;" +
                        "-fx-border-color: #e2e8f0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 8 12;" +
                        "-fx-font-size: 14;"
        );
        area.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                area.setStyle(area.getStyle() + "-fx-border-color: #667eea; -fx-border-width: 2;");
            } else {
                area.setStyle(area.getStyle().replace("-fx-border-color: #667eea; -fx-border-width: 2;", ""));
            }
        });
        return area;
    }

    private DatePicker createStyledDatePicker() {
        DatePicker picker = new DatePicker();
        picker.setPrefHeight(40);
        picker.setPrefWidth(180);
        picker.setStyle(
                "-fx-background-color: #f7fafc;" +
                        "-fx-border-color: #e2e8f0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );
        return picker;
    }

    private Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(40);
        button.setPrefWidth(120);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
        return button;
    }

    private Button createSecondaryButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(40);
        button.setPrefWidth(120);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.web("#4a5568"));
        button.setStyle(
                "-fx-background-color: #f7fafc;" +
                        "-fx-border-color: #e2e8f0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #edf2f7;" +
                        "-fx-border-color: #cbd5e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #f7fafc;" +
                        "-fx-border-color: #e2e8f0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        ));
        return button;
    }

    private void fillFields(Project project) {
        nameField.setText(project.getName());
        descriptionArea.setText(project.getDescription());
        startDatePicker.setValue(project.getStartDate());
        endDatePicker.setValue(project.getEndDate());
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (name.isEmpty()) {
            showStatus("Nama proyek wajib diisi!", "error");
            return;
        }

        if (description.isEmpty()) {
            showStatus("Deskripsi wajib diisi!", "error");
            return;
        }

        if (startDate == null || endDate == null) {
            showStatus("Tanggal mulai dan selesai wajib diisi!", "error");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showStatus("Tanggal mulai tidak boleh setelah tanggal selesai!", "error");
            return;
        }

        try {
            if (editingProject == null) {
                ProjectService.createProject(name, description, startDate, endDate,
                        UserService.getCurrentUser().getUsername());
                showStatus("Proyek berhasil dibuat!", "success");
            } else {
                editingProject.setName(name);
                editingProject.setDescription(description);
                editingProject.setStartDate(startDate);
                editingProject.setEndDate(endDate);
                showStatus("Proyek berhasil diperbarui!", "success");
            }

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(() -> {
                        dialogStage.close();
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            showStatus("Kesalahan saat menyimpan proyek: " + e.getMessage(), "error");
        }
    }

    private void showStatus(String message, String type) {
        statusLabel.setText(message);
        if (type.equals("error")) {
            statusLabel.setTextFill(Color.web("#e53e3e")); // merah
        } else {
            statusLabel.setTextFill(Color.web("#38a169")); // hijau
        }
    }
}

