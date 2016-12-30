ALTER TABLE person ADD COLUMN card_type enum('None','A','B');
ALTER TABLE person ADD COLUMN card_number varchar(5);
ALTER TABLE person ADD COLUMN trainer_name varchar(50);