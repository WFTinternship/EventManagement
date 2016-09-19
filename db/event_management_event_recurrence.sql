-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: 127.0.0.1    Database: event_management
-- ------------------------------------------------------
-- Server version	5.7.15

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
-- Table structure for table `event_recurrence`
--

DROP TABLE IF EXISTS `event_recurrence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_recurrence` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(11) unsigned NOT NULL,
  `recurrence_type_id` int(11) unsigned NOT NULL,
  `repeat_interval` int(11) unsigned NOT NULL,
  `recurrence_option_id` int(10) unsigned DEFAULT NULL,
  `repeat_end` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `recurrence_type_event_id_UNIQUE` (`event_id`,`recurrence_type_id`),
  KEY `fk_event_recurrence_event_id_idx` (`event_id`),
  KEY `fk_event_recurrence_recurrence_option_idx` (`recurrence_option_id`),
  KEY `fk_event_recurrence_recurrence_type_id_idx` (`recurrence_type_id`),
  CONSTRAINT `fk_event_recurrence_event_id` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_recurrence_recurrence_option` FOREIGN KEY (`recurrence_option_id`) REFERENCES `recurrence_option` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_recurrence_recurrence_type` FOREIGN KEY (`recurrence_type_id`) REFERENCES `recurrence_type` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_recurrence`
--

LOCK TABLES `event_recurrence` WRITE;
/*!40000 ALTER TABLE `event_recurrence` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_recurrence` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-19 19:13:04
