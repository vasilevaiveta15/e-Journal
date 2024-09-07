package com.example.ejournal;

import com.example.ejournal.bean.*;
import com.example.ejournal.security.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class JournalResource {
    private final JournalService journalService;
    private final AuthService authService;

    public static final String LOAD_GRADES = "/api/be/e-journal/grades";
    public static final String LOAD_ALL_STUDENTS = "/api/be/e-journal/students";
    public static final String GRADE = "/api/be/e-journal/grade";
    public static final String SUBJECTS = "/api/be/e-journal/all-subjects";
    public static final String REGISTRATION = "/api/be/e-journal/user/reg";
    public static final String REGISTRATION_STUDENT = "/api/be/e-journal/user/reg/student";
    public static final String ADD_SUBJECT = "/api/be/e-journal/subjects";
    public static final String ROLE = "/api/be/e-journal/role";
    public static final String PROFILE_TEACHER = "/api/be/e-journal/teacher-profile";
    public static final String ADD_FINAL_GRADE = "/api/be/e-journal/final-grade";

    public JournalResource(JournalService journalService,
                           AuthService authService) {
        this.journalService = journalService;
        this.authService = authService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(LOAD_GRADES)
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> loadGrades(HttpServletRequest request) {
        return journalService.loadGrades(request.getUserPrincipal().getName()); //tuk se pokazvat vsichki klasove i srokove na uchenika
        //fe shte trqbva da gi podrejda ako myclass e class pokaji go purvo i ako finalGrade e populnena
        //na vsichki predmeti pokaji sledvaash klas i taka natatuk
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping(PROFILE_TEACHER)
    @ResponseStatus(HttpStatus.OK)
    public Teacher teacherProfile(HttpServletRequest request) {

        return journalService.loadTeacherProfile(request.getUserPrincipal().getName());
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping(LOAD_ALL_STUDENTS)
    @ResponseStatus(HttpStatus.OK)
    public List<Student> loadAllStudentsITeach(HttpServletRequest request) {
        return journalService.loadAllStudents(request.getUserPrincipal().getName());
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping(GRADE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addGrade(@RequestBody Grade grade) throws IllegalAccessException {
        journalService.addGrade(grade);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PatchMapping(GRADE)
    @ResponseStatus(HttpStatus.CREATED)
    public void refactorGrade(@RequestBody Grade grade) {
        journalService.refactorGrade(grade);
    }

    //Rolqta za uchitelq e za da moje da vijda predmetite koito sushtestvuvat za da si izbira pri registraciq po kakvo shte prepodava
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping(SUBJECTS)
    public List<Subj> loadSubjects() {
        return journalService.loadSubjects();
    }

    @GetMapping(ROLE)
    public String getRole(HttpServletRequest request) {
        return journalService.getRole(request.getUserPrincipal().getName());
    }

    @PostMapping(REGISTRATION)
    @ResponseStatus(HttpStatus.CREATED)
    public void registration(@RequestBody Registration registration) throws IllegalAccessException {
        journalService.registration(registration);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ADD_SUBJECT)
    @ResponseStatus(HttpStatus.CREATED)
    public void addSubject(@RequestBody RequestSubject requestSubject) {
        journalService.addSubject(requestSubject.getSubject());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(REGISTRATION_STUDENT)
    @ResponseStatus(HttpStatus.CREATED)
    public void registrationStudent(@RequestBody RegistrationStudent registration) {
        journalService.registrationStudent(registration);
    }

    @PostMapping("/api/be/e-journal/token")
    @ResponseStatus(HttpStatus.CREATED)
    public String token(HttpServletRequest request) {
        return authService.authenticateUser(request.getUserPrincipal());
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping(ADD_FINAL_GRADE)
//    @ResponseStatus(HttpStatus.CREATED)
//    public void addFinalGrade(@RequestParam Long subjectId,
//                              @RequestParam Long userId) {
//        journalService.addFinalGrade(subjectId, userId);
//    }

}
