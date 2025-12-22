-------------------------------------------------------
-- SAYFA 4: RAPORLAMA VE PROCEDURE
-------------------------------------------------------

-- Tarih aralığına göre satış raporu
CREATE OR REPLACE FUNCTION sales_report(start_date DATE, end_date DATE)
RETURNS TABLE (
    product_name VARCHAR,
    total_quantity INT,
    total_revenue NUMERIC
)
AS $$
BEGIN
RETURN QUERY
SELECT p.product_name,
       SUM(oi.quantity)::INT,
        SUM(oi.line_total)
FROM order_item oi
         JOIN product p ON oi.product_id = p.product_id
         JOIN customer_order o ON oi.order_id = o.order_id
WHERE o.order_date::date BETWEEN start_date AND end_date
GROUP BY p.product_name
ORDER BY total_revenue DESC;
END;
$$ LANGUAGE plpgsql;

-- Ürün soft delete
CREATE OR REPLACE PROCEDURE sp_delete_product(p_product_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
UPDATE product
SET is_active = FALSE
WHERE product_id = p_product_id;
END;
$$;
