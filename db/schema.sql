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

CREATE TABLE user (
   id SERIAL PRIMARY KEY,
   name TEXT,
   email TEXT,
   password TEXT
);
