----- Update total_marks and total_marks_obtained in exams_results -----
UPDATE exams_results SET total_marks = total_questions * 4, total_marks_obtained = correct_answers * 4;