-- Inserting roles
INSERT INTO role (name) VALUES (0);
INSERT INTO role (name) VALUES (1);
INSERT INTO role (name) VALUES (2);
INSERT INTO role (name) VALUES (3);

-- Inserting customers
INSERT INTO customer (customerId, firstname, lastName, email, roleId, password)
VALUES ('f4789fa7-e512-4c56-b19a-44eafc79fc70', 'Juan', 'Doe', 'juan.doe@example.com', 1, 'co$2a$10$osvKabUtRtdOecK6n77pc.iUxwqWUTgwJPTjtJOtR4gQ5Ug.RVWo6');
INSERT INTO customer (customerId, firstname, lastName, email, roleId, password)
VALUES ('131a8261-c37e-4970-bb2d-e527a703b80c', 'Ana', 'Smith', 'ana.smith@example.com', 2, '$2a$10$.41fNWM9fGg1SutUQPZVfeYA8TZXxIAifGiHIRLA0aPvmoZ/B.lrO');
INSERT INTO customer (customerId, firstname, lastName, email, roleId, password)
VALUES ('3ceb9085-6a4a-4aab-b4b4-d61ee4bd1b16', 'Roberto', 'Johnson', 'roberto.johnson@example.com', 3, '$2a$10$SYICIuZ89GsQOvnwEyeDGerlu9Dvm/w2kmIv/PWFA.gWx2VdA14uW');

-- Inserting rooms
INSERT INTO room (roomId, computerAmount, building, roomNum, roomName, subRoom)
VALUES (211012, 20, '21', '101', 'Sala de Conferencias 1', 2);
INSERT INTO room (roomId, computerAmount, building, roomNum, roomName, subRoom)
VALUES (202010, 30, '20', '201', 'Sala de Reuniones 1', 0);
INSERT INTO room (roomId, computerAmount, building, roomNum, roomName, subRoom)
VALUES (182181, 15, '18', '218', 'LIS - Sala 1', 1);

-- Inserting applications
INSERT INTO application (applicationName, version) VALUES ('Microsoft Office', '2021');
INSERT INTO application (applicationName, version) VALUES ('Zoom', '5.0');
INSERT INTO application (applicationName, version) VALUES ('Adobe Photoshop', '2022');

-- Assigning applications to rooms
INSERT INTO roomsoftware (applicationId, roomId) VALUES (1, 211012);
INSERT INTO roomsoftware (applicationId, roomId) VALUES (2, 202010);
INSERT INTO roomsoftware (applicationId, roomId) VALUES (3, 182181);

-- Inserting restrictions
INSERT INTO restriction (description) VALUES ('No se permiten alimentos o bebidas');
INSERT INTO restriction (description) VALUES ('No fumar en la sala');
INSERT INTO restriction (description) VALUES ('No se permiten mascotas');

-- Assigning restrictions to rooms
INSERT INTO roomrestriction (restrictionId, roomId) VALUES (1, 211012);
INSERT INTO roomrestriction (restrictionId, roomId) VALUES (2, 202010);
INSERT INTO roomrestriction (restrictionId, roomId) VALUES (3, 182181);

-- Inserting implements
INSERT INTO implement (implementName, state) VALUES ('Proyector', 0);
INSERT INTO implement (implementName, state) VALUES ('Pizarra', 1);
INSERT INTO implement (implementName, state) VALUES ('Sistema de Sonido', 2);

-- Assigning implements to rooms
INSERT INTO roomimplement (implementId, roomId) VALUES (1, 211012);
INSERT INTO roomimplement (implementId, roomId) VALUES (2, 202010);
INSERT INTO roomimplement (implementId, roomId) VALUES (2, 182181);

-- Inserting reservation states
INSERT INTO reservationstate (description) VALUES (0);
INSERT INTO reservationstate (description) VALUES (1);
INSERT INTO reservationstate (description) VALUES (2);

-- Inserting reservations with date and time, separated by exactly two hours
INSERT INTO reservation (activityName, activityDescription, startsAt, endsAt, reservationType, reservationStateId, roomId, customerId)
VALUES ('Reunión de la Junta', 'Reunión mensual de la junta', '2023-10-25 14:30:00', '2023-10-25 16:30:00', 0, 1, 211012, 'f4789fa7-e512-4c56-b19a-44eafc79fc70');
INSERT INTO reservation (activityName, activityDescription, startsAt, endsAt, reservationType, reservationStateId, roomId, customerId)
VALUES ('Sesión de Capacitación', 'Capacitación para nuevos empleados', '2023-10-28 09:00:00', '2023-10-28 11:00:00', 1, 2, 182181, '131a8261-c37e-4970-bb2d-e527a703b80c');
INSERT INTO reservation (activityName, activityDescription, startsAt, endsAt, reservationType, reservationStateId, roomId, customerId)
VALUES ('Reunión de Equipo', 'Reunión semanal del equipo', '2023-10-27 15:00:00', '2023-10-27 17:00:00', 1, 3, 202010, '3ceb9085-6a4a-4aab-b4b4-d61ee4bd1b16');

