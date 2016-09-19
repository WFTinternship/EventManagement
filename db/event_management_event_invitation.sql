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
-- Table structure for table `event_invitation`
--

DROP TABLE IF EXISTS `event_invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_invitation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `user_role` varchar(20) NOT NULL,
  `user_response_id` int(11) NOT NULL,
  `attendees_count` int(10) unsigned DEFAULT NULL,
  `participated` tinyint(1) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_user_UNIQUE` (`event_id`,`user_id`),
  UNIQUE KEY `event_id_user_id_UNIQUE` (`event_id`,`user_id`),
  KEY `user_id_idx` (`user_id`),
  KEY `event_id_idx` (`event_id`),
  KEY `fk_event_invitation_response_id_idx` (`user_response_id`),
  CONSTRAINT `fk_ event_invitation_event_id` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_ event_invitation_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_invitation`
--

LOCK TABLES `event_invitation` WRITE;
/*!40000 ALTER TABLE `event_invitation` DISABLE KEYS */;
INSERT INTO `event_invitation` VALUES (1,131,1,'ORGANIZER',1,1,0,NULL),(2,131,2,'MEMBER',1,1,0,NULL),(3,1,1,'ORGANIZER',1,1,0,NULL),(4,2,2,'ORGANIZER',2,1,0,NULL),(5,132,2,'MEMBER',1,1,0,NULL),(6,132,1,'ORGANIZER',1,1,0,NULL),(7,133,2,'MEMBER',5,1,0,NULL),(8,133,1,'ORGANIZER',4,1,0,NULL),(9,134,2,'MEMBER',5,1,0,NULL),(10,134,1,'ORGANIZER',4,1,0,NULL),(11,135,1,'ORGANIZER',5,1,0,NULL),(12,136,1,'ORGANIZER',5,1,0,NULL),(13,137,1,'ORGANIZER',5,1,0,NULL),(14,137,2,'MEMBER',1,1,0,NULL),(15,138,1,'ORGANIZER',5,1,0,NULL),(16,139,1,'ORGANIZER',5,1,0,NULL),(17,140,1,'ORGANIZER',5,1,0,NULL),(18,141,1,'ORGANIZER',1,1,0,NULL),(20,2,1,'MEMBER',1,1,0,NULL);
/*!40000 ALTER TABLE `event_invitation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-19 19:13:03
