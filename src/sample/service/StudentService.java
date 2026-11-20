package sample.service;

import sample.exception.InvalidStudentException;
import sample.model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private final String filePath = "Data/students.txt";

    public StudentService() {
        // Make sure the Data folder and file exist
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // LOAD
    // =====================================================

    public List<Student> loadStudents() {

        List<Student> students = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length != 4) {
                    continue;
                }

                try {
                    students.add(new Student(
                            Integer.parseInt(parts[0].trim()),
                            parts[1],
                            Integer.parseInt(parts[2].trim()),
                            Double.parseDouble(parts[3].trim())
                    ));
                } catch (NumberFormatException | InvalidStudentException e) {
                    // skip corrupted line, keep loading the rest
                    System.out.println("Skipped bad line: " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return students;
    }

    // =====================================================
    // ADD  (synchronized + rejects duplicate IDs)
    // =====================================================

    public synchronized boolean addStudent(Student student) {

        for (Student s : loadStudents()) {
            if (s.getId() == student.getId()) {
                return false;
            }
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath, true))) {

            writer.write(student.toString());
            writer.newLine();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================================================
    // UPDATE (matched by ID)
    // =====================================================

    public synchronized boolean updateStudent(Student updated) {

        List<Student> students = loadStudents();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == updated.getId()) {
                students.set(i, updated);
                return saveStudents(students);
            }
        }
        return false;
    }

    // =====================================================
    // DELETE
    // =====================================================

    public synchronized boolean deleteStudent(int id) {

        List<Student> students = loadStudents();
        boolean removed = students.removeIf(s -> s.getId() == id);

        return removed && saveStudents(students);
    }

    // =====================================================
    // GPA BONUS  - WITHOUT sync (race condition demo)
    // =====================================================

    public boolean addBonusWithoutSync(int id, double bonus) {
        return applyBonus(id, bonus);
    }

    // =====================================================
    // GPA BONUS  - WITH sync
    // =====================================================

    public synchronized boolean addBonus(int id, double bonus) {
        return applyBonus(id, bonus);
    }

    private boolean applyBonus(int id, double bonus) {

        List<Student> students = loadStudents();   // 1) read

        for (int i = 0; i < students.size(); i++) {

            Student s = students.get(i);

            if (s.getId() == id) {

                try {
                    Thread.sleep(500);              // 2) simulate slow work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }

                double newGpa = Math.min(4.0, s.getGpa() + bonus);

                try {
                    students.set(i, new Student(
                            s.getId(), s.getName(), s.getAge(), newGpa));
                } catch (InvalidStudentException e) {
                    return false;
                }

                return saveStudents(students);      // 3) write
            }
        }
        return false;
    }

    // =====================================================
    // SAVE ALL
    // =====================================================

    private boolean saveStudents(List<Student> students) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath))) {

            for (Student s : students) {
                writer.write(s.toString());
                writer.newLine();
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}