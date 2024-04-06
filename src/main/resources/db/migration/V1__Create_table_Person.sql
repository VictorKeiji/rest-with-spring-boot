CREATE TABLE `person` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `first_name` varchar(15) NOT NULL,
                          `last_name` varchar(20) NOT NULL,
                          `address` varchar(80) DEFAULT NULL,
                          `gender` varchar(1) DEFAULT NULL,
                          PRIMARY KEY (`id`)
);
