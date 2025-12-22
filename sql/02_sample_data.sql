-------------------------------------------------------
-- SAYFA 2: ÖRNEK VERİLER
-------------------------------------------------------

-- VENDOR
INSERT INTO vendor (vendor_name, email, phone, address) VALUES
                                                            ('Anka El Sanatları', 'info@ankaelsanatlari.com', '03124567890', 'Ankara'),
                                                            ('Mavi Çömlek Atölyesi', 'iletisim@mavicomlek.com', '02242334455', 'Kütahya'),
                                                            ('İz Craft Studio', 'contact@izcraft.com', '02324667788', 'İzmir'),
                                                            ('Doğa Örgü Evi', 'dogargu@gmail.com', '02862223344', 'Edirne'),
                                                            ('Serra Design Art', 'serra@designart.com', '02125559988', 'İstanbul');

-- CATEGORY
INSERT INTO category (category_name) VALUES
                                         ('Takı'), ('Ev Dekoru'), ('Seramik'),
                                         ('Tekstil'), ('Ahşap'), ('Tablo'), ('Aksesuar');

-- CUSTOMER
INSERT INTO customer (customer_name, email, phone, address) VALUES
                                                                ('Burak Yılmaz', 'burak@gmail.com', '05321112233', 'Ankara'),
                                                                ('Melisa Çetin', 'melisa@gmail.com', '05432223344', 'İzmir'),
                                                                ('Deniz Kara', 'deniz@gmail.com', '05067778899', 'Bursa'),
                                                                ('Ahmet Demir', 'ahmet@gmail.com', '05553334455', 'İstanbul');

-- PRODUCT
INSERT INTO product (vendor_id, category_id, product_name, description, price, stock_quantity) VALUES
                                                                                                   (1,1,'Gümüş Lotus Kolye','925 ayar el yapımı kolye',185,18),
                                                                                                   (2,3,'Seramik Kupa','El boyaması kupa',110,30),
                                                                                                   (3,6,'Minimal Kanvas Tablo','Akrilik tablo',320,8),
                                                                                                   (4,4,'El Örgüsü Atkı','Doğal yün atkı',210,22),
                                                                                                   (5,5,'Oyma Ahşap Kutu','El oyma kutu',275,10);

-- APP USER (şifreler demo amaçlı kısa)
INSERT INTO app_user (username, password_hash, role, vendor_id) VALUES
                                                                    ('admin','1234','ADMIN',NULL),
                                                                    ('anka','1234','VENDOR',1),
                                                                    ('mavi','1234','VENDOR',2),
                                                                    ('izcraft','1234','VENDOR',3),
                                                                    ('doga','1234','VENDOR',4),
                                                                    ('serra','1234','VENDOR',5),
                                                                    ('burak','1234','CUSTOMER',NULL);
