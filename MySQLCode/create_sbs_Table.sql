
CREATE TABLE IF NOT EXISTS `sbs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mail_to` varchar(50) NOT NULL,
  `sbs_name` varchar(100) NOT NULL,
  `address1` varchar(100) NOT NULL,
  `address2` varchar(100) NOT NULL,
  `address3` varchar(100) NOT NULL,
  `address4` varchar(100) NOT NULL,
  `title` varchar(10) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `amount` double NOT NULL,
  `eft` tinyint(1) NOT NULL,
  `trainer_id` varchar(20) NOT NULL,
  `sbs_ac` varchar(10) NOT NULL,
  `sur_n` varchar(100) DEFAULT NULL,
  `first_n` varchar(100) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1058 ;
