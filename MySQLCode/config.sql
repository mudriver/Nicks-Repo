
--
-- Database: `trainers`
--

-- --------------------------------------------------------

--
-- Table structure for table `config`
--

CREATE TABLE IF NOT EXISTS `config` (
  `bi_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `v_name` varchar(255) NOT NULL,
  `v_key` varchar(50) NOT NULL,
  `v_value` varchar(50) NOT NULL,
  `d_created_date` date NOT NULL,
  PRIMARY KEY (`bi_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `config`
--

INSERT INTO `config` (`bi_id`, `v_name`, `v_key`, `v_value`, `d_created_date`) VALUES
(1, 'Staff List Administrator Return Year', 'SLARY', '2017', '2017-03-15'),
(2, 'Trainer Employee Online Return Year', 'TEORY', '2017', '2017-03-15');

