-------------------------------------------------------
-- SAYFA 3: STOK YÖNETİMİ
-------------------------------------------------------

-- Sipariş sonrası stok düşürme fonksiyonu
CREATE OR REPLACE FUNCTION fn_process_order_item()
RETURNS TRIGGER AS $$
DECLARE
v_stock INT;
BEGIN
SELECT stock_quantity
INTO v_stock
FROM product
WHERE product_id = NEW.product_id
    FOR UPDATE;

IF v_stock < NEW.quantity THEN
        RAISE EXCEPTION 'Yetersiz stok!';
END IF;

UPDATE product
SET stock_quantity = stock_quantity - NEW.quantity
WHERE product_id = NEW.product_id;

INSERT INTO stock_movement (product_id, movement_type, quantity, description)
VALUES (NEW.product_id, 'OUT', NEW.quantity, 'Sipariş nedeniyle stok düştü');

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
CREATE TRIGGER trg_process_order_item
    AFTER INSERT ON order_item
    FOR EACH ROW
    EXECUTE FUNCTION fn_process_order_item();
