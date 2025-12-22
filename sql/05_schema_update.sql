-------------------------------------------------------
-- SAYFA 5: SCHEMA EVOLUTION
-------------------------------------------------------

-- CUSTOMER kullanıcıları müşteri profiline bağlanır
ALTER TABLE app_user
    ADD COLUMN customer_id INT UNIQUE;

ALTER TABLE app_user
    ADD CONSTRAINT fk_user_customer
        FOREIGN KEY (customer_id)
            REFERENCES customer(customer_id)
            ON DELETE CASCADE;

-- Siparişlerin satıcı bilgisi
ALTER TABLE customer_order
    ADD COLUMN vendor_id INT NOT NULL;

ALTER TABLE customer_order
    ADD CONSTRAINT fk_order_vendor
        FOREIGN KEY (vendor_id)
            REFERENCES vendor(vendor_id);
