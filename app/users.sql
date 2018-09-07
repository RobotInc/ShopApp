-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 07, 2018 at 06:13 PM
-- Server version: 5.7.23-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `know_where`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `uid` text NOT NULL,
  `fcm` text
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `uid`, `fcm`) VALUES
(1, 'Mohan Ravi', 'immohanravi@gmail.com', '6GURC7tJORdAd6WZ5DThvxnvdy92', 'dPupiWa3CYQ:APA91bFEWJSiIXrJlimlxpbDGYgCoQitBqx9t9Yt1Ux0z5CaJYU5_iY6FuLKYpRcuBq8F1ODmQ8VgVTBKhMC4ETF8z9lS8fDvScaFm873AGQqLykSWoP5CZ2TFwJgMjC2hJ3M2zRF5GV'),
(2, 'Ramkumar AR', 'a.r.ramkumar25@gmail.com', '3LrGeQC7hogLu6UImJGtPOcNYwG2', 'eAKI2qi3TPA:APA91bFs6ehby5lT-lY4uez1SRabyCIpCs_sNTNNUs2CAVFMrX77Imc0ULeS3JLgbchMkMAXzg-zylLvoYLV5S140grBvgNUeazPgOdi5qV1VqVIdArCvHYBLL1t4oN2YTZ0O_uD3quD'),
(3, 'Iyyappa G', 'iamiypa@gmail.com', 'jzz8HelCLceJJvuWZMFerD5uOkJ2', 'fClkKCcRA4M:APA91bFcV6S3YdNpXzWozbX6hGNuDEHZQ3lEmkFQV1qXZyMbXXQVfaOBsgV-iuEEV-vN3ij0rixuAVV5emDIvF0eijyTH5nevsDH18FBKYzUclM3oD6JzNBA6EjdDM4KAVa2R_oNYpw0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
