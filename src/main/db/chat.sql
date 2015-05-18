# ************************************************************
# Sequel Pro SQL dump
# Версия 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Адрес: 127.0.0.1 (MySQL 5.6.24)
# Схема: chat
# Время создания: 2015-05-18 18:38:45 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Дамп таблицы Actions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Actions`;

CREATE TABLE `Actions` (
  `actionID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userID` int(11) unsigned NOT NULL,
  `messageID` int(11) unsigned NOT NULL,
  `requestType` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`actionID`),
  KEY `messageID` (`messageID`),
  KEY `userID` (`userID`),
  CONSTRAINT `messageID` FOREIGN KEY (`messageID`) REFERENCES `Messages` (`messageID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userID` FOREIGN KEY (`userID`) REFERENCES `Users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

LOCK TABLES `Actions` WRITE;
/*!40000 ALTER TABLE `Actions` DISABLE KEYS */;

INSERT INTO `Actions` (`actionID`, `userID`, `messageID`, `requestType`)
VALUES
	(1,1,1,'add'),
	(2,1,2,'add'),
	(3,2,3,'add'),
	(4,3,4,'add'),
	(5,1,2,'edit'),
	(6,2,5,'add'),
	(7,3,6,'add'),
	(8,1,7,'add'),
	(9,3,6,'edit');

/*!40000 ALTER TABLE `Actions` ENABLE KEYS */;
UNLOCK TABLES;


# Дамп таблицы Messages
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Messages`;

CREATE TABLE `Messages` (
  `messageID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `messageText` varchar(5000) DEFAULT NULL,
  `messageTime` datetime DEFAULT NULL,
  PRIMARY KEY (`messageID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

LOCK TABLES `Messages` WRITE;
/*!40000 ALTER TABLE `Messages` DISABLE KEYS */;

INSERT INTO `Messages` (`messageID`, `messageText`, `messageTime`)
VALUES
	(1,'text','2015-05-15 19:00:01'),
	(2,'text','2015-05-18 19:00:02'),
	(3,'text','2015-05-18 19:00:03'),
	(4,'hello','2015-05-18 19:00:04'),
	(5,'text','2015-05-18 19:00:05'),
	(6,'text','2015-05-18 19:00:06'),
	(7,'hello, my name is kendrick','2015-05-18 19:00:07');

/*!40000 ALTER TABLE `Messages` ENABLE KEYS */;
UNLOCK TABLES;


# Дамп таблицы Users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Users`;

CREATE TABLE `Users` (
  `userID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(256) NOT NULL DEFAULT 'Guest',
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;

INSERT INTO `Users` (`userID`, `username`)
VALUES
	(1,'Anton'),
	(2,'Dima'),
	(3,'Taylor'),
	(4,'Iowan');

/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
