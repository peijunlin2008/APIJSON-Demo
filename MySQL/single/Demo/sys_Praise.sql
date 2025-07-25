-- MySQL dump 10.13  Distrib 8.0.31, for macos12 (x86_64)
--
-- Host: apijson.cn    Database: sys
-- ------------------------------------------------------
-- Server version	5.7.43-log

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
-- Table structure for table `Praise`
--

DROP TABLE IF EXISTS `Praise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Praise` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '动态id',
  `momentId` bigint(15) NOT NULL COMMENT '唯一标识',
  `userId` bigint(15) NOT NULL COMMENT '用户id',
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='如果对Moment写安全要求高，可以将Moment内praiserUserIdList分离到Praise表中，作为userIdList。\n权限注解也改下：\n@MethodAccess(\n		PUT = {OWNER, ADMIN}\n		)\nclass Moment {\n       …\n}\n\n@MethodAccess(\n		PUT = {LOGIN, CONTACT, CIRCLE, OWNER, ADMIN}\n		)\n class Praise {\n       …\n }\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Praise`
--

LOCK TABLES `Praise` WRITE;
/*!40000 ALTER TABLE `Praise` DISABLE KEYS */;
INSERT INTO `Praise` VALUES (1,12,82001,'2017-11-19 13:02:30'),(2,15,82002,'2017-11-19 13:02:30'),(3,32,82003,'2017-11-19 13:02:30'),(4,58,82004,'2017-11-19 13:02:30'),(5,170,82005,'2017-11-19 13:02:30'),(6,235,82006,'2017-11-19 13:02:30'),(7,301,82007,'2017-11-19 13:02:30'),(8,371,82008,'2017-11-19 13:02:30'),(9,470,82009,'2017-11-19 13:02:30'),(10,511,82010,'2017-11-19 13:02:30'),(11,543,82011,'2017-11-19 13:02:30'),(12,551,82012,'2017-11-19 13:02:30'),(13,594,82013,'2017-11-19 13:02:30'),(14,595,82014,'2017-11-19 13:02:30'),(15,704,82015,'2017-11-19 13:02:30'),(16,1491200468898,82016,'2017-11-19 13:02:30'),(17,1491277116776,82017,'2017-11-19 13:02:30'),(18,1493835799335,82018,'2017-11-19 13:02:30');
/*!40000 ALTER TABLE `Praise` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-07  1:38:54
