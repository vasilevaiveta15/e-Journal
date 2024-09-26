package com.example.ejournal;

import com.example.ejournal.bean.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.example.ejournal.JournalResource.*;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.userPassword;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class EJournalApplicationTests {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    /**
     * Link to the service {@link JournalResource#loadGrades}
     */
    @Test
    @Sql(scripts = "loadGrades.sql")
    @WithUserDetails(value = "test@abv.bg")
    void testLoadGrades() throws Exception {
        User user = new User("test@abv.bg", userPassword, AuthorityUtils.createAuthorityList("STUDENT"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(get(LOAD_GRADES)
                        .principal(testingAuthenticationToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    /**
     * Link to the service {@link JournalResource#loadAllStudentsITeach}
     */
    @Test
    @Sql(scripts = "loadAllStudents.sql")
    @WithUserDetails(value = "test2@abv.bg")
    void testLoadAllStudents() throws Exception {
        User user = new User("test2@abv.bg", userPassword, AuthorityUtils.createAuthorityList("TEACHER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(get(LOAD_ALL_STUDENTS)
                        .principal(testingAuthenticationToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    /**
     * Link to the service {@link JournalResource#addGrade}
     */
    @Test
    @Sql(scripts = "grade.sql")
    @WithUserDetails(value = "test2@abv.bg")
    void testAddGrade() throws Exception {
        User user = new User("test2@abv.bg", userPassword, AuthorityUtils.createAuthorityList("TEACHER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        Grade grade = new Grade();
        grade.setGrade(5L);
        grade.setStudentId(-112L);
        grade.setSubjectId(-98L);
        grade.setTerm(1L);

        mockMvc.
                perform(post(GRADE)
                        .principal(testingAuthenticationToken)
                        .content(objectMapper.writeValueAsString(grade))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    /**
     * Link to the service {@link JournalResource#refactorGrade}
     */
    @Test
    @Sql(scripts = "grade.sql")
    @WithUserDetails(value = "test2@abv.bg")
    void testRefactorGrade() throws Exception {
        User user = new User("test2@abv.bg", userPassword, AuthorityUtils.createAuthorityList("TEACHER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        Grade grade = new Grade();
        grade.setGrade(6L);
        grade.setStudentId(-112L);
        grade.setSubjectId(-98L);

        mockMvc.
                perform(patch(GRADE)
                        .principal(testingAuthenticationToken)
                        .content(objectMapper.writeValueAsString(grade))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    /**
     * Link to the service {@link JournalResource#loadSubjects}
     */
    @Test
    @Sql(scripts = "subjects.sql")
    void testLoadSubjects() throws Exception {
        mockMvc.perform(get(SUBJECTS))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    /**
     * Link to the service {@link JournalResource#addSubject}
     */
    @Test
    @Sql(scripts = "addSubject.sql")
    @WithUserDetails(value = "test2@abv.bg")
    void testAddSubject() throws Exception {
        User user = new User("test2@abv.bg", userPassword, AuthorityUtils.createAuthorityList("ADMIN"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        RequestSubject requestSubject = new RequestSubject();
        Subject subject = new Subject();
        subject.setTerm(2L);
        subject.setName("Mathematics");

        Subject subject2 = new Subject();
        subject2.setTerm(2L);
        subject.setName("Geography");

        List<Subject> subjectList = Arrays.asList(subject, subject2);
        requestSubject.setSubject(subjectList);

        mockMvc.
                perform(post(ADD_SUBJECT)
                        .principal(testingAuthenticationToken)
                        .content(objectMapper.writeValueAsString(requestSubject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    /**
     * Link to the service {@link JournalResource#registration}
     */
    @Test
    void testRegistration() throws Exception {
        Registration registration = new Registration();
        registration.setClas("2");
        registration.setEmail("testcheEmal@abv.bg");
        registration.setGroup("A");
        registration.setPassword("123");
        registration.setRole("STUDENT");
        registration.setFirstName("Pesho");
        registration.setLastName("Petrov");
        registration.setRepeatedPassword("123");
        registration.setSubjectId(1L);

        mockMvc.
                perform(post(REGISTRATION)
                        .content(objectMapper.writeValueAsString(registration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

    }
}
