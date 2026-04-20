-- MySQL dump 10.13  Distrib 9.6.0, for Win64 (x86_64)
--
-- Host: localhost    Database: chatdb
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat_room`
--

DROP TABLE IF EXISTS `chat_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_name` varchar(255) DEFAULT NULL,
  `access_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_room`
--

LOCK TABLES `chat_room` WRITE;
/*!40000 ALTER TABLE `chat_room` DISABLE KEYS */;
INSERT INTO `chat_room` VALUES (1,'general chat',NULL),(9,'Developers','8A43DD'),(10,'Testing Dept','359247'),(11,'Interns','9EE1CD'),(12,'test','CE4C68');
/*!40000 ALTER TABLE `chat_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friendship`
--

DROP TABLE IF EXISTS `friendship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friendship` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `addressee_id` bigint NOT NULL,
  `requester_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK19mckd9nov5mnli8u35lx27m8` (`addressee_id`),
  KEY `FK608bqp7hf9dcq8ow16pndxxrf` (`requester_id`),
  CONSTRAINT `FK19mckd9nov5mnli8u35lx27m8` FOREIGN KEY (`addressee_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK608bqp7hf9dcq8ow16pndxxrf` FOREIGN KEY (`requester_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendship`
--

LOCK TABLES `friendship` WRITE;
/*!40000 ALTER TABLE `friendship` DISABLE KEYS */;
INSERT INTO `friendship` VALUES (1,'2026-04-09 15:37:41.203844','ACCEPTED',3,2),(2,'2026-04-10 11:16:52.058955','ACCEPTED',5,2),(3,'2026-04-13 19:14:23.589935','ACCEPTED',3,4);
/*!40000 ALTER TABLE `friendship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(5000) DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `content_type` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `message_type` varchar(20) DEFAULT NULL,
  `sent_at` datetime(6) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `recipient_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq97urb0l1mxmmjl54tmlya11f` (`room_id`),
  KEY `FKpdrb79dg3bgym7pydlf9k3p1n` (`user_id`),
  KEY `FKc60hib4mr1pl61lmcpvehy3m5` (`recipient_id`),
  CONSTRAINT `FKc60hib4mr1pl61lmcpvehy3m5` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpdrb79dg3bgym7pydlf9k3p1n` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKq97urb0l1mxmmjl54tmlya11f` FOREIGN KEY (`room_id`) REFERENCES `chat_room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,'hey',1,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,'DELIVERED',NULL),(2,'hello',1,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,'DELIVERED',NULL),(3,'hy buddies',1,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,'DELIVERED',NULL),(4,'this is chatroom 1',1,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,'DELIVERED',NULL),(7,'hello',1,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,'DELIVERED',NULL),(8,'Hey',NULL,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-09 15:01:21.262152','SENT',1),(9,'Vaibhav this side',NULL,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-09 15:01:31.317007','SENT',1),(10,'hey vaibhav',NULL,3,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-09 15:40:39.751894','SENT',2),(11,'hi lasya',NULL,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-09 15:40:54.333034','SENT',3),(12,'hey all',1,4,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-09 23:59:47.520338','DELIVERED',NULL),(13,'hi vaibhav',NULL,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-10 09:53:40.071693','SENT',3),(14,'hi vaibhav',NULL,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-10 09:53:44.426505','SENT',3),(15,'hello lasya',NULL,3,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-10 09:53:57.075445','SENT',2),(16,'hello vaibhav',NULL,5,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-10 11:17:11.914265','SENT',2),(17,'hey charan',NULL,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-10 11:17:29.330695','SENT',5),(18,'iYSjWJY26xqj8F5/2umjTA==',NULL,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-12 22:33:22.335415','SENT',3),(19,NULL,NULL,2,'image/png',_binary '\0','Screenshot 2025-12-25 132700.png','/uploads/0e679eed-9281-491e-a7c8-7b6568d53143.png','IMAGE','2026-04-12 22:33:56.320019','SENT',3),(20,'dt5h880dZ83BHE1nFScXZQ==',11,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-12 22:42:56.075460','DELIVERED',NULL),(21,'xpUplsngcfWRG4T56+fRrQ==',1,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-13 19:10:47.569759','DELIVERED',NULL),(22,'05e6NfDG4ac+e0VaDydZGw==',1,3,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-13 19:10:56.521753','DELIVERED',NULL),(23,'LSr/l2zDJDHYiyhlfn+4Lw==',1,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-13 19:12:59.318136','DELIVERED',NULL),(24,'rVznmazFuJ2aaIRNnNckmA==',9,2,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-13 19:13:41.659126','DELIVERED',NULL),(25,NULL,9,3,'image/jpeg',_binary '\0','hindu-god-hanuman-his-panchmukhi-260nw-2604433571.jpg','/uploads/77690269-ec65-457e-8d0d-7a1c02177fb9.jpg','IMAGE','2026-04-13 19:13:59.187008','DELIVERED',NULL),(26,'l2kfsKJQeif1kCLmeaNGdQ==',NULL,3,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-13 19:14:37.837059','SENT',4),(27,'cjFeIdzZmXsTx89UyMUsRA==',NULL,4,NULL,_binary '\0',NULL,NULL,'TEXT','2026-04-13 19:14:50.463475','SENT',3);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_reaction`
--

DROP TABLE IF EXISTS `message_reaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message_reaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `emoji` varchar(255) NOT NULL,
  `reacted_at` datetime(6) DEFAULT NULL,
  `message_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdf4mgt18iwqtybf55a7s0md75` (`message_id`),
  KEY `FKe3ngmocd08j6nfeo1bxdtp8bh` (`user_id`),
  CONSTRAINT `FKdf4mgt18iwqtybf55a7s0md75` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`),
  CONSTRAINT `FKe3ngmocd08j6nfeo1bxdtp8bh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_reaction`
--

LOCK TABLES `message_reaction` WRITE;
/*!40000 ALTER TABLE `message_reaction` DISABLE KEYS */;
INSERT INTO `message_reaction` VALUES (1,'👍','2026-04-09 23:59:27.000000',8,1),(2,'❤️','2026-04-09 23:59:38.780587',2,4),(4,'❤️','2026-04-10 09:54:09.226196',15,2),(5,'❤️','2026-04-10 11:17:35.448216',17,5);
/*!40000 ALTER TABLE `message_reaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_read` bit(1) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnk4ftb5am9ubmkv1661h15ds9` (`user_id`),
  CONSTRAINT `FKnk4ftb5am9ubmkv1661h15ds9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'Vaibhav sent you a friend request.','2026-04-09 15:37:41.303291',_binary '',3),(2,'Lasya accepted your friend request.','2026-04-09 15:40:03.510015',_binary '',2),(3,'Vaibhav sent you a friend request.','2026-04-10 11:16:52.089960',_binary '\0',5),(4,'Charan accepted your friend request.','2026-04-10 11:16:58.538045',_binary '',2),(5,'admin sent you a friend request.','2026-04-13 19:14:23.609626',_binary '\0',3),(6,'Lasya accepted your friend request.','2026-04-13 19:14:29.940187',_binary '\0',4);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_access`
--

DROP TABLE IF EXISTS `room_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_access` (
  `room_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`room_id`,`user_id`),
  KEY `FKoadycaxo0x2jw7ojldcl2ost7` (`user_id`),
  CONSTRAINT `FK7pdvdd5fkjmwpbl15qoaachjk` FOREIGN KEY (`room_id`) REFERENCES `chat_room` (`id`),
  CONSTRAINT `FKoadycaxo0x2jw7ojldcl2ost7` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_access`
--

LOCK TABLES `room_access` WRITE;
/*!40000 ALTER TABLE `room_access` DISABLE KEYS */;
INSERT INTO `room_access` VALUES (1,1),(9,2),(11,2),(9,3),(9,4),(10,4),(11,4),(12,4);
/*!40000 ALTER TABLE `room_access` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_room`
--

DROP TABLE IF EXISTS `user_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe59qe6lt7f7ibt8547lt3v1n7` (`room_id`),
  KEY `FKyiyqqc4bed6bdmtsjadvmfnq` (`user_id`),
  CONSTRAINT `FKe59qe6lt7f7ibt8547lt3v1n7` FOREIGN KEY (`room_id`) REFERENCES `chat_room` (`id`),
  CONSTRAINT `FKyiyqqc4bed6bdmtsjadvmfnq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_room`
--

LOCK TABLES `user_room` WRITE;
/*!40000 ALTER TABLE `user_room` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `verified` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Vaibhav',NULL,NULL,_binary '\0',NULL,''),(2,'Vaibhav','vaibhavbk2124@gmail.com',NULL,_binary '',NULL,''),(3,'Lasya','vaibhavbk.22is@saividya.ac.in',NULL,_binary '',NULL,''),(4,'admin','admin@admin.com',NULL,_binary '','admin','ADMIN'),(5,'Charan','vaibhavbkbusiness@gmail.com',NULL,_binary '',NULL,'USER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-19 12:58:47
