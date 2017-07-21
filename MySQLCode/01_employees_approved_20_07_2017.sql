

CREATE TABLE IF NOT EXISTS `te_employees_approved` (
  `employees_employee_id` int(11) NOT NULL AUTO_INCREMENT,
  `employees_pps_number` varchar(255) DEFAULT NULL,
  `employees_title` varchar(10) NOT NULL,
  `employees_surname` varchar(200) NOT NULL,
  `employees_firstname` varchar(200) NOT NULL,
  `employees_date_of_birth` date DEFAULT NULL,
  `employees_nationality` enum('Afghan','Algerian','Angolan','Argentine','Austrian','Australian','Bangladeshi','Belarusian','Belgian','Bolivian','Bosnian/Herzegovinian','Brazilian','British','Bulgarian','Cambodian','Cameroonian','Canadian','Central African','Chadian','Chinese','Colombian','Costa Rican','Croatian','Czech','Congolese','Danish','Ecuadorian','Egyptian','Salvadoran','English','Estonian','Ethiopian','Finnish','French','German','Ghanaian','Greek','Guatemalan','Dutch','Honduran','Hungarian','Icelandic','Indian','Indonesian','Iranian','Iraqi','Irish','Israeli','Italian','Ivorian','Jamaican','Japanese','Jordanian','Kazakh','Kenyan','Lao','Latvian','Libyan','Lithuanian','Malagasy','Malaysian','Malian','Mauritanian','Mexican','Moroccan','Namibian','Nicaraguan','Nigerien','Nigerian','Norwegian','Omani','Pakistani','Panamanian','Paraguayan','Peruvian','Philippine','Polish','Portuguese','Congolese','Romanian','Russian','Saudi, Saudi Arabian','Scottish','Senegalese','Serbian','Singaporean','Slovak','Somalian','South African','Spanish','Sudanese','Swedish','Swiss','Syrian','Thai','Tunisian','Turkish','Turkmen','Ukranian','Emirati','American','Uruguayan','Vietnamese','Welsh','Zambian','Zimbabwean','Georgia') DEFAULT NULL,
  `employees_sex` enum('Male','Female') DEFAULT NULL,
  `employees_marital_status` enum('Single','Divorced','Widowed','Separated','Married','Civil Partner','Cohabiting') DEFAULT NULL,
  `employees_spouse_name` varchar(200) DEFAULT NULL,
  `employees_address1` varchar(200) DEFAULT NULL,
  `employees_address2` varchar(200) DEFAULT NULL,
  `employees_address3` varchar(200) DEFAULT NULL,
  `employees_address4` varchar(200) DEFAULT NULL,
  `employees_address5` varchar(200) DEFAULT NULL,
  `employees_post_code` varchar(20) DEFAULT NULL,
  `employees_phone_no` varchar(20) DEFAULT NULL,
  `employees_mobile_no` varchar(20) DEFAULT NULL,
  `employees_email` varchar(200) DEFAULT NULL,
  `employees_internal_comments` text,
  `employees_comments` text,
  `employees_hri_account_no` varchar(5) DEFAULT NULL,
  `employees_card_no_temp` varchar(5) DEFAULT NULL,
  `employees_last_updated` date NOT NULL DEFAULT '2014-01-01',
  `employees_date_entered` date NOT NULL,
  `employees_is_new` tinyint(1) NOT NULL DEFAULT '0',
  `employees_has_taxable_earnings` tinyint(1) DEFAULT '0',
  `employee_verified` tinyint(1) NOT NULL DEFAULT '0',
  `employees_num_hour_worked` double DEFAULT NULL,
  `employees_last_year_paid` double DEFAULT NULL,
  `employees_category_of_employment` varchar(30) DEFAULT NULL,
  `employees_old_employee_card_number` varchar(30) DEFAULT NULL,
  `employees_existing_air_card_holder` varchar(3) DEFAULT NULL,
  `employees_request_date` date DEFAULT NULL,
  `is_approved` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`employees_employee_id`)
);


CREATE TABLE IF NOT EXISTS `te_employment_approved_history` (
  `eh_employment_id` int(11) NOT NULL AUTO_INCREMENT,
  `eh_employee_id` int(11) NOT NULL,
  `eh_trainer_id` int(11) NOT NULL,
  `eh_date_from` date NOT NULL,
  `eh_date_to` date DEFAULT NULL,
  `eh_hours_worked_temp` decimal(20,2) DEFAULT NULL,
  `eh_hours_worked` enum('Less than 8','Greater than 8') DEFAULT NULL,
  `eh_employment_category` enum('Assistant Trainer','Blacksmith / Farrier','General Maintenance','Groom','Head Lad / Girl','Horse Box Driver','Manager','Office Administrator','Race Day Help','Rider','Security','Travelling Head Lad / Girl','Yard Person') DEFAULT NULL,
  `eh_year` varchar(4) DEFAULT NULL,
  `eh_earnings` float DEFAULT NULL,
  `eh_temp_category` varchar(200) DEFAULT NULL,
  `eh_pps_number` varchar(20) DEFAULT NULL,
  `eh_pps_number_valid` tinyint(1) NOT NULL DEFAULT '0',
  `eh_verified` tinyint(1) NOT NULL DEFAULT '0',
  `employee_last_updated` date DEFAULT NULL,
  `employee_num_hour_worked` int(11) DEFAULT NULL,
  PRIMARY KEY (`eh_employment_id`),
  KEY `fk_employee_id` (`eh_employee_id`),
  KEY `fk_trainer_id` (`eh_trainer_id`)
);


CREATE TABLE IF NOT EXISTS `te_pension_approved` (
  `pension_pension_id` int(11) NOT NULL AUTO_INCREMENT,
  `pension_employee_id` int(11) NOT NULL,
  `pension_date_joined_scheme` date NOT NULL,
  `pension_date_left_scheme` date DEFAULT NULL,
  `pension_card_type` varchar(1) NOT NULL,
  `pension_type` varchar(1) NOT NULL,
  `pension_hri_acc_number` varchar(30) NOT NULL,
  `pension_trainer` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`pension_pension_id`),
  KEY `te_pension_ibfk_1` (`pension_employee_id`)
);
