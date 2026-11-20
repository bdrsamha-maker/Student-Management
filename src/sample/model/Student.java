package sample.model;

import sample.exception.InvalidStudentException;

public class Student {

    private final int id;
    private final String name;
    private final int age;
    private final double gpa;

    // Validation lives in the model: invalid data can never become a Student
    public Student(int id, String name, int age, double gpa)
            throws InvalidStudentException {

        if (id <= 0) {
            throw new InvalidStudentException("ID must be a positive number.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidStudentException("Name cannot be empty.");
        }
        if (name.contains(",")) {
            throw new InvalidStudentException("Name cannot contain a comma.");
        }
        if (age < 15 || age > 100) {
            throw new InvalidStudentException("Age must be between 15 and 100.");
        }
        if (gpa < 0 || gpa > 4) {
            throw new InvalidStudentException("GPA must be between 0 and 4.");
        }

        this.id = id;
        this.name = name.trim();
        this.age = age;
        this.gpa = gpa;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public int getAge() { return age; }

    public double getGpa() { return gpa; }

    public String getStatus() {
        if (gpa >= 3.5) return "● Excellent";
        if (gpa >= 2.0) return "● Passing";
        return "● At Risk";
    }

    @Override
    public String toString() {
        return id + "," + name + "," + age + "," + gpa;
    }
}