/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2020-03-27 16:56:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `library_book`
-- ----------------------------
DROP DATABASE IF EXISTS `test`;
CREATE DATABASE `test`;
USE test;

DROP TABLE IF EXISTS `library_book`;
CREATE TABLE `library_book` (
  `book_id` bigint(20) NOT NULL,
  `book_name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of library_book
-- ----------------------------
INSERT INTO `library_book` VALUES ('1', 'demoData', '1', '1', '2020-01-06 15:11:33', null);
INSERT INTO `library_book` VALUES ('2', 'demoDat2', '2', '2', '2020-01-06 15:11:34', null);
