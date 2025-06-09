DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
	`id` bigint AUTO_INCREMENT PRIMARY KEY,
	`user_name` varchar(255) NOT NULL UNIQUE,
	`password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;