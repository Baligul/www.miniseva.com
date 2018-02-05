INSERT INTO sub_categories
       (name, description, slug, category_id, duration, price, sort_order, created_on, created_by)
VALUES
       ('Grade VII', 'Grade VII', 'grade-vii', 1, '12 Months', 3500, 1, NOW(), 1),
       ('Grade VIII', 'Grade VIII', 'grade-viii', 1, '12 Months', 3500, 2, NOW(), 1),
       ('Grade IX', 'Grade IX', 'grade-ix', 1, '12 Months', 3500, 3, NOW(), 1),
       ('Grade X', 'Grade X', 'grade-x', 1, '12 Months', 3500, 4, NOW(), 1),
       ('Grade XI', 'Grade XI', 'grade-xi', 1, '12 Months', 3500, 5, NOW(), 1),
       ('Grade XII', 'Grade XII', 'grade-xii', 1, '12 Months', 3500, 6, NOW(), 1),
       ('Competitive Exams', 'Competitive Exams', 'competitive-exams', 1, '12 Months', 3500, 7, NOW(), 1);

-- update categories count --
UPDATE categories SET sub_categories_count = (SELECT COUNT (id) FROM sub_categories WHERE category_id = 1) WHERE id = 1;