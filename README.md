# Student Management System

JavaFX desktop application for managing students, built with the MVC architecture.

## Features
- Add, edit, and delete students
- Search by name or ID
- Live statistics (total students, average GPA, at-risk students)
- Data validation with a custom exception
- File storage in `Data/students.txt`
- Thread-safe service methods (`synchronized`)

## Project Structure
- `model/` : Student class
- `exception/` : InvalidStudentException
- `service/` : StudentService (file handling and logic)
- `view/` : StudentView, StudentFormView (UI only)
- `controller/` : StudentController (events and wiring)
- `Test.java` : tests the logic without the UI

## How to Run
1. Open the project in IntelliJ IDEA.
2. Add the JavaFX SDK to the project libraries.
3. Run `sample.Main`.

## Author
Badr