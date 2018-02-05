INSERT INTO account
       (group_id, college_id, first_name, last_name, username, email, password, enabled, created_on, created_by, approved)
VALUES
       (1, 1, 'System', 'MiniSeva', 'system@miniseva.com', 'system@miniseva.com', '$2a$10$9NhAigH0vdTPk1M45AVSYO1UpA0ZQSl1ce6drvP4KdPzlBHnnHGm2', TRUE, NOW(), 1, TRUE),
       (4, 1, 'Admin', 'MiniSeva', 'admin@miniseva.com', 'admin@miniseva.com', '$2a$10$9NhAigH0vdTPk1M45AVSYO1UpA0ZQSl1ce6drvP4KdPzlBHnnHGm2', TRUE, NOW(), 1, TRUE),
       (41, 1, 'Student1', 'Demo', 'demo_student1@miniseva.com', 'demo_student1@miniseva.com', '$2a$10$9NhAigH0vdTPk1M45AVSYO1UpA0ZQSl1ce6drvP4KdPzlBHnnHGm2', TRUE, NOW(), 1, TRUE),
       (41, 1, 'Student2', 'Demo', 'demo_student2@miniseva.com', 'demo_student2@miniseva.com', '$2a$10$9NhAigH0vdTPk1M45AVSYO1UpA0ZQSl1ce6drvP4KdPzlBHnnHGm2', TRUE, NOW(), 1, TRUE),
       (41, 1, 'Teacher1', 'Demo', 'demo_teacher1@miniseva.com', 'demo_teacher1@miniseva.com', '$2a$10$9NhAigH0vdTPk1M45AVSYO1UpA0ZQSl1ce6drvP4KdPzlBHnnHGm2', TRUE, NOW(), 1, TRUE);
