-------------------------------------------------------
-- SAYFA 1: TABLO TANIMLARI
-------------------------------------------------------

-- 1) VENDOR
CREATE TABLE vendor (
                        vendor_id   SERIAL PRIMARY KEY,
                        vendor_name VARCHAR(100) NOT NULL,
                        email       VARCHAR(100),
                        phone       VARCHAR(30),
                        address     VARCHAR(255)
);

-- 2) CATEGORY
CREATE TABLE category (
                          category_id   SERIAL PRIMARY KEY,
                          category_name VARCHAR(100) NOT NULL
);

-- 3) CUSTOMER
CREATE TABLE customer (
                          customer_id   SERIAL PRIMARY KEY,
                          customer_name VARCHAR(100) NOT NULL,
                          email         VARCHAR(100),
                          phone         VARCHAR(30),
                          address       VARCHAR(255)
);

-- 4) PRODUCT
CREATE TABLE product (
                         product_id     SERIAL PRIMARY KEY,
                         vendor_id      INT NOT NULL,
                         category_id    INT,
                         product_name   VARCHAR(150) NOT NULL,
                         description    VARCHAR(500),
                         price          NUMERIC(10,2) NOT NULL,
                         stock_quantity INT NOT NULL DEFAULT 0,
                         is_active      BOOLEAN NOT NULL DEFAULT TRUE,

                         CONSTRAINT fk_product_vendor
                             FOREIGN KEY (vendor_id)
                                 REFERENCES vendor(vendor_id)
                                 ON DELETE CASCADE,

                         CONSTRAINT fk_product_category
                             FOREIGN KEY (category_id)
                                 REFERENCES category(category_id)
                                 ON DELETE SET NULL
);

-- 5) CUSTOMER_ORDER
CREATE TABLE customer_order (
                                order_id     SERIAL PRIMARY KEY,
                                customer_id  INT NOT NULL,
                                order_date   TIMESTAMP NOT NULL DEFAULT NOW(),
                                total_amount NUMERIC(10,2) NOT NULL DEFAULT 0,
                                status       VARCHAR(20) NOT NULL DEFAULT 'PENDING',

                                CONSTRAINT fk_order_customer
                                    FOREIGN KEY (customer_id)
                                        REFERENCES customer(customer_id)
);

-- 6) ORDER_ITEM
CREATE TABLE order_item (
                            order_item_id SERIAL PRIMARY KEY,
                            order_id      INT NOT NULL,
                            product_id    INT NOT NULL,
                            quantity      INT NOT NULL CHECK (quantity > 0),
                            unit_price    NUMERIC(10,2) NOT NULL,
                            line_total    NUMERIC(10,2) NOT NULL,

                            CONSTRAINT fk_item_order
                                FOREIGN KEY (order_id)
                                    REFERENCES customer_order(order_id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_item_product
                                FOREIGN KEY (product_id)
                                    REFERENCES product(product_id)
);

-- 7) STOCK_MOVEMENT
CREATE TABLE stock_movement (
                                movement_id   SERIAL PRIMARY KEY,
                                product_id    INT NOT NULL,
                                movement_type VARCHAR(10) NOT NULL,
                                quantity      INT NOT NULL,
                                movement_date TIMESTAMP NOT NULL DEFAULT NOW(),
                                description   VARCHAR(255),

                                CONSTRAINT fk_movement_product
                                    FOREIGN KEY (product_id)
                                        REFERENCES product(product_id)
);

-- 8) APP_USER
CREATE TABLE app_user (
                          user_id       SERIAL PRIMARY KEY,
                          username      VARCHAR(50) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          role          VARCHAR(20) NOT NULL,
                          vendor_id     INT,

                          CONSTRAINT fk_user_vendor
                              FOREIGN KEY (vendor_id)
                                  REFERENCES vendor(vendor_id),

                          CONSTRAINT chk_user_role
                              CHECK (role IN ('ADMIN','VENDOR','CUSTOMER'))
);
