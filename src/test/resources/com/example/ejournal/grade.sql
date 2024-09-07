insert into users(id, name, last_name, role, class, groupp, email, password)
values (-111, 'TestV', 'Vasilev', 'STUDENT', '12', 'G', 'test@abv.bg', '123');

insert into users(id, name, last_name, role, class, groupp, email, password)
values (-112, 'TestV', 'Vasilev', 'TEACHER', '12', 'G', 'test2@abv.bg', '123');

insert into subjects (id, name, term, year)
values (-98, 'Biology', '2', '2024');

insert into subjects_users (id, subject_id, user_id)
values (-44, -98, -111);

insert into subjects_users (id, subject_id, user_id)
values (-45, -98, -112);