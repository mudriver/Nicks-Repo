ALTER TABLE `te_employment_history`  
	ADD `card_type` VARCHAR(1) NOT NULL AFTER `employee_num_hour_worked`,  
	ADD `pension_type` VARCHAR(1) NOT NULL AFTER `card_type`,  
	ADD `hri_acc_number` VARCHAR(30) NOT NULL AFTER `pension_type`;

ALTER TABLE `te_pension`  
	ADD `employment_category` ENUM('Assistant Trainer','Blacksmith / Farrier','General Maintenance','Groom','Head Lad / Girl','Horse Box Driver','Manager','Office Administrator','Race Day Help','Rider','Security','Travelling Head Lad / Girl','Yard Person') NOT NULL AFTER `pension_trainer`;

ALTER TABLE `te_pension` 
	CHANGE `pension_card_type` `pension_card_type` VARCHAR(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL, 
	CHANGE `pension_type` `pension_type` VARCHAR(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL, 
	CHANGE `pension_hri_acc_number` `pension_hri_acc_number` VARCHAR(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL;



ALTER TABLE `te_employment_approved_history`  
	ADD `card_type` VARCHAR(1) NOT NULL AFTER `employee_num_hour_worked`,  
	ADD `pension_type` VARCHAR(1) NOT NULL AFTER `card_type`,  
	ADD `hri_acc_number` VARCHAR(30) NOT NULL AFTER `pension_type`;

ALTER TABLE `te_pension_approved`  
	ADD `employment_category` ENUM('Assistant Trainer','Blacksmith / Farrier','General Maintenance','Groom','Head Lad / Girl','Horse Box Driver','Manager','Office Administrator','Race Day Help','Rider','Security','Travelling Head Lad / Girl','Yard Person') NOT NULL AFTER `pension_trainer`;

ALTER TABLE `te_pension_approved` 
	CHANGE `pension_card_type` `pension_card_type` VARCHAR(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL, 
	CHANGE `pension_type` `pension_type` VARCHAR(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL, 
	CHANGE `pension_hri_acc_number` `pension_hri_acc_number` VARCHAR(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL;