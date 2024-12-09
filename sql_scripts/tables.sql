CREATE TABLE application (
                             applicationId BIGSERIAL NOT NULL,
                             applicationName VARCHAR(255),
                             version VARCHAR(255),
                             PRIMARY KEY (applicationId)
);

CREATE TABLE customer (
                          roleId BIGINT,
                          customerId VARCHAR(64) NOT NULL,
                          email VARCHAR(255),
                          firstname VARCHAR(255),
                          lastname VARCHAR(255),
                          password VARCHAR(255),
                          PRIMARY KEY (customerId),
                          CONSTRAINT UK_email_customerId UNIQUE (email, customerId)
);

CREATE TABLE implement (
                           state SMALLINT CHECK (state BETWEEN 0 AND 3),
                           implementId BIGSERIAL NOT NULL,
                           implementName VARCHAR(255),
                           PRIMARY KEY (implementId)
);

CREATE TABLE reservation (
                             reservationType SMALLINT NOT NULL CHECK (reservationType BETWEEN 0 AND 1),
                             endsAt TIMESTAMP(6) NOT NULL,
                             reservationId BIGSERIAL NOT NULL,
                             reservationStateId BIGINT,
                             roomId BIGINT,
                             startsAt TIMESTAMP(6) NOT NULL,
                             activityName VARCHAR(64) NOT NULL,
                             customerId VARCHAR(64),
                             activityDescription VARCHAR(512) NOT NULL,
                             PRIMARY KEY (reservationId)
);

CREATE TABLE reservationstate (
                                  description SMALLINT NOT NULL UNIQUE CHECK (description BETWEEN 0 AND 2),
                                  reservationStateId BIGSERIAL NOT NULL,
                                  PRIMARY KEY (reservationStateId)
);

CREATE TABLE restriction (
                             restrictionId BIGSERIAL NOT NULL,
                             description VARCHAR(255),
                             PRIMARY KEY (restrictionId)
);

CREATE TABLE role (
                      name SMALLINT CHECK (name BETWEEN 0 AND 3),
                      roleId BIGSERIAL NOT NULL,
                      PRIMARY KEY (roleId)
);

CREATE TABLE room (
                      computerAmount INTEGER NOT NULL,
                      roomNum VARCHAR(3) NOT NULL,
                      subRoom INTEGER,
                      roomId BIGINT NOT NULL,
                      building VARCHAR(16) NOT NULL,
                      roomName VARCHAR(64),
                      PRIMARY KEY (roomId)
);

CREATE TABLE roomimplement (
                               implementId BIGINT NOT NULL,
                               roomId BIGINT NOT NULL,
                               state SMALLINT CHECK (state BETWEEN 0 AND 3)
);

CREATE TABLE roomrestriction (
                                 restrictionId BIGINT NOT NULL,
                                 roomId BIGINT NOT NULL
);

CREATE TABLE roomsoftware (
                              applicationId BIGINT NOT NULL,
                              roomId BIGINT NOT NULL,
                              version VARCHAR(20)
);

ALTER TABLE customer
    ADD CONSTRAINT FK_customer_role FOREIGN KEY (roleId) REFERENCES role;

ALTER TABLE reservation
    ADD CONSTRAINT FK_reservation_state FOREIGN KEY (reservationStateId) REFERENCES reservationstate;

ALTER TABLE reservation
    ADD CONSTRAINT FK_reservation_room FOREIGN KEY (roomId) REFERENCES room;

ALTER TABLE reservation
    ADD CONSTRAINT FK_reservation_customer FOREIGN KEY (customerId) REFERENCES customer;

ALTER TABLE roomimplement
    ADD CONSTRAINT FK_room_implement FOREIGN KEY (implementId) REFERENCES implement;

ALTER TABLE roomimplement
    ADD CONSTRAINT FK_implement_room FOREIGN KEY (roomId) REFERENCES room;

ALTER TABLE roomrestriction
    ADD CONSTRAINT FK_room_restriction FOREIGN KEY (restrictionId) REFERENCES restriction;

ALTER TABLE roomrestriction
    ADD CONSTRAINT FK_restriction_room FOREIGN KEY (roomId) REFERENCES room;

ALTER TABLE roomsoftware
    ADD CONSTRAINT FK_room_software FOREIGN KEY (applicationId) REFERENCES application;

ALTER TABLE roomsoftware
    ADD CONSTRAINT FK_software_room FOREIGN KEY (roomId) REFERENCES room;