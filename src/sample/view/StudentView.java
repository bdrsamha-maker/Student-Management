package sample.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sample.model.Student;

// The view only BUILDS the UI. It has no logic.
public class StudentView {

    private final BorderPane root;

    private TextField searchField;
    private TableView<Student> studentTable;

    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    private Label totalValue;
    private Label averageValue;
    private Label atRiskValue;

    public StudentView() {
        root = new BorderPane();
        root.setTop(createHeader());
        root.setLeft(createSidebar());
        root.setCenter(createMainContent());
    }

    // ================= HEADER =================

    private HBox createHeader() {

        Label logo = new Label("STUDENT HUB");
        Label subtitle = new Label("Student Management System");

        VBox branding = new VBox(3, logo, subtitle);

        Label user = new Label("Admin  ● Online");

        HBox header = new HBox(20, branding, user);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 25, 15, 25));
        HBox.setHgrow(branding, Priority.ALWAYS);

        return header;
    }

    // ================= SIDEBAR =================

    private VBox createSidebar() {

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);

        Label menuTitle = new Label("MANAGEMENT");

        addButton = createMenuButton("➕  Add Student");
        editButton = createMenuButton("✏  Edit Student");
        deleteButton = createMenuButton("🗑  Delete Student");

        sidebar.getChildren().addAll(
                menuTitle,
                addButton,
                editButton,
                deleteButton
        );

        return sidebar;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setAlignment(Pos.CENTER_LEFT);
        return button;
    }

    // ================= MAIN CONTENT =================

    private VBox createMainContent() {

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Student Search");
        Label description = new Label("Search and manage students");
        VBox titleSection = new VBox(5, title, description);

        TableView<Student> table = createStudentTable();

        content.getChildren().addAll(
                titleSection,
                createSearchSection(),
                createStatistics(),
                new Label("All Students"),
                table
        );

        VBox.setVgrow(table, Priority.ALWAYS);

        return content;
    }

    private HBox createSearchSection() {

        searchField = new TextField();
        searchField.setPromptText("Search by name or ID...");
        searchField.setPrefHeight(45);

        HBox box = new HBox(10, searchField);
        box.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        return box;
    }

    // ================= STATISTICS (real values, set by controller) =================

    private HBox createStatistics() {

        totalValue = new Label("0");
        averageValue = new Label("0.00");
        atRiskValue = new Label("0");

        return new HBox(15,
                createCard("Total Students", totalValue),
                createCard("Average GPA", averageValue),
                createCard("At Risk", atRiskValue)
        );
    }

    private VBox createCard(String title, Label valueLabel) {

        VBox card = new VBox(8, new Label(title), valueLabel);
        card.setPadding(new Insets(15));
        card.setPrefWidth(200);

        return card;
    }

    public void updateStatistics(int total, double averageGpa, long atRisk) {
        totalValue.setText(String.valueOf(total));
        averageValue.setText(String.format("%.2f", averageGpa));
        atRiskValue.setText(String.valueOf(atRisk));
    }

    // ================= TABLE =================

    private TableView<Student> createStudentTable() {

        studentTable = new TableView<>();

        TableColumn<Student, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getId()));

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Student, Number> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getAge()));

        TableColumn<Student, Number> gpaColumn = new TableColumn<>("GPA");
        gpaColumn.setCellValueFactory(
                data -> new SimpleDoubleProperty(data.getValue().getGpa()));

        TableColumn<Student, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getStatus()));

        studentTable.getColumns().addAll(
                idColumn, nameColumn, ageColumn, gpaColumn, statusColumn);

        studentTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        return studentTable;
    }

    // ================= GETTERS =================

    public BorderPane getRoot() { return root; }

    public TextField getSearchField() { return searchField; }

    public TableView<Student> getStudentTable() { return studentTable; }

    public Button getAddButton() { return addButton; }

    public Button getEditButton() { return editButton; }

    public Button getDeleteButton() { return deleteButton; }
}