
insert into activity (id, task_id, author_id, status_code, updated)
values (1001, 1, 1, 'in_progress', now() - interval '5 hour')
ON CONFLICT (id) DO NOTHING;

insert into activity (id, task_id, author_id, status_code, updated)
values (1002, 1, 1, 'ready_for_review', now() - interval '2 hour')
ON CONFLICT (id) DO NOTHING;

insert into activity (id, task_id, author_id, status_code, updated)
values (1003, 1, 1, 'done', now())
ON CONFLICT (id) DO NOTHING;
