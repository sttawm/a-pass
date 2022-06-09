# --- !Ups
CREATE TABLE things (
  id serial primary key,
  name text
);

# --- !Downs
DROP TABLE IF EXISTS things;
