package com.example.ejournal;

import com.example.ejournal.bean.*;
import com.example.ejournal.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JournalDao {
    private final NamedParameterJdbcTemplate namedTemplate;

    @Autowired
    public JournalDao(NamedParameterJdbcTemplate namedTemplate) {
        this.namedTemplate = namedTemplate;
    }

    public List<Subject> loadGrades(Long id) {
        String sql = " " +
                "SELECT s.id           AS id,                             " +
                "       s.name         AS name,                           " +
                "       su.term        AS term,                           " +
                "       g.grade        AS grade,                          " +
                "       su.class       AS class,                          " +
                "       u.class        AS myClass,                        " +
                "       su.final_grade AS finalGrade,                     " +
                "       g.id           AS gradeId                         " +
                "FROM subjects_users su                                   " +
                "         JOIN subjects s ON s.id = su.subject_id         " +
                "         JOIN users u ON u.id = su.user_id               " +
                "         LEFT JOIN grades g ON g.subject_user_id = su.id " +
                "WHERE u.id = :id                                         ";

        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedTemplate.query(sql, params, (rs, rowNum) -> {
            Subject subject = new Subject();
            subject.setSubjectId(rs.getLong("id"));
            subject.setName(rs.getString("name"));
            subject.setTerm(rs.getLong("term"));
            subject.setGrade(rs.getBigDecimal("grade"));
            subject.setClasss(rs.getLong("class"));
            subject.setMyClass(rs.getLong("myClass"));
            subject.setFinalGrade(rs.getBigDecimal("finalGrade"));
            subject.setGradeId(rs.getLong("gradeId"));
            return subject;
        });

    }

    public Long loadId(Long subjectId,
                       Long studentId,
                       Long term) {
        String sql = " " +
                "SELECT su.id                                " +
                "FROM subjects_users su                      " +
                "         join users u on u.id = su.user_id  " +
                "WHERE su.subject_id = :subjectId            " +
                "  AND su.user_id = :userId                  " +
                "  and u.class = su.class                    " +
                "  and su.term = :term                       ";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("subjectId", subjectId)
                .addValue("userId", studentId)
                .addValue("term", term);
        try {
            return namedTemplate.queryForObject(sql, params, Long.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addGrade(Long grade,
                         Long id) {
        String sql = " " +
                "INSERT INTO grades (subject_user_id, " +
                "                    grade)           " +
                "VALUES (:id,                         " +
                "        :grade)                      ";

        MapSqlParameterSource params =
                new MapSqlParameterSource("id", id)
                        .addValue("grade", grade);

        namedTemplate.update(sql, params);
    }


    public void updateGrade(Long grade,
                            Long gradeId) {
        String sql = " " +
                "UPDATE grades SET grade = :grade WHERE id = :gradeId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("grade", grade)
                .addValue("gradeId", gradeId);

        namedTemplate.update(sql, params);
    }

    public List<Student> loadAllStudents(Long userId) {
        String sql = " " +
                " select u.name      AS userName,                            " +
                "        u.last_name AS lastName,                            " +
                "        u.class     AS class,                               " +
                "        u.groupp    AS groupp,                              " +
                "        u.id        AS id                                   " +
                "from users u                                                " +
                "where id in (select user_id                                 " +
                "             from subjects_users                            " +
                "             where subject_id in (select subject_id         " +
                "                                  from subjects_users       " +
                "                                  where user_id = :userId)) " +
                "  and role = 'STUDENT'                                      ";

        return namedTemplate.query(sql, new MapSqlParameterSource("userId", userId), (rs, rowNum) -> {
            Student student = new Student();
            student.setId(rs.getLong("id"));
            student.setName(rs.getString("userName"));
            student.setLastName(rs.getString("lastName"));
            student.setClas(rs.getString("class"));
            student.setGroup(rs.getString("groupp"));
            return student;
        });
    }

    public User findByEmail(String email) {
        String sql = "" +
                "SELECT id,             " +
                "       name,            " +
                "       last_name,      " +
                "       email,          " +
                "       password,       " +
                "       role            " +
                "   FROM users          " +
                " WHERE email = :email  ";

        MapSqlParameterSource params = new MapSqlParameterSource("email", email);

        try {
            return namedTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(Role.valueOf(rs.getString("role")));
                user.setPassword(rs.getString("password"));
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public void createUser(Registration registration) {
        String sql = "" +
                "insert into users(name,      " +
                "                  last_name, " +
                "                  role,      " +
                "                  class,     " +
                "                  groupp,    " +
                "                   email,    " +
                "                   password) " +
                "values (:name,               " +
                "        :lastName,           " +
                "        :role,               " +
                "        :clas,               " +
                "        :group,              " +
                "        :email,              " +
                "        :password)           ";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", registration.getFirstName())
                .addValue("lastName", registration.getLastName())
                .addValue("role", registration.getRole())
                .addValue("clas", registration.getClas())
                .addValue("password", registration.getPassword())
                .addValue("email", registration.getEmail())
                .addValue("group", registration.getGroup());

        namedTemplate.update(sql, params);
    }

    public void createConnection(String email,
                                 Long subjectId,
                                 Long term,
                                 String classs) {
        String sql = ""
                + "insert into subjects_users (user_id,     "
                + "                    subject_id,          "
                + "                    term,                "
                + "                    class)               "
                + " SELECT                                  "
                + "    (SELECT u.id                         "
                + "    FROM users u                         "
                + "    WHERE u.email = :email)              "
                + "    , :subjectId                         "
                + "    , :term                              "
                + "    , :class                             "
                + " FROM dual                               ";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("subjectId", subjectId)
                .addValue("term", term)
                .addValue("class", classs);


        namedTemplate.update(sql, params);
    }

    public void addSubject(Subject subject) {
        String sql = "insert into subjects (name) VALUES(:name)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", subject.getName())
                .addValue("term", subject.getTerm());
        namedTemplate.update(sql, params);
    }

    public List<Subj> loadSubjects() {
        String sql = ""
                + "SELECT id,       "
                + "       name      "
                + "  FROM subjects  ";

        return namedTemplate.query(sql, new MapSqlParameterSource(), (rs, rowNum) -> {
            Subj s = new Subj();
            s.setId(rs.getLong("id"));
            s.setName(rs.getString("name"));
            return s;
        });

    }

    public Teacher loadMySubject(Long id) {
        String sql = " " +
                "select s.name AS name,                                  " +
                "       s.id   AS id                                     " +
                "from subjects s                                         " +
                "         JOIN subjects_users su ON su.subject_id = s.id " +
                "where su.user_id = :userId                              ";

        MapSqlParameterSource params = new MapSqlParameterSource("userId", id);
        try {
            return namedTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                Teacher t = new Teacher();
                t.setSubjectName(rs.getString("name"));
                t.setSubjectId(rs.getLong("id"));
                return t;
            });
        } catch (EmptyResultDataAccessException e) {
            return new Teacher();
        }
    }

    public void addFinalGrade(Long subjectId,
                              Long userId,
                              Long finalGrade) {
        String sql = " " +
                "update subjects_users                                                           " +
                "set final_grade = :finalGrade                                                   " +
                "where subject_id = :subjectId                                                   " +
                "  and user_id = :userId                                                         " +
                "  and class = (select class from users where id = :userId and role = 'STUDENT') ";

        MapSqlParameterSource params = new MapSqlParameterSource("subjectId", subjectId)
                .addValue("userId", userId)
                .addValue("finalGrade", finalGrade);

        namedTemplate.update(sql, params);
    }

    public void nextYear() {
        String sql = "" +
                " UPDATE users                                   " +
                " SET class = CASE                               " +
                "                 WHEN class < 12 THEN class + 1 " +
                "                 ELSE class                     " +
                "     END                                        " +
                " WHERE role = 'STUDENT'                         ";

        namedTemplate.update(sql, new MapSqlParameterSource());
    }

    public List<Student> loadAllUsers(String role) {
        String sql = " " +
                " SELECT id,         " +
                "        name,       " +
                "        last_name,  " +
                "        class,      " +
                "        groupp      " +
                " FROM users         " +
                " WHERE role = :role ";

        MapSqlParameterSource params = new MapSqlParameterSource("role", role);
        return namedTemplate.query(sql, params, (rs, rowNum) -> {
            Student s = new Student();
            s.setId(rs.getLong("id"));
            s.setName(rs.getString("name"));
            s.setLastName(rs.getString("last_name"));
            s.setClas(rs.getString("class"));
            s.setGroup(rs.getString("groupp"));
            return s;
        });
    }
}
