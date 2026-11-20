package sample.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.Student;

// One popup used for BOTH add and edit (pass null for add)
public class StudentFormView {

    private final Stage stage;

    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField ageField = new TextField();
    private final TextField gpaField = new TextField();

    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    public StudentFormView(String title, Student existing) {

        stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);

        if (existing != null) {
            idField.setText(String.valueOf(existing.getId()));
            idField.setDisable(true);               // ID is the key, not editable
            nameField.setText(existing.getName());
            ageField.setText(String.valueOf(existing.getAge()));
            gpaField.setText(String.valueOf(existing.getGpa()));
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.addRow(0, new Label("ID"), idField);
        grid.addRow(1, new Label("Name"), nameField);
        grid.addRow(2, new Label("Age"), ageField);
        grid.addRow(3, new Label("GPA"), gpaField);

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(20, grid, buttons);
        root.setPadding(new Insets(25));

        stage.setScene(new Scene(root, 380, 260));
    }

    public void show() { stage.showAndWait(); }

    public Stage getStage() { return stage; }

    public TextField getIdField() { return idField; }

    public TextField getNameField() { return nameField; }

    public TextField getAgeField() { return ageField; }

    public TextField getGpaField() { return gpaField; }

    public Button getSaveButton() { return saveButton; }

    public Button getCancelButton() { return cancelButton; }
}