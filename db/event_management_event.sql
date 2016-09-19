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
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `short_desc` varchar(300) DEFAULT NULL,
  `full_desc` varchar(1500) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `category_id` int(10) unsigned NOT NULL,
  `public_accessed` tinyint(4) NOT NULL DEFAULT '1',
  `guests_allowed` tinyint(4) NOT NULL DEFAULT '0',
  `start` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_category_idx` (`category_id`),
  CONSTRAINT `fk_event_event_category_id` FOREIGN KEY (`category_id`) REFERENCES `event_category` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'Event1','Event1 short description','Event1 full description','Yerevan, Armenia',NULL,NULL,NULL,NULL,160,1,0,'2016-08-10 12:09:26','2016-08-10 12:09:26','2016-08-10 12:09:26',NULL),(2,'Event2','Event2 short description','Event2 full description','Gyumri, Armenia',NULL,NULL,NULL,NULL,161,1,0,'2016-08-10 12:10:10','2016-08-10 12:10:10','2016-08-10 12:10:10',NULL),(3,'Event3','Event3 short description','Event3 full description','Yerevan, Armenia',NULL,NULL,NULL,NULL,160,1,0,'2016-08-31 17:27:25','2016-08-10 12:10:10','2016-08-10 12:10:10',NULL),(4,'Event4','Event1 short description','Event1 full description','Yerevan, Armenia',NULL,NULL,NULL,NULL,160,1,0,'2016-08-10 12:09:26','2016-08-10 12:09:26','2016-08-10 12:09:26',NULL),(5,'Event5','Event2 short description','Event2 full description','Gyumri, Armenia',NULL,NULL,NULL,NULL,161,1,0,'2016-08-10 12:10:10','2016-08-10 12:10:10','2016-08-10 12:10:10',NULL),(6,'Event6','Event3 short description','Event3 full description','Yerevan, Armenia',NULL,NULL,NULL,NULL,160,1,0,'2016-08-31 17:27:25','2016-08-10 12:10:10','2016-08-10 12:10:10',NULL),(130,'mmm','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-01-15 09:45:00','2016-01-20 09:45:00','2016-09-19 13:28:05',NULL),(131,'mmm','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-01-15 09:45:00','2016-01-20 09:45:00','2016-09-19 13:29:15',NULL),(132,'ttttt','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-01-01 09:15:00','2016-01-14 09:45:00','2016-09-19 13:34:37',NULL),(133,'ascasc','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-01-08 10:00:00','2016-01-07 10:00:00','2016-09-19 13:40:24',NULL),(134,'vsadv','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-01-01 09:45:00','2016-01-06 09:45:00','2016-09-19 13:42:04',NULL),(135,'ewgwge','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-01-08 09:45:00','2016-01-22 10:00:00','2016-09-19 13:47:10',NULL),(136,'asf','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-09-08 09:45:00','2016-09-20 09:30:00','2016-09-19 13:54:09',NULL),(137,'asf','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-09-08 09:45:00','2016-09-20 09:30:00','2016-09-19 13:54:41',NULL),(138,'sdg','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-09-01 09:45:00','2016-09-28 09:30:00','2016-09-19 13:57:26',NULL),(139,'asf','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-09-01 09:45:00','2016-09-06 10:00:00','2016-09-19 13:59:21',NULL),(140,'jhvjhg','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-09-22 09:30:00','2016-09-13 09:45:00','2016-09-19 17:21:22',NULL),(141,'jhvjhg   ','                        ','                        ','',0,0,NULL,NULL,160,0,0,'2016-09-22 09:30:00','2016-09-13 09:45:00','2016-09-19 17:26:07',NULL);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
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
