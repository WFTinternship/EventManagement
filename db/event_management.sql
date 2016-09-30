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
  `short_desc` varchar(500) DEFAULT NULL,
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
  `organizer_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `event_category_idx` (`category_id`),
  KEY `kf_event_organizer_id_idx` (`organizer_id`),
  CONSTRAINT `fk_event_event_category_id` FOREIGN KEY (`category_id`) REFERENCES `event_category` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `kf_event_organizer_id` FOREIGN KEY (`organizer_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (3,'Summer team event with family members','New Lorem ipsum dolor sit amet, consectetur adipisicing elit. Non a voluptatibus, ex odit totam cumque nihil','New Lorem ipsum dolor sit amet, consectetur adipisicing elit. Non a voluptatibus, ex odit totam cumque nihil eos asperiores ea, labore rerum. Doloribus tenetur quae impedit adipisci, laborum dolorum eaque ratione quaerat, eos dicta consequuntur atque ex facere voluptate cupiditate incidunt.\r\n\r\nNew Lorem ipsum dolor sit amet, consectetur adipisicing elit. Non a voluptatibus, ex odit totam cumque nihil eos asperiores ea, labore rerum. Doloribus tenetur quae impedit adipisci, laborum dolorum eaque ratione quaerat, eos dicta consequuntur atque ex facere voluptate cupiditate incidunt.','Park Resort Aghveran, H5, Aghveran, Kotayk Province, Armenia',40.489091,44.60065069999996,NULL,'1a146a64-b8c0-4b10-97d1-e95e8e2a7959.jpg',162,0,1,'2016-09-21 00:00:00','2016-09-21 23:59:00','2016-08-10 12:10:10','2016-09-30 13:23:03',2),(6,'Armaghan Mount','Armaghan is an extinct volcano (2829 m), it is  a natural monument of Armenia, in Gegharkunik Region. But there is very little information about it. Maybe it is for the best. Maybe that is why it kept its primary state.','Armaghan Mountain is located in Gegharkunik region. The altitude is 2829 m. There is a lake on the peak of the mount: diameter Ã?????Ã????Ã???Ã??Ã?Â¢?? 50 m., depth Ã?????Ã????Ã???Ã??Ã?Â¢?? 15 m.  The mount is a part of the Geghama Mountains. The volcanic crater of the peak is partially filled with water. This little lake is surrounded by alpine vegetation. \r\nIn 2009 a new domed basalt church was built instead of the old chapel. According to the legend, sometimes celestial fairies come here to drink water from the Lake Kari, and at that moment the peak of the mount is covered by for making them invisible to people. \r\nDue to the attractive force and lovely environment time flies very fast. Anyway, it is better to return earlier: at least 2 hours before sunset. \r\nLocals say that Armaghan Mount is God\'s gift, and having once been here, you will certainly return.','Mount Armaghan, Gegharkunik Province, Armenia',40.0709453,45.21379890000003,NULL,'399ee497-1708-46d1-8264-474dac613540.jpg',160,1,1,'2016-10-08 08:00:00','2016-10-08 20:00:00','2016-08-10 12:10:10','2016-09-30 13:16:17',1),(146,'Hiking in Vayots Dzor','Route - Vayots Dzor, Hors town, Hors lake, Aghavnadzor village\r\nDistance - 16 km\r\nFrom Yerevan - 150 km\r\nDuration - 7-8 hours','Vayots Dzor region is situated in the south- eastern part of RA. It has the fewest number of population in Armenia, though the region has been populated since ancient times. This can be proved by many objects found in this region which belong to the period of primitive man, such as daggers, knives, bracelets, rings and so on. There are many hieroglyphs on the rocks, showing the scenes of hunting. The specialists claim that the territory of Vayots Dzor has been populated for more than 5500 years. Archeologists have found an 5500-years-old-leather-shoe here called \"trekh\".\r\nThis is the oldest shoe found in the world, and now it is in the historical museum of RA.\r\n\r\nThe variety of landscapes and reliefs, the variety of flora and fauna are typical to this region. The altitude has the range between 850m (Areni) and 3522m (Mount Vardenis). The climate is dry. The precipitation varies from 300 to 800 mm, depending on the altitude. The main landscapes are semi-deserts and steppes.\r\nFlora and fauna of this region are quite rich. There are over 1650 species of plants. Though there are not many forests, there are 150 species of trees. Famous bezoar goats, wild mouflons are typical to this region. Sometimes you can find also poisonous snakes such as Armenian viper.','Vayots Dzor Province, Armenia',39.7641996,45.33375279999996,NULL,NULL,160,1,1,'2016-06-11 08:00:00','2016-07-11 20:00:00','2016-09-22 11:13:23','2016-09-30 13:23:30',1),(173,'Team event in Akunqi Draxt','We are planning team event - fun time in Park Resort Aghveran with own-made barbecue, for July 9, Saturday. Please sign up who will join to get prepared accordingly. Please sign up till the end of tomorrow (July 1). \r\nThere was a decision to spend some time in Akunqi Draxt on July 9th, Saturday due to weather forecast fully booked season in Aghveran.','','Akunki Drakht Restaurant, Kotayk Province, Armenia',40.26140199999999,44.67835000000002,NULL,'5e99d4cc-39d0-4d3a-96bf-3ec09420c0e5.JPG',162,1,0,'2016-07-09 00:00:00','2016-07-09 23:59:00','2016-09-30 13:32:57','2016-09-30 13:33:36',3);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_category`
--

DROP TABLE IF EXISTS `event_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` varchar(150) DEFAULT NULL,
  `creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=164 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_category`
--

LOCK TABLES `event_category` WRITE;
/*!40000 ALTER TABLE `event_category` DISABLE KEYS */;
INSERT INTO `event_category` VALUES (160,'Hiking',NULL,'2016-08-10 12:06:26'),(161,'Service day',NULL,'2016-08-10 12:06:26'),(162,'Team events',NULL,'2016-08-10 12:06:31'),(163,'Uncategorized',NULL,'2016-09-30 12:18:53');
/*!40000 ALTER TABLE `event_category` ENABLE KEYS */;
UNLOCK TABLES;

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
  `user_response_id` int(11) unsigned NOT NULL,
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
  CONSTRAINT `fk_ event_invitation_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_invitation_response_id` FOREIGN KEY (`user_response_id`) REFERENCES `user_response` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_invitation`
--

LOCK TABLES `event_invitation` WRITE;
/*!40000 ALTER TABLE `event_invitation` DISABLE KEYS */;
INSERT INTO `event_invitation` VALUES (94,146,1,1,1,0,NULL),(135,146,2,1,1,0,NULL),(152,3,1,3,1,0,NULL),(153,3,2,3,1,0,NULL),(156,6,18,5,1,0,NULL),(157,6,3,5,1,0,NULL),(160,6,1,3,1,0,NULL),(161,173,1,5,1,0,NULL),(162,173,2,5,1,0,NULL),(163,173,3,2,1,0,NULL),(164,173,18,5,1,0,NULL);
/*!40000 ALTER TABLE `event_invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_media`
--

DROP TABLE IF EXISTS `event_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_media` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(10) unsigned NOT NULL,
  `name` varchar(300) NOT NULL,
  `media_type_id` int(10) unsigned NOT NULL,
  `description` varchar(300) DEFAULT NULL,
  `uploader_id` int(10) unsigned NOT NULL,
  `upload_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `path_UNIQUE` (`name`),
  KEY `event_id_idx` (`event_id`),
  KEY `uploaded_by_idx` (`uploader_id`),
  KEY `fk_event_media_media_type_id_idx` (`media_type_id`),
  CONSTRAINT `fk_event_media_event_id` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_media_user_id` FOREIGN KEY (`uploader_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `kf_event_media_media_type_id` FOREIGN KEY (`media_type_id`) REFERENCES `media_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_media`
--

LOCK TABLES `event_media` WRITE;
/*!40000 ALTER TABLE `event_media` DISABLE KEYS */;
INSERT INTO `event_media` VALUES (9,3,'3e80e070-1229-4c7f-bb69-fa7df33554f2.jpg',1,NULL,1,'2016-09-28 13:37:30'),(10,3,'85853ce4-c2d7-480e-aa72-611efd6e33be.jpg',1,NULL,2,'2016-09-28 13:37:30'),(11,3,'855ed131-eff1-446b-a7c4-ba5d12460049.jpg',1,NULL,1,'2016-09-28 15:37:55'),(12,3,'12e680ac-5420-4d35-bf46-aa4ca907bcd5.jpg',1,NULL,1,'2016-09-28 15:37:55'),(28,146,'1dd853ae-2c0d-49c9-b03a-f2b2e62fd73f.jpg',1,NULL,1,'2016-09-30 12:51:18'),(29,146,'8ddd42af-3d81-44b9-8b8e-794faedd5527.jpeg',1,NULL,1,'2016-09-30 12:53:49'),(30,146,'66f22e10-c0a0-4218-bb4d-ef8c5b2fb4bc.jpg',1,NULL,2,'2016-09-30 12:55:08');
/*!40000 ALTER TABLE `event_media` ENABLE KEYS */;
UNLOCK TABLES;

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

--
-- Table structure for table `media_type`
--

DROP TABLE IF EXISTS `media_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_type`
--

LOCK TABLES `media_type` WRITE;
/*!40000 ALTER TABLE `media_type` DISABLE KEYS */;
INSERT INTO `media_type` VALUES (1,'image'),(2,'video');
/*!40000 ALTER TABLE `media_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurrence_option`
--

DROP TABLE IF EXISTS `recurrence_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurrence_option` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `recurrence_type_id` int(11) unsigned NOT NULL,
  `title` varchar(200) NOT NULL,
  `abbreviation` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `recurrence_type_title_UNIQUE` (`recurrence_type_id`,`title`),
  KEY `fk_repeat_on_value_rec_type_id_idx` (`recurrence_type_id`),
  CONSTRAINT `fk_repeat_on_value_rec_type_id` FOREIGN KEY (`recurrence_type_id`) REFERENCES `recurrence_type` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurrence_option`
--

LOCK TABLES `recurrence_option` WRITE;
/*!40000 ALTER TABLE `recurrence_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurrence_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurrence_type`
--

DROP TABLE IF EXISTS `recurrence_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurrence_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `interval_unit` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurrence_type`
--

LOCK TABLES `recurrence_type` WRITE;
/*!40000 ALTER TABLE `recurrence_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurrence_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(300) NOT NULL,
  `last_name` varchar(300) NOT NULL,
  `password` varchar(150) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(30) DEFAULT NULL,
  `avatar_path` varchar(300) DEFAULT NULL,
  `verified` tinyint(1) DEFAULT '0',
  `registration_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Hermine','Turshujyan','7962468883c36dc5666c3aaa3bb3d38d0900ec827c203dd53d86babf0c3094e6','turshujyan@gmail.com','',NULL,0,'2016-08-26 12:49:15'),(2,'Anna','Asmangulyan','7962468883c36dc5666c3aaa3bb3d38d0900ec827c203dd53d86babf0c3094e6','annaasmangulyan@workfront.com',NULL,NULL,0,'2016-08-26 12:49:15'),(3,'Sona','Mikayelyan','7962468883c36dc5666c3aaa3bb3d38d0900ec827c203dd53d86babf0c3094e6','sonamikayelyan@workfront.com',NULL,NULL,0,'2016-08-26 12:49:15'),(18,'Artur','Babayan','7962468883c36dc5666c3aaa3bb3d38d0900ec827c203dd53d86babf0c3094e6','arturbabayan@workfront.com',NULL,NULL,0,'2016-09-30 12:59:13');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_response`
--

DROP TABLE IF EXISTS `user_response`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_response` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_response`
--

LOCK TABLES `user_response` WRITE;
/*!40000 ALTER TABLE `user_response` DISABLE KEYS */;
INSERT INTO `user_response` VALUES (3,'Maybe'),(2,'No'),(4,'Undefined'),(5,'Waiting for response'),(1,'Yes');
/*!40000 ALTER TABLE `user_response` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-30 13:39:25
