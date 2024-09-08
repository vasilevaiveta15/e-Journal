package com.example.ejournal;

import com.example.ejournal.bean.*;
import com.example.ejournal.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class JournalService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final JournalDao journalDao;

    @Autowired
    public JournalService(BCryptPasswordEncoder passwordEncoder,
                          JournalDao journalDao) {
        this.passwordEncoder = passwordEncoder;
        this.journalDao = journalDao;
    }

    public void doLogin(String login,
                        String pass) {
    }

    public List<Subject> loadGrades(String email) {
        User user = journalDao.findByEmail(email);
        return journalDao.loadGrades(user.getId());
    }

    public void addGrade(Grade grade) throws IllegalAccessException {
        //check if the subjectId is mine
        Long id = journalDao.loadId(grade.getSubjectId(), grade.getStudentId());
        if (null == id) {
            throw new IllegalAccessException("Could not add grade!");
        }
        journalDao.addGrade(grade.getGrade(), id);
    }

    public void refactorGrade(Grade grade) {
        journalDao.updateGrade(grade.getGrade(), grade.getGradeId());
    }

    public List<Student> loadAllStudents(String email) {
        User user = journalDao.findByEmail(email);
        List<Student> students = journalDao.loadAllStudents(user.getId());
        for (Student s : students) {
            List<Subject> subject = journalDao.loadGrades(s.getId());
            s.setSubjects(subject);
        }
        return students;
    }

    public void registrationStudent(RegistrationStudent registration) {
        registration.setPassword(passwordEncoder.encode(registration.getPassword()));
        Registration registration1 = setRegistration(registration);
        journalDao.createUser(registration1);
        for (RequestSubj requestSubj : registration.getRequestSubj()) {
            for (Map.Entry<Long, Long> entry : requestSubj.getSubjects().entrySet()) {
                journalDao.createConnection(registration.getEmail(), entry.getKey(), entry.getValue(), requestSubj.getClasss());
            }
        }
    }

    public void registration(Registration registration) throws IllegalAccessException {
        validation(registration);
        registration.setPassword(passwordEncoder.encode(registration.getPassword()));
        journalDao.createUser(registration);
        if (Role.TEACHER.toString().equals(registration.getRole())) {
            journalDao.createConnection(registration.getEmail(), registration.getSubjectId(), null, null);
        }
    }

    public User findByEmail(String email) {
        return journalDao.findByEmail(email);
    }

    public void addSubject(List<Subject> subjects) {
        for (Subject s : subjects) {
            journalDao.addSubject(s);
        }
    }

    public List<Subj> loadSubjects() {
        return journalDao.loadSubjects();
    }

    public String getRole(String email) {
        User user = journalDao.findByEmail(email);
        return user.getRole().name();
    }

    public Teacher loadTeacherProfile(String email) {
        User user = journalDao.findByEmail(email);
        Teacher teacher = new Teacher();
        teacher.setEmail(email);
        teacher.setId(user.getId());
        teacher.setRole(user.getRole());
        teacher.setFirstName(user.getFirstName());
        teacher.setLastName(user.getLastName());
        Teacher teacher2 = journalDao.loadMySubject(teacher.getId());
        teacher.setSubjectId(teacher2.getSubjectId());
        teacher.setSubjectName(teacher2.getSubjectName());
        return teacher;
    }

    private void validation(Registration registration) throws IllegalAccessException {
        if (null == registration.getRole()) {
            throw new IllegalAccessException("You cannot make registration without choose role.");
        }
        if (!Role.STUDENT.toString().equals(registration.getRole())
                && StringUtils.hasLength(registration.getClas())
                && StringUtils.hasLength(registration.getGroup())) {

            throw new IllegalAccessException("You are not student!");
        }
        if (Role.ADMIN.toString().equals(registration.getRole()) && null != registration.getSubjectId()) {
            throw new IllegalAccessException("Not allowed to fill subject!");
        }
    }

    private Registration setRegistration(RegistrationStudent registration) {
        Registration registration1 = new Registration();
        registration1.setRepeatedPassword(registration.getRepeatedPassword());
        registration1.setPassword(registration.getPassword());
        registration1.setRole(registration.getRole());
        registration1.setFirstName(registration.getFirstName());
        registration1.setLastName(registration.getLastName());
        registration1.setGroup(registration.getGroup());
        registration1.setEmail(registration.getEmail());
        registration1.setClas(registration.getMyClas());
        return registration1;
    }

    public void addFinalGrade(Long subjectId,
                              Long userId,
                              Long finalGrade) {
        journalDao.addFinalGrade(subjectId, userId, finalGrade);
    }

    public void nextYear() {
        journalDao.nextYear();
    }
}
