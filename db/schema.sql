--
-- Usar la DB mensajeria
--

USE `mensajeria`;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `rol` varchar(31) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(50) NOT NULL,
  `contrasenya` varchar(50) NOT NULL,
  `email` varchar(80) NOT NULL,
  `nombres` varchar(50) NOT NULL,
  `username` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_email` (`email`),
  UNIQUE KEY `UK_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mensajes`
--

DROP TABLE IF EXISTS `mensajes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mensajes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_hora` datetime(6) NOT NULL,
  `leido` bit(1) NOT NULL,
  `mensaje` varchar(2048) NOT NULL,
  `autor_id` bigint NOT NULL,
  `dest_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_autor` (`autor_id`),
  KEY `FK_dest` (`dest_id`),
  CONSTRAINT `FK_dest` FOREIGN KEY (`dest_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK_autor` FOREIGN KEY (`autor_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- El sistema tiene inicialmente el usuario "Admin"
--
LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES ('admin',1,'Admin','admin','admin@admin.com','Admin','admin');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
