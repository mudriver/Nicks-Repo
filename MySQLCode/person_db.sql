CREATE DATABASE  IF NOT EXISTS `person_db` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `person_db`;
-- MySQL dump 10.13  Distrib 5.5.43, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: person_db
-- ------------------------------------------------------
-- Server version	5.5.43-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pps_number` varchar(8) DEFAULT NULL,
  `title` varchar(10) NOT NULL,
  `surname` varchar(200) NOT NULL,
  `firstname` varchar(200) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `nationality` enum('Afghan','Algerian','Angolan','Argentine','Austrian','Australian','Bangladeshi','Belarusian','Belgian','Bolivian','Bosnian/Herzegovinian','Brazilian','British','Bulgarian','Cambodian','Cameroonian','Canadian','Central African','Chadian','Chinese','Colombian','Costa Rican','Croatian','Czech','Congolese','Danish','Ecuadorian','Egyptian','Salvadoran','English','Estonian','Ethiopian','Finnish','French','German','Ghanaian','Greek','Guatemalan','Dutch','Honduran','Hungarian','Icelandic','Indian','Indonesian','Iranian','Iraqi','Irish','Israeli','Italian','Ivorian','Jamaican','Japanese','Jordanian','Kazakh','Kenyan','Lao','Latvian','Libyan','Lithuanian','Malagasy','Malaysian','Malian','Mauritanian','Mexican','Moroccan','Namibian','Nicaraguan','Nigerien','Nigerian','Norwegian','Omani','Pakistani','Panamanian','Paraguayan','Peruvian','Philippine','Polish','Portuguese','Congolese','Romanian','Russian','Saudi, Saudi Arabian','Scottish','Senegalese','Serbian','Singaporean','Slovak','Somalian','South African','Spanish','Sudanese','Swedish','Swiss','Syrian','Thai','Tunisian','Turkish','Turkmen','Ukranian','Emirati','American','Uruguayan','Vietnamese','Welsh','Zambian','Zimbabwean','Georgia') DEFAULT NULL,
  `sex` enum('Male','Female') DEFAULT NULL,
  `marital_status` enum('Single','Divorced','Widowed','Separated','Married','Civil Partner','Cohabiting') DEFAULT NULL,
  `spouse_name` varchar(200) DEFAULT NULL,
  `address1` varchar(200) DEFAULT NULL,
  `address2` varchar(200) DEFAULT NULL,
  `address3` varchar(200) DEFAULT NULL,
  `address4` varchar(200) DEFAULT NULL,
  `address5` varchar(200) DEFAULT NULL,
  `postcode` varchar(20) DEFAULT NULL,
  `phone_no` varchar(20) DEFAULT NULL,
  `mobile_no` varchar(20) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `comments` text,
  `hri_account_no` varchar(5) DEFAULT NULL,
  `last_updated` date DEFAULT NULL,
  `date_entered` date DEFAULT NULL,
  `is_new` tinyint(1) DEFAULT NULL,
  `has_taxable_earnings` tinyint(1) DEFAULT NULL,
  `employee_verified` tinyint(1) DEFAULT NULL,
  `request_date` date DEFAULT NULL,
  `existing_air_card_holder` varchar(3) DEFAULT NULL,
  `old_employee_card_holder` varchar(30) DEFAULT NULL,
  `category_of_employment` varchar(30) DEFAULT NULL,
  `last_year_paid` double DEFAULT NULL,
  `num_hour_worked` double DEFAULT NULL,
  `batch_no` int(20) DEFAULT NULL,
  `acc_no` varchar(20) DEFAULT NULL,
  `stable_address1` varchar(200) DEFAULT NULL,
  `stable_address2` varchar(200) DEFAULT NULL,
  `stable_address3` varchar(200) DEFAULT NULL,
  `fax` varchar(200) DEFAULT NULL,
  `restricted` varchar(200) DEFAULT NULL,
  `hunter_chase` varchar(200) DEFAULT NULL,
  `curragh` varchar(200) DEFAULT NULL,
  `insurance_expiry` date DEFAULT NULL,
  `last_racecard_id` int(20) DEFAULT NULL,
  `return_completed` tinyint(1) DEFAULT NULL,
  `contact_name` varchar(200) DEFAULT NULL,
  `contact_number` varchar(20) DEFAULT NULL,
  `verified_status` enum('NOTVERIFIED','PENDING','VERIFIED') DEFAULT NULL,
  `notes` text,
  `role` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-22 14:49:12
