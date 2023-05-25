-- Set variables for user IDs
DO $$
DECLARE
userId1 INTEGER := 1;
userId2 INTEGER := 2;
product1 INTEGER;
product2 INTEGER;
product3 INTEGER;
product4 INTEGER;
product5 INTEGER;
address1 INTEGER;
address2 INTEGER;
order1 INTEGER;
order2 INTEGER;
order3 INTEGER;
order4 INTEGER;
order5 INTEGER;
BEGIN
-- Delete existing data from tables
DELETE FROM shop_order_quantities WHERE id >= 1;
DELETE FROM shop_order WHERE id >= 1;
DELETE FROM inventory WHERE id >= 1;
DELETE FROM product WHERE id >= 1;
DELETE FROM address WHERE id >= 1;

-- Insert product data
INSERT INTO product (name, description, price) VALUES ('Product #1', 'This is a description of product #1.', 5.50) RETURNING id INTO product1;
INSERT INTO product (name, description, price) VALUES ('Product #2', 'This is a description of product #2.', 10.56) RETURNING id INTO product2;
INSERT INTO product (name, description, price) VALUES ('Product #3', 'This is a description of product #3.', 2.74) RETURNING id INTO product3;
INSERT INTO product (name, description, price) VALUES ('Product #4', 'This is a description of product #4.', 15.69) RETURNING id INTO product4;
INSERT INTO product (name, description, price) VALUES ('Product #5', 'This is a description of product #5.', 42.59) RETURNING id INTO product5;

-- Insert inventory data
INSERT INTO inventory (product_id, quantity) VALUES (product1, 5);
INSERT INTO inventory (product_id, quantity) VALUES (product2, 8);
INSERT INTO inventory (product_id, quantity) VALUES (product3, 12);
INSERT INTO inventory (product_id, quantity) VALUES (product4, 73);
INSERT INTO inventory (product_id, quantity) VALUES (product5, 2);

-- Insert address data
INSERT INTO address (address_line_1, city, country, user_id) VALUES ('123 Tester Hill', 'Testerton', 'England', userId1) RETURNING id INTO address1;
INSERT INTO address (address_line_1, city, country, user_id) VALUES ('312 Spring Boot', 'Hibernate', 'England', userId2) RETURNING id INTO address2;

-- Insert web order data
INSERT INTO shop_order (address_id, user_id) VALUES (address1, userId1) RETURNING id INTO order1;
INSERT INTO shop_order (address_id, user_id) VALUES (address1, userId1) RETURNING id INTO order2;
INSERT INTO shop_order (address_id, user_id) VALUES (address1, userId1) RETURNING id INTO order3;
INSERT INTO shop_order (address_id, user_id) VALUES (address2, userId2) RETURNING id INTO order4;
INSERT INTO shop_order (address_id, user_id) VALUES (address2, userId2) RETURNING id INTO order5;

INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order1, product1, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order1, product2, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order2, product3, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order2, product2, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order2, product5, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order3, product3, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order4, product4, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order4, product2, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order5, product3, 5);
INSERT INTO shop_order_quantities (order_id, product_id, quantity) VALUES (order5, product1, 5);

END $$;
