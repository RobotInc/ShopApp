-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 01, 2018 at 10:47 AM
-- Server version: 10.1.35-MariaDB
-- PHP Version: 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shop`
--

-- --------------------------------------------------------

--
-- Table structure for table `CUSTOMERS`
--

CREATE TABLE `CUSTOMERS` (
  `customerID` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `gstin` varchar(250) NOT NULL,
  `registered` tinyint(1) DEFAULT '1',
  `state` varchar(250) NOT NULL,
  `address` varchar(250) NOT NULL,
  `pincode` varchar(10) NOT NULL,
  `phone` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CUSTOMERS`
--

INSERT INTO `CUSTOMERS` (`customerID`, `name`, `gstin`, `registered`, `state`, `address`, `pincode`, `phone`) VALUES
(3, 'Mm textiles', '83jsjjsmsmsjssm', 0, 'Andhra Pradesh', 'pamidi', '515775', '9505953126');

-- --------------------------------------------------------

--
-- Table structure for table `INVOICE`
--

CREATE TABLE `INVOICE` (
  `invoiceID` int(11) NOT NULL,
  `invoice_no` int(11) NOT NULL,
  `customerid1` int(11) NOT NULL,
  `customerid2` int(11) NOT NULL,
  `invoice_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `transport` varchar(250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `INVOICEITEM`
--

CREATE TABLE `INVOICEITEM` (
  `invoiceID` int(11) NOT NULL,
  `productID` int(11) NOT NULL,
  `qty` int(11) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PRODUCTS`
--

CREATE TABLE `PRODUCTS` (
  `productID` int(11) NOT NULL,
  `name` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `PRODUCTS`
--

INSERT INTO `PRODUCTS` (`productID`, `name`) VALUES
(1, 'Fancy'),
(2, 'Kalyani');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `CUSTOMERS`
--
ALTER TABLE `CUSTOMERS`
  ADD PRIMARY KEY (`customerID`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `gstin` (`gstin`),
  ADD UNIQUE KEY `gstin_2` (`gstin`),
  ADD UNIQUE KEY `name_2` (`name`);

--
-- Indexes for table `INVOICE`
--
ALTER TABLE `INVOICE`
  ADD PRIMARY KEY (`invoiceID`),
  ADD UNIQUE KEY `invoice_no` (`invoice_no`),
  ADD KEY `customerID` (`customerid1`);

--
-- Indexes for table `INVOICEITEM`
--
ALTER TABLE `INVOICEITEM`
  ADD KEY `invoiceID` (`invoiceID`),
  ADD KEY `productID` (`productID`);

--
-- Indexes for table `PRODUCTS`
--
ALTER TABLE `PRODUCTS`
  ADD PRIMARY KEY (`productID`),
  ADD UNIQUE KEY `name` (`name`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `CUSTOMERS`
--
ALTER TABLE `CUSTOMERS`
  MODIFY `customerID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `INVOICE`
--
ALTER TABLE `INVOICE`
  MODIFY `invoiceID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `PRODUCTS`
--
ALTER TABLE `PRODUCTS`
  MODIFY `productID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `INVOICE`
--
ALTER TABLE `INVOICE`
  ADD CONSTRAINT `INVOICE_ibfk_1` FOREIGN KEY (`customerid1`) REFERENCES `CUSTOMERS` (`customerID`);

--
-- Constraints for table `INVOICEITEM`
--
ALTER TABLE `INVOICEITEM`
  ADD CONSTRAINT `INVOICEITEM_ibfk_1` FOREIGN KEY (`invoiceID`) REFERENCES `INVOICE` (`invoiceID`),
  ADD CONSTRAINT `INVOICEITEM_ibfk_2` FOREIGN KEY (`productID`) REFERENCES `PRODUCTS` (`productID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
