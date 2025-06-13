package app;

import controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Schedulo - Project Management");
        primaryStage.setMinWidth(667);
        primaryStage.setMinHeight(667);

        LoginController loginController = new LoginController(primaryStage);
        loginController.showLoginScreen();
        
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
