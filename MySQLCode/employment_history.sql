ALTER TABLE te_employment_history add employee_num_hour_worked integer;

UPDATE te_employment_history set employee_num_hour_worked = FLOOR(40 * RAND());