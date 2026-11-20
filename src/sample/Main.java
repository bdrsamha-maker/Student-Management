package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.StudentController;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        StudentController controller = new StudentController();

        Scene scene = new Scene(
                controller.getView().getRoot(),
                1000,
                700
        );

        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}