
package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import service.UserService;

public class LoginController {
    private Stage stage;
    private boolean isRegisterMode = false;

    private VBox mainContainer;
    private Label titleLabel;
    private Label brandLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField emailField;
    private Button primaryButton;
    private Button switchModeButton;
    private Label statusLabel;
    public LoginController(Stage stage) {
        this.stage = stage;
    }
    
    public void showLoginScreen() {
        createLoginUI();
        Scene scene = new Scene(mainContainer, 380, 600);
        stage.setScene(scene);
        stage.setTitle("Schedulo - Pengelola Tugas Pintar");
        stage.setResizable(false);
    }
    
    private void createLoginUI() {
        mainContainer = new VBox(0);
        mainContainer.setAlignment(Pos.CENTER);

        mainContainer.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);"
        );

        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(60, 40, 40, 40));
        
        // Placeholder logo/ikon brand
        Label logoIcon = new Label("📅");
        logoIcon.setFont(Font.font("System", 48));
        
        // Nama brand
        brandLabel = new Label("Schedulo");
        brandLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        brandLabel.setTextFill(Color.WHITE);
        
        // Tagline brand
        Label taglineLabel = new Label("Pengelolaan Tugas Pintar");
        taglineLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        taglineLabel.setTextFill(Color.web("#ffffff80"));
        
        topSection.getChildren().addAll(logoIcon, brandLabel, taglineLabel);

        VBox cardContainer = new VBox(20);
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setPadding(new Insets(40, 30, 40, 30));
        cardContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 25 25 0 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, -2);"
        );

        VBox welcomeSection = new VBox(8);
        welcomeSection.setAlignment(Pos.CENTER);
        
        titleLabel = new Label("Selamat Datang di Schedulo");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2d3748"));
        
        Label subtitleLabel = new Label("Masuk untuk melanjutkan");
        subtitleLabel.setFont(Font.font("System", 14));
        subtitleLabel.setTextFill(Color.web("#718096"));
        
        welcomeSection.getChildren().addAll(titleLabel, subtitleLabel);

        VBox inputSection = new VBox(15);
        inputSection.setAlignment(Pos.CENTER);
        
        usernameField = createModernTextField("👤 Nama Pengguna");
        passwordField = createModernPasswordField("🔒 Kata Sandi");
        emailField = createModernTextField("📧 Email");
        emailField.setVisible(false);
        emailField.setManaged(false);

        setupKeyNavigation();
        
        inputSection.getChildren().addAll(usernameField, passwordField, emailField);

        statusLabel = new Label();
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        statusLabel.setWrapText(true);
        statusLabel.setAlignment(Pos.CENTER);

        VBox buttonSection = new VBox(15);
        buttonSection.setAlignment(Pos.CENTER);
        
        primaryButton = createModernPrimaryButton("Masuk");
        primaryButton.setOnAction(e -> handleLogin());
        
        switchModeButton = createModernSecondaryButton("Belum punya akun? Daftar");
        switchModeButton.setOnAction(e -> toggleMode());
        
        buttonSection.getChildren().addAll(primaryButton, switchModeButton);

        cardContainer.getChildren().addAll(
            welcomeSection,
            inputSection,
            statusLabel,
            buttonSection
        );

        VBox.setVgrow(cardContainer, Priority.ALWAYS);
        
        mainContainer.getChildren().addAll(topSection, cardContainer);
    }
    
    private TextField createModernTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(50);
        field.setPrefWidth(280);
        field.setStyle(
            "-fx-background-color: #f8fafc;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 0 20;" +
            "-fx-font-size: 14;" +
            "-fx-prompt-text-fill: #a0aec0;"
        );
        
        // Efek fokus modern
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #667eea;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 0 20;" +
                    "-fx-font-size: 14;" +
                    "-fx-prompt-text-fill: #a0aec0;" +
                    "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.2), 8, 0, 0, 2);"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: #f8fafc;" +
                    "-fx-border-color: #e2e8f0;" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 0 20;" +
                    "-fx-font-size: 14;" +
                    "-fx-prompt-text-fill: #a0aec0;"
                );
            }
        });
        
        return field;
    }
    
    private PasswordField createModernPasswordField(String placeholder) {
        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.setPrefHeight(50);
        field.setPrefWidth(280);
        field.setStyle(
            "-fx-background-color: #f8fafc;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 0 20;" +
            "-fx-font-size: 14;" +
            "-fx-prompt-text-fill: #a0aec0;"
        );

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #667eea;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 0 20;" +
                    "-fx-font-size: 14;" +
                    "-fx-prompt-text-fill: #a0aec0;" +
                    "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.2), 8, 0, 0, 2);"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: #f8fafc;" +
                    "-fx-border-color: #e2e8f0;" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 0 20;" +
                    "-fx-font-size: 14;" +
                    "-fx-prompt-text-fill: #a0aec0;"
                );
            }
        });
        
        return field;
    }
    
    private void setupKeyNavigation() {
        usernameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (isRegisterMode && emailField.isVisible()) {
                    emailField.requestFocus();
                } else {
                    passwordField.requestFocus();
                }
            }
        });
        
        emailField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });
        
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (isRegisterMode) {
                    handleRegister();
                } else {
                    handleLogin();
                }
            }
        });
    }
    
    private Button createModernPrimaryButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(50);
        button.setPrefWidth(280);
        button.setFont(Font.font("System", FontWeight.BOLD, 16));
        button.setTextFill(Color.WHITE);
        
        button.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.4), 8, 0, 0, 4);"
        );
        
        // Efek hover yang diperkuat
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: linear-gradient(to right, #5a67d8, #6b46c1);" +
                "-fx-background-radius: 25;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.6), 12, 0, 0, 6);" +
                "-fx-scale-y: 1.05;" +
                "-fx-scale-x: 1.05;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                "-fx-background-radius: 25;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.4), 8, 0, 0, 4);" +
                "-fx-scale-y: 1.0;" +
                "-fx-scale-x: 1.0;"
            );
        });
        
        return button;
    }
    
    private Button createModernSecondaryButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("System", FontWeight.NORMAL, 13));
        button.setTextFill(Color.web("#667eea"));
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-cursor: hand;" +
            "-fx-underline: false;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setTextFill(Color.web("#5a67d8"));
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-cursor: hand;" +
                "-fx-underline: true;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setTextFill(Color.web("#667eea"));
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-cursor: hand;" +
                "-fx-underline: false;"
            );
        });
        
        return button;
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Harap isi semua kolom!", "error");
            return;
        }

        primaryButton.setText("Sedang Masuk...");
        primaryButton.setDisable(true);

        javafx.concurrent.Task<Boolean> loginTask = new javafx.concurrent.Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Thread.sleep(500); 
                return UserService.login(username, password);
            }
        };
        
        loginTask.setOnSucceeded(e -> {
            primaryButton.setText("Masuk");
            primaryButton.setDisable(false);
            
            if (loginTask.getValue()) {
                showStatus("Selamat datang kembali! 🎉", "success");
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {}
                    DashboardController dashboardController = new DashboardController(stage);
                    dashboardController.showDashboard();
                });
            } else {
                showStatus("Kredensial tidak valid! 🚫", "error");
            }
        });
        
        new Thread(loginTask).start();
    }
    
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showStatus("Harap isi semua kolom!", "error");
            return;
        }
        
        if (!isValidEmail(email)) {
            showStatus("Harap masukkan alamat email yang valid!", "error");
            return;
        }
        
        if (password.length() < 6) {
            showStatus("Kata sandi harus minimal 6 karakter!", "error");
            return;
        }

        primaryButton.setText("Membuat Akun...");
        primaryButton.setDisable(true);
        
        javafx.concurrent.Task<Boolean> registerTask = new javafx.concurrent.Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Thread.sleep(500);
                return UserService.register(username, password, email);
            }
        };
        
        registerTask.setOnSucceeded(e -> {
            primaryButton.setText("Daftar");
            primaryButton.setDisable(false);
            
            if (registerTask.getValue()) {
                showStatus("Akun berhasil dibuat! 🎉", "success");
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {}
                    toggleMode();
                });
            } else {
                showStatus("Nama pengguna sudah ada! 🚫", "error");
            }
        });
        
        new Thread(registerTask).start();
    }
    
    private void toggleMode() {
        isRegisterMode = !isRegisterMode;
        clearFields();
        clearStatus();
        
        if (isRegisterMode) {
            titleLabel.setText("Buat Akun");
            emailField.setVisible(true);
            emailField.setManaged(true);
            primaryButton.setText("Daftar");
            primaryButton.setOnAction(e -> handleRegister());
            switchModeButton.setText("Sudah punya akun? Masuk");
        } else {
            titleLabel.setText("Selamat Datang Kembali");
            emailField.setVisible(false);
            emailField.setManaged(false);
            primaryButton.setText("Masuk");
            primaryButton.setOnAction(e -> handleLogin());
            switchModeButton.setText("Belum punya akun? Daftar");
        }
    }
    
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
    }
    
    private void clearStatus() {
        statusLabel.setText("");
    }
    
    private void showStatus(String message, String type) {
        statusLabel.setText(message);
        if (type.equals("error")) {
            statusLabel.setTextFill(Color.web("#e53e3e"));
            statusLabel.setStyle("-fx-background-color: #fed7d7; -fx-padding: 8 12; -fx-background-radius: 8;");
        } else {
            statusLabel.setTextFill(Color.web("#38a169"));
            statusLabel.setStyle("-fx-background-color: #c6f6d5; -fx-padding: 8 12; -fx-background-radius: 8;");
        }
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}