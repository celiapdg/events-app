DROP SCHEMA IF EXISTS login;
CREATE SCHEMA login;
USE login;

CREATE TABLE `user` (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) UNIQUE,
  `password` VARCHAR(255),
  username VARCHAR(36) UNIQUE,
  PRIMARY KEY (id)
);


CREATE TABLE `role` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255),
    user_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES `user` (id)
);

SELECT * FROM user;
SELECT * FROM role;

DROP SCHEMA IF EXISTS eventsdb;
CREATE SCHEMA eventsdb;
USE eventsdb;

CREATE TABLE event (
  id BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  host_id BIGINT NOT NULL,
  opening DATETIME NOT NULL,
  ending DATETIME,
  description VARCHAR(2000),
  visibility VARCHAR(30),
  event_status VARCHAR(30),
  PRIMARY KEY (id)
);

CREATE TABLE guest(
    guest_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    queue INT NOT NULL,
    guest_status VARCHAR(30),
    PRIMARY KEY (guest_id, event_id)
    );

SELECT * FROM event;
SELECT * FROM guest;