package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import sample.exception.InvalidStudentException;
import sample.model.Student;
import sample.service.StudentService;
import sample.view.StudentFormView;
import sample.view.StudentView;

import java.util.Optional;

public class StudentController {

    private final StudentView view;
    private final StudentService service;

    private final ObservableList<Student> allStudents;
    private final FilteredList<Student> filteredStudents;

    public StudentController() {

        view = new StudentView();
        service = new StudentService();

        allStudents = FXCollections.observableArrayList(service.loadStudents());

        // FilteredList = live view of allStudents, changes with the search text
        filteredStudents = new FilteredList<>(allStudents, s -> true);
        view.getStudentTable().setItems(filteredStudents);

        setupSearch();
        setupAddButton();
        setupEditButton();
        setupDeleteButton();
        setupMouseEvents();
        setupKeyboardEvents();

        refreshStatistics();
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private void setupSearch() {

        view.getSearchField().textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String search = newValue.toLowerCase().trim();

                    filteredStudents.setPredicate(student ->
                            search.isEmpty()
                                    || student.getName().toLowerCase().contains(search)
                                    || String.valueOf(student.getId()).startsWith(search)
                    );
                });
    }

    // =========================================================
    // ADD
    // =========================================================

    private void setupAddButton() {

        view.getAddButton().setOnAction(event -> {

            StudentFormView form = new StudentFormView("Add Student", null);

            form.getSaveButton().setOnAction(e -> saveNewStudent(form));
            form.getCancelButton().setOnAction(e -> form.getStage().close());

            form.show();
        });
    }

    private void saveNewStudent(StudentFormView form) {

        try {
            Student student = readStudent(form);

            if (!service.addStudent(student)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate ID",
                        "A student with this ID already exists.");
                return;
            }

            allStudents.add(student);
            refreshStatistics();
            form.getStage().close();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Student added successfully.");

        } catch (InvalidStudentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Data", e.getMessage());

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Data",
                    "ID and age must be integers and GPA must be a number.");
        }
    }

    // =========================================================
    // EDIT
    // =========================================================

    private void setupEditButton() {

        view.getEditButton().setOnAction(event -> {

            Student selected = getSelectedStudent();
            if (selected == null) return;

            StudentFormView form = new StudentFormView("Edit Student", selected);

            form.getSaveButton().setOnAction(e -> saveEditedStudent(selected, form));
            form.getCancelButton().setOnAction(e -> form.getStage().close());

            form.show();
        });
    }

    private void saveEditedStudent(Student oldStudent, StudentFormView form) {

        try {
            Student updated = readStudent(form);

            if (!service.updateStudent(updated)) {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Could not update the student.");
                return;
            }

            allStudents.set(allStudents.indexOf(oldStudent), updated);
            refreshStatistics();
            form.getStage().close();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Student updated successfully.");

        } catch (InvalidStudentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Data", e.getMessage());

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Data",
                    "ID and age must be integers and GPA must be a number.");
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void setupDeleteButton() {

        view.getDeleteButton().setOnAction(event -> {

            Student selected = getSelectedStudent();
            if (selected == null) return;

            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Delete " + selected.getName() + "?",
                    ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                if (service.deleteStudent(selected.getId())) {
                    allStudents.remove(selected);
                    refreshStatistics();
                }
            }
        });
    }

    // =========================================================
    // HELPERS
    // =========================================================

    // NumberFormatException comes from parsing, InvalidStudentException from the model
    private Student readStudent(StudentFormView form)
            throws InvalidStudentException {

        int id = Integer.parseInt(form.getIdField().getText().trim());
        String name = form.getNameField().getText();
        int age = Integer.parseInt(form.getAgeField().getText().trim());
        double gpa = Double.parseDouble(form.getGpaField().getText().trim());

        return new Student(id, name, age, gpa);
    }

    private Student getSelectedStudent() {

        Student selected =
                view.getStudentTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Student Selected",
                    "Please select a student first.");
        }
        return selected;
    }

    private void refreshStatistics() {

        double average = allStudents.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0);

        long atRisk = allStudents.stream()
                .filter(s -> s.getGpa() < 2.0)
                .count();

        view.updateStatistics(allStudents.size(), average, atRisk);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // =========================================================
    // MOUSE EVENTS  (all on the search field)
    // =========================================================

    private void setupMouseEvents() {

        view.getSearchField().setOnMousePressed(
                event -> System.out.println("Mouse Pressed"));

        view.getSearchField().setOnMouseReleased(
                event -> System.out.println("Mouse Released"));

        view.getSearchField().setOnMouseEntered(
                event -> System.out.println("Mouse Entered Search Field"));

        view.getSearchField().setOnMouseExited(
                event -> System.out.println("Mouse Exited Search Field"));

        view.getSearchField().setOnMouseMoved(
                event -> System.out.println("Mouse Moved"));

        view.getSearchField().setOnMouseDragged(
                event -> System.out.println("Mouse Dragged"));

        // ONE click handler only (a second setOnMouseClicked would replace this one)
        view.getSearchField().setOnMouseClicked(event -> {
            System.out.println("Mouse Clicked");

            if (event.getClickCount() == 2) {
                view.getSearchField().clear();
            }
        });
    }

    // =========================================================
    // KEYBOARD EVENTS
    // =========================================================

    private void setupKeyboardEvents() {

        view.getSearchField().setOnKeyPressed(event -> {
            System.out.println("Key Pressed: " + event.getCode());

            if (event.getCode() == KeyCode.ESCAPE) {
                view.getSearchField().clear();
            }
        });

        view.getSearchField().setOnKeyReleased(
                event -> System.out.println("Key Released: " + event.getCode()));

        view.getSearchField().setOnKeyTyped(
                event -> System.out.println("Key Typed: " + event.getCharacter()));

        // Delete key removes the selected student
        view.getStudentTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                view.getDeleteButton().fire();
            }
        });
    }

    // =========================================================
    // GET VIEW
    // =========================================================

    public StudentView getView() {
        return view;
    }
}