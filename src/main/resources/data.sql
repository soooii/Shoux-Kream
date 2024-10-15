-- 카테고리 추가
INSERT INTO category (name, created_at, updated_at) VALUES ('아티스트협업', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('#러닝', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('추천', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('랭킹', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('럭셔리', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('남성', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('여성', NOW(), NOW());

-- 상품 추가
INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Laptop', 'Dell', 'Powerful laptop', 'A high-end laptop with 16GB RAM and 512GB SSD.', '/img/item/item1.png', 50, 1200.00, 'laptop, Dell, computer');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Smartphone', 'Samsung', 'Latest smartphone', 'The newest model with 128GB storage.', '/img/item/item2.png', 200, 999.99, 'smartphone, Samsung, mobile');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Headphones', 'Sony', 'Noise-cancelling headphones', 'Premium quality noise-cancelling headphones.', '/img/item/item3.png', 150, 299.99, 'headphones, Sony, audio');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Smart TV', 'LG', '4K Smart TV', 'Ultra HD 4K Smart TV with AI technology.', '/img/item/item4.png', 100, 799.99, 'smart TV, LG, 4K');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Gaming Console', 'Microsoft', 'Next-gen console', 'Next-generation gaming console with 1TB storage.', '/img/item/item5.png', 80, 499.99, 'gaming console, Microsoft, Xbox');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Smartwatch', 'Apple', 'Stylish smartwatch', 'Advanced smartwatch with health tracking features.', '/img/item/item6.png', 300, 399.99, 'smartwatch, Apple, wearable');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Bluetooth Speaker', 'JBL', 'Portable speaker', 'Compact portable Bluetooth speaker with high-quality sound.', '/img/item/item7.png', 250, 149.99, 'Bluetooth speaker, JBL, audio');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Camera', 'Canon', 'Digital camera', 'High-resolution digital camera with interchangeable lenses.', '/img/item/item8.png', 60, 1099.99, 'camera, Canon, photography');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Tablet', 'Apple', 'Sleek and powerful tablet', 'High-performance tablet with 256GB storage and Retina display.', '/img/item/item9.png', 120, 799.99, 'tablet, Apple, iPad, mobile');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Wireless Mouse', 'Logitech', 'Ergonomic wireless mouse', 'Comfortable and precise wireless mouse with long battery life.', '/img/item/item10.png', 400, 49.99, 'wireless mouse, Logitech, computer accessories');

-- 유저 추가
INSERT INTO users (email, password, name, nickname, role, created_at, updated_at)
VALUES
    ('1@xx.xx', '$2a$10$OHFb4wh54/5Lzeh2lJWvdOHH9SGvSNuJKWhZKoQD7eFzyZWFeFFM2', 'John Doe', 'johnd', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('jane.smith@example.com', 'hashed_password_2', 'Jane Smith', 'janes', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin@example.com', 'hashed_password_3', 'Admin User', 'admin', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 카트 추가
INSERT INTO cart (quantity, users_id, items_id, selected)
VALUES
    (2, 1, 1, true),
    (3, 2, 2, true),
    (1, 1, 3, false),
    (4, 1, 2, true);