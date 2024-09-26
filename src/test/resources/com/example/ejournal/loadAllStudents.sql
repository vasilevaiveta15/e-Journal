insert into users(id, name, last_name, role, class, groupp, email, password)
values (-111, 'TestV', 'Vasilev', 'STUDENT', '12', 'G', 'test@abv.bg', '123');

insert into users(id, name, last_name, role, class, groupp, email, password)
values (-112, 'TestV', 'Vasilev', 'TEACHER', '12', 'G', 'test2@abv.bg', '123');

insert into subjects (id, name)
values (-98, 'Biology');

insert into subjects_users (id, subject_id, user_id, term, class, final_grade)
values (-44, -98, -111, 1, 12, 5);

insert into subjects_users (id, subject_id, user_id, term, class, final_grade)
values (-45, -98, -112, 1, 12, 6);

insert into grades (subject_user_id, grade)
values (-44, 5);
