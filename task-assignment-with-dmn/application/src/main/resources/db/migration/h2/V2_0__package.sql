-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE package_entity (
  id    VARCHAR(255) NOT NULL,
  type  VARCHAR(255) NOT NULL,
  weight INTEGER DEFAULT 0,
  radius INTEGER DEFAULT 0,
  width INTEGER DEFAULT 0,
  height INTEGER DEFAULT 0,
  length INTEGER DEFAULT 0,
  PRIMARY KEY (id)
);
