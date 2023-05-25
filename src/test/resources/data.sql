INSERT INTO local_user (email, first_name, last_name, password, username, email_verified)
VALUES ('UserA@junit.com', 'UserA-FirstName', 'UserA-LastName', '$2a$10$lZTXqolYtlZdrRXsx7nJxOCxYqEyb/GGdefrZD7.m/L19nKOmDfKO', 'UserA', true),
       ('UserB@junit.com', 'UserB-FirstName', 'UserB-LastName', '$2a$10$ywutemP2NdEah8PSngjveukBmbWysyueNxquVxDZyxztT5rK469de', 'UserB', false),
       ('UserC@junit.com', 'UserC-FirstName', 'UserC-LastName', '$2a$10$10bNY8rRUeV5Frd9uLmzKupASNSv3uIkC7Pm1dz/UQL0jiCkS6Z72', 'UserC', false);

INSERT INTO address(address_line_1, city, country, user_id)
VALUES ('123 GreenHill', 'Berlin', 'Germany', 1),
       ('123 Spring', 'Warsaw', 'Poland', 3);

INSERT INTO product (name, description, price)
VALUES ('Product #1', 'This is a description of product #1.', 5.50),
       ('Product #2', 'This is a description of product #2.', 10.56),
       ('Product #3', 'This is a description of product #3.', 11.56),
       ('Product #4', 'This is a description of product #4.', 20.56),
       ('Product #5', 'This is a description of product #5.', 10.20);

INSERT INTO inventory(product_id, quantity)
VALUES (1, 5),
       (4, 10),
       (2, 6),
       (5, 8),
       (3, 6);

INSERT INTO shop_order (address_id, user_id)
VALUES (1, 1),
       (1, 1),
       (1, 1),
       (2, 3),
       (2, 3);

INSERT INTO shop_order_quantities (order_id, product_id, quantity)
VALUES (1, 1, 5),
       (1, 2, 5),
       (2, 3, 5),
       (2, 3, 5),
       (5, 3, 4);
