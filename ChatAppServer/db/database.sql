
create database chat_app;
use chat_app;

-- MySQL dump 10.13  Distrib 8.0.41, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: chat_app
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat_participants`
--

DROP TABLE IF EXISTS `chat_participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_participants` (
  `chat_id` varchar(300) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `joined_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`chat_id`,`user_id`),
  KEY `idx_chat_participants_user` (`user_id`),
  CONSTRAINT `chat_participants_ibfk_1` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chat_participants_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_participants`
--

LOCK TABLES `chat_participants` WRITE;
/*!40000 ALTER TABLE `chat_participants` DISABLE KEYS */;
INSERT INTO `chat_participants` VALUES ('650ca4f1-4981-4422-9c76-4765b95eeb2d-94d8f0ef-b683-4e6f-adc4-1493acd4cecf','650ca4f1-4981-4422-9c76-4765b95eeb2d','2025-06-01 13:46:08'),('650ca4f1-4981-4422-9c76-4765b95eeb2d-94d8f0ef-b683-4e6f-adc4-1493acd4cecf','94d8f0ef-b683-4e6f-adc4-1493acd4cecf','2025-06-01 13:46:08'),('6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','650ca4f1-4981-4422-9c76-4765b95eeb2d','2025-06-01 13:48:56'),('6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','94d8f0ef-b683-4e6f-adc4-1493acd4cecf','2025-06-01 13:48:56'),('6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','94f86e91-6b58-4254-8ddf-f4cf9346d740','2025-06-01 13:48:56');
/*!40000 ALTER TABLE `chat_participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chats`
--

DROP TABLE IF EXISTS `chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chats` (
  `id` varchar(300) NOT NULL,
  `name` varchar(200) NOT NULL,
  `is_group` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chats`
--

LOCK TABLES `chats` WRITE;
/*!40000 ALTER TABLE `chats` DISABLE KEYS */;
INSERT INTO `chats` VALUES ('650ca4f1-4981-4422-9c76-4765b95eeb2d-94d8f0ef-b683-4e6f-adc4-1493acd4cecf','BBB',0,'2025-06-01 13:46:08','2025-06-01 13:46:08'),('6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','GROUP',1,'2025-06-01 13:48:56','2025-06-01 13:48:56');
/*!40000 ALTER TABLE `chats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` varchar(50) NOT NULL,
  `sender_id` varchar(50) NOT NULL,
  `chat_id` varchar(300) NOT NULL,
  `content` text,
  `image_path` varchar(500) DEFAULT NULL,
  `attachment_path` varchar(500) DEFAULT NULL,
  `message_type` enum('TEXT','IMAGE','FILE') DEFAULT 'TEXT',
  `is_read` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_messages_sender` (`sender_id`),
  KEY `idx_messages_chat` (`chat_id`),
  KEY `idx_messages_timestamp` (`created_at`),
  KEY `idx_messages_type` (`message_type`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES ('01f00f71-1298-45c6-a71c-7f7ffc4045cf','650ca4f1-4981-4422-9c76-4765b95eeb2d','650ca4f1-4981-4422-9c76-4765b95eeb2d-94d8f0ef-b683-4e6f-adc4-1493acd4cecf','sdafsadf',NULL,NULL,'TEXT',0,'2025-06-01 13:46:25'),('16ff35a3-24af-4298-bc79-ce980e614aa8','650ca4f1-4981-4422-9c76-4765b95eeb2d','6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','null','https://res.cloudinary.com/dghhudods/image/upload/v1748785784/lusfhoafk8jhrsd9jald.png',NULL,'IMAGE',0,'2025-06-01 13:49:45'),('439b7ebc-7178-4e08-b61d-a14933b4f0a2','94d8f0ef-b683-4e6f-adc4-1493acd4cecf','6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','bdsfbds',NULL,NULL,'TEXT',0,'2025-06-01 15:22:50'),('51fa73dc-40ee-4ad6-b74c-6d863018ed42','94d8f0ef-b683-4e6f-adc4-1493acd4cecf','650ca4f1-4981-4422-9c76-4765b95eeb2d-94d8f0ef-b683-4e6f-adc4-1493acd4cecf','sdfsafd',NULL,NULL,'TEXT',0,'2025-06-01 13:46:27'),('541d2527-ec9e-4954-942f-37e4f2ddece7','94d8f0ef-b683-4e6f-adc4-1493acd4cecf','650ca4f1-4981-4422-9c76-4765b95eeb2d-94d8f0ef-b683-4e6f-adc4-1493acd4cecf','hshtsdh',NULL,NULL,'TEXT',0,'2025-06-01 13:46:22'),('7a3cc0b8-644b-4167-b7cb-b3b5fc087e1d','94f86e91-6b58-4254-8ddf-f4cf9346d740','6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','asfbkjnadfb',NULL,NULL,'TEXT',0,'2025-06-01 13:49:30'),('9868dbdc-a715-4355-a560-24171e5f004e','94d8f0ef-b683-4e6f-adc4-1493acd4cecf','650ca4f1-4981-4422-9c76-4765b95eeb2d-94d8f0ef-b683-4e6f-adc4-1493acd4cecf','null','https://res.cloudinary.com/dghhudods/image/upload/v1748785595/kg17fg1jerhlqwcxjnnr.png',NULL,'IMAGE',0,'2025-06-01 13:46:37'),('a24af9bf-10ed-42c5-b491-30fa4f188bc7','650ca4f1-4981-4422-9c76-4765b95eeb2d','650ca4f1-4981-4422-9c76-4765b95eeb2d-94d8f0ef-b683-4e6f-adc4-1493acd4cecf','Nhom7.pdf',NULL,'https://res.cloudinary.com/dghhudods/raw/upload/v1748785616/Nhom7_1748785614859.pdf','FILE',0,'2025-06-01 13:46:57'),('b1e2edea-7b4e-4457-8a8c-a020337cc974','650ca4f1-4981-4422-9c76-4765b95eeb2d','6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','fgsbsfgb',NULL,NULL,'TEXT',0,'2025-06-01 15:22:47'),('b4acd210-3e59-409c-b3de-ee13c581e278','94d8f0ef-b683-4e6f-adc4-1493acd4cecf','6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','dfm,bdsf b',NULL,NULL,'TEXT',0,'2025-06-01 13:49:20'),('c99a86a0-c167-4a16-9f8b-b61f6005dcf5','650ca4f1-4981-4422-9c76-4765b95eeb2d','6e0efb3a-00a3-4ac2-8099-8337ae7d30c4','sjnabsadsfb',NULL,NULL,'TEXT',0,'2025-06-01 13:49:25');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` varchar(50) NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `avatar_path` varchar(500) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('650ca4f1-4981-4422-9c76-4765b95eeb2d','BBB','BBB@gmail.com','1450575459','default_avatar.png','2025-06-01 13:45:45','2025-06-01 13:45:45'),('94d8f0ef-b683-4e6f-adc4-1493acd4cecf','AAA','AAA@gmail.com','1450575459','default_avatar.png','2025-06-01 13:44:58','2025-06-01 13:44:58'),('94f86e91-6b58-4254-8ddf-f4cf9346d740','CCC','CCC@gmail.com','1450575459','default_avatar.png','2025-06-01 13:48:07','2025-06-01 13:48:07');
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

-- Dump completed on 2025-06-01 22:26:36
