----- Courses which are deleted from some of the dbs needs to be inserted again -----
INSERT INTO courses (id, name, description, slug, documents_count, exam_count, language, updated_by, updated_on, created_by, created_on)
VALUES
    (33, 'AIPMT/NEET - Biology (With Solutions)', 'AIPMT/NEET - Biology (With Solutions)', 'aipmt-neet-biology-with-solutions', 0, 297, 'english', 1, '2013-04-16 15:21:19.131319', 1, '2013-04-16 15:21:19.131319'),
    (34, 'AIPMT/NEET/AIEEE/JEE - Physics (With Solutions)', 'AIPMT/NEET/AIEEE/JEE - Physics (With Solutions)', 'aipmt-neet-aieee-jee-physics-with-solutions', 0, 171, 'english', 1, '2013-04-16 15:21:19.131320', 1, '2013-04-16 15:21:19.131320'),
    (35, 'AIPMT/NEET/AIEEE/JEE - Chemistry (With Solutions)', 'AIPMT/NEET/AIEEE/JEE - Chemistry (With Solutions)', 'aipmt-neet-aieee-jee-chemistry-with-solutions', 0, 188, 'english', 1, '2013-04-16 15:21:19.131321', 1, '2013-04-16 15:21:19.131321'),
    (36, 'AIEEE/JEE - Math (With Solutions)', 'AIEEE/JEE - Math (With Solutions)', 'aieee-jee-math-with-solutions', 0, 180, 'english', 1, '2013-04-16 15:21:19.131322', 1, '2013-04-16 15:21:19.131322'),
    (53, 'Delhi Biology XI', 'Delhi Biology XI', 'delhi-biology-xi', 44, 22, 'english', 1, '2013-04-16 15:21:19.131339', 1, '2013-04-16 15:21:19.131339'),
    (54, 'Delhi Chemistry XI', 'Delhi Chemistry XI', 'delhi-chemisty-xi', 28, 14, 'english', 1, '2013-04-16 15:21:19.131340', 1, '2013-04-16 15:21:19.131340'),
    (55, 'Delhi Mathematics XI', 'Delhi Mathematics XI', 'delhi-mathematics-xi', 32, 16, 'english', 1, '2013-04-16 15:21:19.131341', 1, '2013-04-16 15:21:19.131341'),
    (56, 'Delhi Physics XI', 'Delhi Physics XI', 'delhi-physics-xi', 30, 11, 'english', 1, '2013-04-16 15:21:19.131342', 1, '2013-04-16 15:21:19.131342'),
    (57, 'Delhi Biology XII', 'Delhi Biology XII', 'delhi-biology-xii', 32, 16, 'english', 1, '2013-04-16 15:21:19.131343', 1, '2013-04-16 15:21:19.131343'),
    (58, 'Delhi Chemistry XII', 'Delhi Chemistry XII', 'delhi-chemisty-xii', 32, 16, 'english', 1, '2013-04-16 15:21:19.131344', 1, '2013-04-16 15:21:19.131344'),
    (59, 'Delhi Mathematics XII', 'Delhi Mathematics XII', 'delhi-mathematics-xii', 26, 13, 'english', 1, '2013-04-16 15:21:19.131345', 1, '2013-04-16 15:21:19.131345'),
    (60, 'Delhi Physics XII', 'Delhi Physics XII', 'delhi-physics-xii', 30, 9, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346'),
    (61, 'Course Name', 'Dummy Course', 'Course-name', 0, 0, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346'),
    (62, 'Course Name', 'Dummy Course', 'Course-name', 0, 0, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346'),
    (63, 'Course Name', 'Dummy Course', 'Course-name', 0, 0, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346'),
    (64, 'Course Name', 'Dummy Course', 'Course-name', 0, 0, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346'),
    (65, 'Course Name', 'Dummy Course', 'Course-name', 0, 0, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346'),
    (66, 'Course Name', 'Dummy Course', 'Course-name', 0, 0, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346'),
    (67, 'Course Name', 'Dummy Course', 'Course-name', 0, 0, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346'),
    (68, 'Course Name', 'Dummy Course', 'Course-name', 0, 0, 'english', 1, '2013-04-16 15:21:19.131346', 1, '2013-04-16 15:21:19.131346')
ON CONFLICT (id) DO UPDATE SET published = true, published_on = now(), sub_category_id = 7 WHERE EXCLUDED.id IN (33, 34, 35, 36);