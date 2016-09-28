-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: 127.0.0.1    Database: event_management
-- ------------------------------------------------------
-- Server version	5.7.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `id`              INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`           VARCHAR(255)     NOT NULL,
  `short_desc`      VARCHAR(300)              DEFAULT NULL,
  `full_desc`       VARCHAR(1500)             DEFAULT NULL,
  `location`        VARCHAR(200)              DEFAULT NULL,
  `lat`             DOUBLE                    DEFAULT NULL,
  `lng`             DOUBLE                    DEFAULT NULL,
  `file_path`       VARCHAR(255)              DEFAULT NULL,
  `image_path`      VARCHAR(255)              DEFAULT NULL,
  `category_id`     INT(10) UNSIGNED NOT NULL,
  `public_accessed` TINYINT(4)       NOT NULL DEFAULT '1',
  `guests_allowed`  TINYINT(4)       NOT NULL DEFAULT '0',
  `start`           DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end`             DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creation_date`   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified`   DATETIME                  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_category_idx` (`category_id`),
  CONSTRAINT `fk_event_event_category_id` FOREIGN KEY (`category_id`) REFERENCES `event_category` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 130
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event`
  DISABLE KEYS */;
INSERT INTO `event` VALUES
  (127, 'Event1', 'Event1 short description', 'Event1 full description', 'Yerevan, Armenia', NULL, NULL, NULL, NULL,
        160, 1, 0, '2016-08-10 12:09:26', '2016-08-10 12:09:26', '2016-08-10 12:09:26', NULL),
  (128, 'Event2', 'Event2 short description', 'Event2 full description', 'Gyumri, Armenia', NULL, NULL, NULL, NULL, 161,
        1, 0, '2016-08-10 12:10:10', '2016-08-10 12:10:10', '2016-08-10 12:10:10', NULL),
  (129, 'Event3', 'Event3 short description', 'Event3 full description', 'Yerevan, Armenia', NULL, NULL, NULL, NULL,
        160, 1, 0, '2016-08-31 17:27:25', '2016-08-10 12:10:10', '2016-08-10 12:10:10', NULL);
/*!40000 ALTER TABLE `event`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_category`
--

DROP TABLE IF EXISTS `event_category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_category` (
  `id`            INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`         VARCHAR(100)     NOT NULL,
  `description`   VARCHAR(150)              DEFAULT NULL,
  `creation_date` DATETIME                  DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 163
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_category`
--

LOCK TABLES `event_category` WRITE;
/*!40000 ALTER TABLE `event_category`
  DISABLE KEYS */;
INSERT INTO `event_category`
VALUES (160, 'Category1', NULL, '2016-08-10 12:06:26'), (161, 'Category2', NULL, '2016-08-10 12:06:26'),
  (162, 'Uncatigorized', NULL, '2016-08-10 12:06:31');
/*!40000 ALTER TABLE `event_category`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_invitation`
--

DROP TABLE IF EXISTS `event_invitation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_invitation` (
  `id`              INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `event_id`        INT(10) UNSIGNED NOT NULL,
  `user_id`         INT(10) UNSIGNED NOT NULL,
  `user_role`       VARCHAR(20)      NOT NULL,
  `user_response`   VARCHAR(40)      NOT NULL,
  `attendees_count` INT(10) UNSIGNED          DEFAULT NULL,
  `participated`    TINYINT(1)                DEFAULT NULL,
  `last_modified`   DATETIME                  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_user_UNIQUE` (`event_id`, `user_id`),
  UNIQUE KEY `event_id_user_id_UNIQUE` (`event_id`, `user_id`),
  KEY `user_id_idx` (`user_id`),
  KEY `event_id_idx` (`event_id`),
  KEY `fk_event_invitation_response_id_idx` (`user_response`),
  CONSTRAINT `fk_ event_invitation_event_id` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ event_invitation_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_invitation`
--

LOCK TABLES `event_invitation` WRITE;
/*!40000 ALTER TABLE `event_invitation`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `event_invitation`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_media`
--

DROP TABLE IF EXISTS `event_media`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_media` (
  `id`            INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `event_id`      INT(10) UNSIGNED NOT NULL,
  `name`          VARCHAR(300)     NOT NULL,
  `media_type_id` INT(10) UNSIGNED NOT NULL,
  `description`   VARCHAR(300)              DEFAULT NULL,
  `uploader_id`   INT(10) UNSIGNED NOT NULL,
  `upload_date`   DATETIME         NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `path_UNIQUE` (`name`),
  KEY `event_id_idx` (`event_id`),
  KEY `uploaded_by_idx` (`uploader_id`),
  KEY `fk_event_media_media_type_id_idx` (`media_type_id`),
  CONSTRAINT `fk_event_media_event_id` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_media_media_type_id` FOREIGN KEY (`media_type_id`) REFERENCES `media_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_media_user_id` FOREIGN KEY (`uploader_id`) REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_media`
--

LOCK TABLES `event_media` WRITE;
/*!40000 ALTER TABLE `event_media`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `event_media`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_recurrence`
--

DROP TABLE IF EXISTS `event_recurrence`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_recurrence` (
  `id`                   INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `event_id`             INT(11) UNSIGNED NOT NULL,
  `recurrence_type_id`   INT(11) UNSIGNED NOT NULL,
  `repeat_interval`      INT(11) UNSIGNED NOT NULL,
  `recurrence_option_id` INT(10) UNSIGNED          DEFAULT NULL,
  `repeat_end`           DATETIME                  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `recurrence_type_event_id_UNIQUE` (`event_id`, `recurrence_type_id`),
  KEY `fk_event_recurrence_event_id_idx` (`event_id`),
  KEY `fk_event_recurrence_recurrence_option_idx` (`recurrence_option_id`),
  KEY `fk_event_recurrence_recurrence_type_id_idx` (`recurrence_type_id`),
  CONSTRAINT `fk_event_recurrence_event_id` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_recurrence_recurrence_option` FOREIGN KEY (`recurrence_option_id`) REFERENCES `recurrence_option` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_recurrence_recurrence_type` FOREIGN KEY (`recurrence_type_id`) REFERENCES `recurrence_type` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_recurrence`
--

LOCK TABLES `event_recurrence` WRITE;
/*!40000 ALTER TABLE `event_recurrence`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `event_recurrence`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_type`
--

DROP TABLE IF EXISTS `media_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_type` (
  `id`    INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45)      NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_type`
--

LOCK TABLES `media_type` WRITE;
/*!40000 ALTER TABLE `media_type`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `media_type`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurrence_option`
--

DROP TABLE IF EXISTS `recurrence_option`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurrence_option` (
  `id`                 INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `recurrence_type_id` INT(11) UNSIGNED NOT NULL,
  `title`              VARCHAR(200)     NOT NULL,
  `abbreviation`       VARCHAR(20)               DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `recurrence_type_title_UNIQUE` (`recurrence_type_id`, `title`),
  KEY `fk_repeat_on_value_rec_type_id_idx` (`recurrence_type_id`),
  CONSTRAINT `fk_repeat_on_value_rec_type_id` FOREIGN KEY (`recurrence_type_id`) REFERENCES `recurrence_type` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurrence_option`
--

LOCK TABLES `recurrence_option` WRITE;
/*!40000 ALTER TABLE `recurrence_option`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `recurrence_option`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurrence_type`
--

DROP TABLE IF EXISTS `recurrence_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurrence_type` (
  `id`            INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`         VARCHAR(200)     NOT NULL,
  `interval_unit` VARCHAR(45)      NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurrence_type`
--

LOCK TABLES `recurrence_type` WRITE;
/*!40000 ALTER TABLE `recurrence_type`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `recurrence_type`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id`                INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name`        VARCHAR(300)     NOT NULL,
  `last_name`         VARCHAR(300)     NOT NULL,
  `password`          VARCHAR(150)     NOT NULL,
  `email`             VARCHAR(100)     NOT NULL,
  `phone_number`      VARCHAR(30)               DEFAULT NULL,
  `avatar_path`       VARCHAR(300)              DEFAULT NULL,
  `verified`          TINYINT(1)                DEFAULT '0',
  `registration_date` DATETIME                  DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 359
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
  DISABLE KEYS */;
INSERT INTO `user` VALUES
  (355, 'Hermine', 'Turshujyan', '7962468883c36dc5666c3aaa3bb3d38d0900ec827c203dd53d86babf0c3094e6',
   'turshujyan@gmail.com', '', NULL, 0, '2016-08-26 12:49:15');
/*!40000 ALTER TABLE `user`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_response`
--

DROP TABLE IF EXISTS `user_response`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_response` (
  `id`    INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45)      NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_response`
--

LOCK TABLES `user_response` WRITE;
/*!40000 ALTER TABLE `user_response`
  DISABLE KEYS */;
INSERT INTO `user_response` VALUES (3, 'Maybe'), (2, 'No'), (4, 'Undefined'), (1, 'Yes');
/*!40000 ALTER TABLE `user_response`
  ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2016-08-31 17:31:31
