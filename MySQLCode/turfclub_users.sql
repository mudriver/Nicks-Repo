-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 15, 2016 at 11:12 AM
-- Server version: 5.5.34
-- PHP Version: 5.3.10-1ubuntu3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `turfclub_users`
--

-- --------------------------------------------------------

--
-- Table structure for table `user_roles`
--

CREATE TABLE IF NOT EXISTS `user_roles` (
  `role_id` int(20) NOT NULL AUTO_INCREMENT,
  `role_user_id` int(20) NOT NULL,
  `role_roletype_id` int(20) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

--
-- Dumping data for table `user_roles`
--

INSERT INTO `user_roles` (`role_id`, `role_user_id`, `role_roletype_id`) VALUES
(2, 2, 2),
(4, 2, 3),
(5, 3, 6),
(6, 4, 4),
(7, 6, 7),
(8, 7, 5),
(9, 1, 8),
(10, 6, 11),
(11, 7, 9),
(12, 8, 9),
(13, 8, 10),
(14, 9, 11);

-- --------------------------------------------------------

--
-- Table structure for table `user_role_types`
--

CREATE TABLE IF NOT EXISTS `user_role_types` (
  `role_type_id` int(20) NOT NULL AUTO_INCREMENT,
  `role_type_name` varchar(200) NOT NULL,
  `role_description` text,
  PRIMARY KEY (`role_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `user_role_types`
--

INSERT INTO `user_role_types` (`role_type_id`, `role_type_name`, `role_description`) VALUES
(1, 'VET_USER', NULL),
(2, 'VET_ADMIN', NULL),
(4, 'INSPECTIONS_CEO', 'Can only enter or edit categories assigned to the inspections CEO group. Can see all inspections entered by any group marked as scheduled or completed. '),
(5, 'INSPECTIONS_LICENCING', 'Can only enter or edit inspections assigned to INSPECTIONS_LICENCING Group. Can see all inspections marked as complete and only scheduled inspections entered by members of this group'),
(6, 'ACCOUNTS', NULL),
(7, 'TRAINERS_USER', 'A view only list of trainers and details associated with same'),
(8, 'INSPECTIONS_ADMIN', 'Same as inspections CEO except hidden status available to select called scheduled(confidential). This allows this user to schedule inspections which can not be seen by any other user until marked as complete'),
(9, 'TRAINERS_ADMIN', 'Allowed to edit trainers details and change licence types, employees etc'),
(10, 'STABLESTAFF', 'Can view trainers but only edit the details of staff associated with each trainer'),
(11, 'STABLESTAFF_PENSION', 'Same as stablestaff except can edit pension earnings details associated with each employee also');

-- --------------------------------------------------------

--
-- Table structure for table `user_users`
--

CREATE TABLE IF NOT EXISTS `user_users` (
  `users_id` int(11) NOT NULL AUTO_INCREMENT,
  `users_first_name` varchar(200) NOT NULL,
  `users_surname` varchar(200) NOT NULL,
  `users_email` varchar(200) NOT NULL,
  `users_pwd` text NOT NULL,
  PRIMARY KEY (`users_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `user_users`
--

INSERT INTO `user_users` (`users_id`, `users_first_name`, `users_surname`, `users_email`, `users_pwd`) VALUES
(1, 'Ann', 'Whelan', 'awhelan@turfclub.ie', '$2a$10$wZA35NxRjkjdJfG12ydu9OH3RtZIF1tz.Wh7MNp5mk8Fyv5XDaK3a'),
(2, 'Ray', 'Bergin', 'ray.bergin@turfclub.ie', '$2a$10$kikePczfPB4lvNggsKuA9.KUERhzI1Sv9qnkwmeluKOyehqObGLUi'),
(3, 'Sandra', 'Heavey', 'sandra.heavey@turfclub.ie', '$2a$10$z.FYE7ElNYOGxu1ig8SSs.Q/KGpLb3TkPzGQPzM9nDJuaHjTdb1Wq'),
(4, 'Michelle', 'Byrne', 'michelle.byrne@turfclub.ie', '$2a$10$6G9KZ9aLOIrUVLQb1i3pYuy0N7SLl5rsVdnYQQZMibeV/eMovvyb6'),
(6, 'Tricia', 'Cullen', 'tricia.cullen@turfclub.ie', '$2a$10$XCyJosWqibL18Xve/QrAgeJbMBXo0U4JR7ZAQLoFnHTidKGRPGRwO'),
(7, 'Angie', 'Coakley', 'angela.coakley@turfclub.ie', '$2a$10$YuePWdgyoKBhDZTnNDkBPeUefr9aEFO.UaXypfHzGgjP/UpNkfWw6'),
(8, 'Sally', 'Wilson', 'sally.wilson@turfclub.ie', '$2a$10$k7pUhfYbKMrAnGrKGvZ8IuHEgJMlQdxvxpW7rkQRu/vMS9ilJ3R0u'),
(9, 'HRI', 'Asst', 'hri@turfclub.ie', '$2a$10$Ki4f.7ANUho60rUv.WK8JO0SPZzStzMvC1jL7bnd3WfXSJwYabJMC');

-- --------------------------------------------------------

--
-- Stand-in structure for view `user_users_with_roles`
--
CREATE TABLE IF NOT EXISTS `user_users_with_roles` (
`role_id` int(20)
,`users_email` varchar(200)
,`role_type_name` varchar(200)
);
-- --------------------------------------------------------

--
-- Structure for view `user_users_with_roles`
--
DROP TABLE IF EXISTS `user_users_with_roles`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `user_users_with_roles` AS select `user_roles`.`role_id` AS `role_id`,`user_users`.`users_email` AS `users_email`,`user_role_types`.`role_type_name` AS `role_type_name` from ((`user_roles` join `user_role_types` on((`user_role_types`.`role_type_id` = `user_roles`.`role_roletype_id`))) join `user_users` on((`user_roles`.`role_user_id` = `user_users`.`users_id`)));

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
