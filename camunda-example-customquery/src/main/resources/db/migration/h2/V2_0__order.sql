-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE book_entity (
  id    VARCHAR(255) NOT NULL,
  name  VARCHAR(255),
  price INTEGER,
  PRIMARY KEY (id)
);
CREATE TABLE order_entity (
  id   VARCHAR(255) NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY (id)
);
CREATE TABLE order_entity_positions (
  order_entity_id VARCHAR(255) NOT NULL,
  positions_id    VARCHAR(255) NOT NULL
);
CREATE TABLE order_position_entity (
  id      VARCHAR(255) NOT NULL,
  book_id VARCHAR(255),
  PRIMARY KEY (id)
);
ALTER TABLE order_entity_positions
  ADD CONSTRAINT UK_ipmapd5cyxgbks52l5tay2xg6 UNIQUE (positions_id);
ALTER TABLE order_entity_positions
  ADD CONSTRAINT FKfpswkik4b3w309vnh2gkak4lu FOREIGN KEY (positions_id) REFERENCES order_position_entity;
ALTER TABLE order_entity_positions
  ADD CONSTRAINT FKqlotmb9dpjrjgt4e47wvhqxvm FOREIGN KEY (order_entity_id) REFERENCES order_entity;
ALTER TABLE order_position_entity
  ADD CONSTRAINT FKtfet7ypb8w4lt4wbgk5bk0dow FOREIGN KEY (book_id) REFERENCES book_entity;
