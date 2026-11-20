package sample;

import sample.exception.InvalidStudentException;
import sample.model.Student;
import sample.service.StudentService;

// Tests the logic WITHOUT any UI (that is the point of the service layer)
public class Test {

    public static void main(String[] args) throws Exception {

        StudentService service = new StudentService();

        service.addStudent(new Student(1, "Ahmed Ali", 20, 3.0));

        // ---- Exception test ----
        try {
            new Student(2, "", 20, 3.0);
        } catch (InvalidStudentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // ---- Race condition demo: two threads each add +0.1 ----
        System.out.println("Start GPA = " + gpaOf(service, 1));

        runTwoThreads(() -> service.addBonusWithoutSync(1, 0.1));
        System.out.println("WITHOUT sync -> " + gpaOf(service, 1) + "  (expected 3.2)");

        service.updateStudent(new Student(1, "Ahmed Ali", 20, 3.0));

        runTwoThreads(() -> service.addBonus(1, 0.1));
        System.out.println("WITH sync    -> " + gpaOf(service, 1) + "  (expected 3.2)");
    }

    private static void runTwoThreads(Runnable task) throws InterruptedException {
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    private static double gpaOf(StudentService service, int id) {
        for (Student s : service.loadStudents()) {
            if (s.getId() == id) return s.getGpa();
        }
        return -1;
    }
}