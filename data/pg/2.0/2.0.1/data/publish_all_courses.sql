----- Publish all Courses -----
UPDATE courses SET published = true;
UPDATE courses SET published_on = NOW();
