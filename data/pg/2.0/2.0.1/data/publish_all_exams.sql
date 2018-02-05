----- Publish all Exams -----
UPDATE exams SET published = true;
UPDATE exams SET published_on = NOW();