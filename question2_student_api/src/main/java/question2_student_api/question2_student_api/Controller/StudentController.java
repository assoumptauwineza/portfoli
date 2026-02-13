package question2_student_api.question2_student_api.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import question2_student_api.question2_student_api.model.Student;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private List<Student> students = new ArrayList<>();

    public StudentController() {
        students.add(new Student(1L, "John", "Doe", "john.doe@email.com", "Computer Science", 3.8));
        students.add(new Student(2L, "Jane", "Smith", "jane.smith@email.com", "Computer Science", 3.9));
        students.add(new Student(3L, "Mike", "Johnson", "mike.j@email.com", "Business", 3.2));
        students.add(new Student(4L, "Sarah", "Williams", "sarah.w@email.com", "Engineering", 3.6));
        students.add(new Student(5L, "Tom", "Brown", "tom.b@email.com", "Computer Science", 3.4));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> result = students.stream()
                .filter(s -> s.getMajor().equalsIgnoreCase(major))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterStudentsByGpa(@RequestParam Double gpa) {
        List<Student> result = students.stream()
                .filter(s -> s.getGpa() >= gpa)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @RequestBody Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(studentId)) {
                updatedStudent.setStudentId(studentId);
                students.set(i, updatedStudent);
                return ResponseEntity.ok(updatedStudent);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
