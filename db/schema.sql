CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE photo (
   id SERIAL PRIMARY KEY,
   name TEXT,
   owner_id int
);