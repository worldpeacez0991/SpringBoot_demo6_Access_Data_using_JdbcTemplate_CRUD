--CAUTION, this is for testing purposes, only use DROP when you know this data is temporary for testing only.
DROP TABLE COUNTRIES IF EXISTS;

CREATE TABLE countries (
    id int,
    name varchar(255)
);

INSERT INTO countries (id, name) VALUES (1, 'USA');
INSERT INTO countries (id, name) VALUES (2, 'France');
INSERT INTO countries (id, name) VALUES (3, 'Brazil');
INSERT INTO countries (id, name) VALUES (4, 'Italy');
INSERT INTO countries (id, name) VALUES (5, 'Canada');

