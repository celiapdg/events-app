DROP SCHEMA IF EXISTS login;
CREATE SCHEMA login;
USE login;

CREATE TABLE `user` (
  id BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  `password` VARCHAR(255),
  username VARCHAR(36) UNIQUE,
  PRIMARY KEY (id)
);


CREATE TABLE `role` (
  id BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  user_id BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES `user` (id)
);
