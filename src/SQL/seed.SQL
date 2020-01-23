DROP TABLE IF EXISTS `contacts`;
CREATE TABLE `contacts`(
    `contactId` int(11) NOT NULL AUTO_INCREMENT,
    `userId` int(11),
    `firstName` varchar(80) NOT NULL,
    `lastName` varchar(80) NOT NULL,
    `emailAddress` varchar(80),
    `phoneNumber` int(11),
    `organization` varchar(80),
    `role` varchar(80),
    `updateDate` date,
    `createDate` date NOT NULL,
    PRIMARY KEY (`contactID`)
) ENGINE=InnoDB;