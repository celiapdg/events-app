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

-- passwords and username are the same
INSERT INTO user(email, password, username) VALUES
('celia@celia.celia', '$2a$10$ileXLH7.vJ/m0CHIC3RUF.Up8D5rJenealiPhK3xJnU3YW1Ydl5T2', 'celia'),
('ruben@ruben.ben', '$2a$10$dYZTe7f/V9ljHhQRPqMnkO26CuhDoVThZ2DXQ0f1fXIYx5T7Wvgc6', 'ruben'),
('julia@julia.lia', '$2a$10$I//FwjQ37WwQNPCfpLdO0u7ax7TqLlxgISVmFcF25C9OLqlCUM4uG', 'julia'),
('silvia@silvia.via', '$2a$10$qzNEsmCwLRxTtx/6U9/chuqXMyT6LkagTThGghmk1EduBtQU.u7be', 'silvia'),
('angel@angel.gel', '$2a$10$3pI8BW9z4bqVOytwD9XFF.d/R7SrrwNF9gCSRfMOAUnYwbr8OjjVi', 'angel'),
('clau@clau.dia','$2a$10$bLZELZgBzbjunOSWzNVnWO.ZZG/VKakmJBh5Ufk.7C/b1jNTntfQq','clau');

INSERT INTO role(name, user_id) VALUES
('USER',1),
('USER',2),
('USER',3),
('USER',4),
('USER',5),
('USER',6);

SELECT * FROM user;
SELECT * FROM role;

DROP SCHEMA IF EXISTS eventsdb;
CREATE SCHEMA eventsdb;
USE eventsdb;

CREATE TABLE events (
  id BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  host_id BIGINT NOT NULL,
  opening DATETIME NOT NULL,
  ending DATETIME,
  description VARCHAR(2000),
  guest_limit INT NOT NULL,
  current_total_guests INT NOT NULL,
  total_guest_limit INT NOT NULL,
  current_queue_position INT NOT NULL,
  visibility VARCHAR(30) NOT NULL,
  event_status VARCHAR(30) NOT NULL,
  registration_status VARCHAR(30) NOT NULL,
  entry_code VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE guest(
    guest_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    queue_position INT NOT NULL,
    guest_status VARCHAR(30),
    FOREIGN KEY (event_id) REFERENCES events (id),
    PRIMARY KEY (guest_id, event_id)
    );

INSERT INTO events(name, host_id, opening, ending, description, guest_limit, current_total_guests, total_guest_limit,
current_queue_position, visibility, event_status, registration_status, entry_code) VALUES
('giveaway', 1, now() - interval 1 day, now(), 'giveaway giveaway', 3, 0, 1, 0, 'PUBLIC', 
'NOT_STARTED', 'OPEN', 'JIIC3C'),
('contest', 3, now() - interval 3 day, now() - interval 1 day, 'contest contest', 3, 0, 1, 0, 'PUBLIC', 
'NOT_STARTED', 'OPEN', 'JKLK3C'),
('concierto', 1, now(), now(), 'concierto concierto', 3, 0, 30, 0, 'PUBLIC', 
'NOT_STARTED', 'OPEN', 'J67A3C'),
('cataloging', 2, now() - interval 5 day, now() - interval 1 day, 'cataloging cataloging', 3, 0, 1, 0, 'PUBLIC', 
'NOT_STARTED', 'CLOSED', 'JKK33C'),
('tour', 2, now() - interval 1 day, now() + interval 1 day, 'tour tour', 3, 5, 30, 0, 'PUBLIC', 
'NOT_STARTED', 'CLOSED', 'JKCC3C'),
('turnips', 1, now() + interval 1 day, now() + interval 6 day, 'turnips turnips', 3, 0, 1, 0, 'PUBLIC', 
'NOT_STARTED', 'OPEN', 'JKGH3C'),
('dance', 1, now(), now(), 'dance dance', 0, 0, 0, 0, 'PRIVATE', 
'NOT_STARTED', 'CLOSED_BY_HOST', 'JK7F3C'),
('fish tournament', 2, NOW(), NOW(), 'fish fish', 4, 0, 10, 0, 'PUBLIC', 
'STARTED', 'OPEN', 'JK566C');

INSERT INTO guest(guest_id, event_id, queue_position, guest_status) VALUES
(1, 5, 1, 'REGISTERED'),
(3, 5, 2, 'REGISTERED'),
(5, 5, 5, 'REGISTERED'),
(6, 5, 3, 'REGISTERED'),
(4, 5, 4, 'REGISTERED');


SELECT * FROM events;
SELECT * FROM guest;
