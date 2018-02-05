ALTER TABLE questions ADD COLUMN IF NOT EXISTS question_type character varying(8) DEFAULT 'sc';
ALTER TABLE questions ADD COLUMN IF NOT EXISTS marks smallint DEFAULT 4;
ALTER TABLE exams_responses ADD COLUMN IF NOT EXISTS answer text DEFAULT NULL;
ALTER TABLE exams_responses ADD COLUMN IF NOT EXISTS marks_obtained smallint DEFAULT 0;
ALTER TABLE exams_results ADD COLUMN IF NOT EXISTS total_marks smallint DEFAULT 0;
ALTER TABLE exams_results ADD COLUMN IF NOT EXISTS total_marks_obtained smallint DEFAULT 0;