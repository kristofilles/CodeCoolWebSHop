ALTER TABLE IF EXISTS ONLY products
  DROP CONSTRAINT IF EXISTS products_pk CASCADE;
ALTER TABLE IF EXISTS ONLY products
  DROP CONSTRAINT IF EXISTS products_fk0 CASCADE;
ALTER TABLE IF EXISTS ONLY products
  DROP CONSTRAINT IF EXISTS products_fk1 CASCADE;
ALTER TABLE IF EXISTS ONLY suppliers
  DROP CONSTRAINT IF EXISTS suppliers_pk CASCADE;
ALTER TABLE IF EXISTS ONLY product_categories
  DROP CONSTRAINT IF EXISTS product_categories_pk CASCADE;
ALTER TABLE IF EXISTS ONLY orders
  DROP CONSTRAINT IF EXISTS orders_pk CASCADE;
ALTER TABLE IF EXISTS ONLY orders
  DROP CONSTRAINT IF EXISTS orders_fk0 CASCADE;
ALTER TABLE IF EXISTS ONLY orders
  DROP CONSTRAINT IF EXISTS orders_fk1 CASCADE;
ALTER TABLE IF EXISTS ONLY line_item
  DROP CONSTRAINT IF EXISTS line_item_pk CASCADE;
ALTER TABLE IF EXISTS ONLY line_item
  DROP CONSTRAINT IF EXISTS line_item_fk0 CASCADE;
ALTER TABLE IF EXISTS ONLY line_item
  DROP CONSTRAINT IF EXISTS line_item_fk1 CASCADE;
ALTER TABLE IF EXISTS ONLY shipping_details
  DROP CONSTRAINT IF EXISTS shipping_details_pk CASCADE;
ALTER TABLE IF EXISTS ONLY shipping_details
  DROP CONSTRAINT IF EXISTS shipping_details_fk0 CASCADE;


DROP TABLE IF EXISTS products;
CREATE TABLE products (
  id                  INTEGER      NOT NULL UNIQUE,
  name                VARCHAR(60)  NOT NULL UNIQUE,
  image               VARCHAR(60)  NOT NULL,
  default_price       FLOAT        NOT NULL,
  currency_string     VARCHAR(30)  NOT NULL,
  description         VARCHAR(255) NOT NULL,
  supplier_id         INTEGER      NOT NULL,
  product_category_id INTEGER      NOT NULL,
  CONSTRAINT products_pk PRIMARY KEY (id)
) WITH (
OIDS = FALSE
);


DROP TABLE IF EXISTS suppliers;
CREATE TABLE suppliers (
  id          INTEGER      NOT NULL UNIQUE,
  name        VARCHAR(60)  NOT NULL,
  description VARCHAR(255) NOT NULL,
  CONSTRAINT suppliers_pk PRIMARY KEY (id)
) WITH (
OIDS = FALSE
);


DROP TABLE IF EXISTS product_categories;
CREATE TABLE product_categories (
  id          INTEGER      NOT NULL UNIQUE,
  name        VARCHAR(60)  NOT NULL,
  department  VARCHAR(30)  NOT NULL,
  description VARCHAR(255) NOT NULL,
  CONSTRAINT product_categories_pk PRIMARY KEY (id)
) WITH (
OIDS = FALSE
);


DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
  id      INTEGER     NOT NULL UNIQUE,
  user_id INTEGER     NOT NULL,
  status  VARCHAR(30) NOT NULL,
  shipping_detail_id INTEGER,
  CONSTRAINT orders_pk PRIMARY KEY (id)
) WITH (
OIDS = FALSE
);


DROP TABLE IF EXISTS line_item;
CREATE TABLE line_item (
  id         INTEGER NOT NULL UNIQUE,
  product_id INTEGER NOT NULL,
  quantity   INTEGER NOT NULL,
  orders_id  INTEGER NOT NULL,
  CONSTRAINT line_item_pk PRIMARY KEY (id)
) WITH (
OIDS = FALSE
);


DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id         INTEGER NOT NULL UNIQUE,
  username   VARCHAR(255) NOT NULL,
  password   VARCHAR(255) NOT NULL,
  CONSTRAINT users_pk PRIMARY KEY (id)
) WITH (
OIDS = FALSE
);

DROP TABLE IF EXISTS shipping_details;
CREATE TABLE shipping_details (
  id          INTEGER      NOT NULL UNIQUE,
  first_name  VARCHAR(255)  NOT NULL,
  last_name   VARCHAR(255) NOT NULL,
  email       VARCHAR(255) NOT NULL,
  zip_code    INTEGER NOT NULL,
  city        VARCHAR(255) NOT NULL,
  address     VARCHAR(255) NOT NULL,
  user_id     INTEGER NOT NULL,
  CONSTRAINT shipping_details_pk PRIMARY KEY (id)
) WITH (
OIDS = FALSE
);

ALTER TABLE shipping_details
  ADD CONSTRAINT shipping_details_fk0 FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE products
  ADD CONSTRAINT products_fk0 FOREIGN KEY (supplier_id) REFERENCES suppliers (id);
ALTER TABLE products
  ADD CONSTRAINT products_fk1 FOREIGN KEY (product_category_id) REFERENCES product_categories (id);


ALTER TABLE line_item
  ADD CONSTRAINT line_item_fk0 FOREIGN KEY (product_id) REFERENCES products (id);
ALTER TABLE line_item
  ADD CONSTRAINT line_item_fk1 FOREIGN KEY (orders_id) REFERENCES orders (id);

ALTER TABLE orders
  ADD CONSTRAINT orders_fk0 FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE orders
  ADD CONSTRAINT orders_fk1 FOREIGN KEY (shipping_detail_id) REFERENCES shipping_details (id);
