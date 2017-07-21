
CREATE TABLE IF NOT EXISTS `te_cards_approved` (
  `cards_card_id` int(11) NOT NULL AUTO_INCREMENT,
  `cards_employee_id` int(11) NOT NULL,
  `cards_card_number` varchar(5) NOT NULL,
  `cards_issue_date` date DEFAULT NULL,
  `cards_card_returned_to_office` tinyint(1) DEFAULT NULL,
  `cards_previous_air_card_holder` tinyint(1) DEFAULT NULL,
  `cards_card_type` enum('None','A','B') NOT NULL DEFAULT 'None',
  `cards_card_status` enum('Applied','Issued') NOT NULL DEFAULT 'Applied',
  PRIMARY KEY (`cards_card_id`),
  KEY `te_cards_ibfk_1` (`cards_employee_id`)
);
