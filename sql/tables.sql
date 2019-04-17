CREATE TABLE `user_right` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `service` varchar(45) NOT NULL,
  `service_right` varchar(45) NOT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  PRIMARY KEY (`id`,`user_id`,`service`,`service_right`)
) ENGINE=InnoDB;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `pw` varchar(60) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `last_login` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`id`,`email`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB;

CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commercial` tinyint(1) NOT NULL DEFAULT '0',
  `company_name` varchar(45) NOT NULL DEFAULT ' ',
  `title` varchar(45) NOT NULL DEFAULT ' ',
  `salutation` varchar(45) NOT NULL DEFAULT ' ',
  `first_name` varchar(45) NOT NULL DEFAULT ' ',
  `last_name` varchar(45) NOT NULL DEFAULT ' ',
  `street_name` varchar(45) NOT NULL DEFAULT ' ',
  `street_nr` varchar(45) NOT NULL DEFAULT ' ',
  `zip_code` varchar(45) NOT NULL DEFAULT ' ',
  `place` varchar(45) NOT NULL DEFAULT ' ',
  `homepage` varchar(45) NOT NULL DEFAULT ' ',
  `telephone` varchar(45) NOT NULL DEFAULT ' ',
  `email` varchar(45) NOT NULL DEFAULT ' ',
  `fax` varchar(45) NOT NULL DEFAULT ' ',
  `created` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  `modified` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `customer_contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `title` varchar(45) NOT NULL DEFAULT ' ',
  `salutation` varchar(45) NOT NULL DEFAULT ' ',
  `first_name` varchar(45) NOT NULL DEFAULT ' ',
  `last_name` varchar(45) NOT NULL DEFAULT ' ',
  `street_name` varchar(45) NOT NULL DEFAULT ' ',
  `street_nr` varchar(45) NOT NULL DEFAULT ' ',
  `zip_code` varchar(45) NOT NULL DEFAULT ' ',
  `place` varchar(45) NOT NULL DEFAULT ' ',
  `telephone` varchar(45) NOT NULL DEFAULT ' ',
  `email` varchar(45) NOT NULL DEFAULT ' ',
  `fax` varchar(45) NOT NULL DEFAULT ' ',
  `created` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  `modified` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`id`,`customer_id`)
) ENGINE=InnoDB;

CREATE TABLE `customer_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `type` varchar(45) NOT NULL DEFAULT ' ',
  `street_name` varchar(45) NOT NULL DEFAULT ' ',
  `street_nr` varchar(45) NOT NULL DEFAULT ' ',
  `zip_code` varchar(45) NOT NULL DEFAULT ' ',
  `place` varchar(45) NOT NULL DEFAULT ' ',
  `telephone` varchar(45) NOT NULL DEFAULT ' ',
  `email` varchar(45) NOT NULL DEFAULT ' ',
  `fax` varchar(45) NOT NULL DEFAULT ' ',
  `created` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  `modified` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`id`,`customer_id`)
) ENGINE=InnoDB;