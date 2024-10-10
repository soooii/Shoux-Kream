-- 카테고리 추가
INSERT INTO category (name, created_at, updated_at) VALUES ('Electronics', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('Fashion', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('Home Appliances', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('Beauty', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('Toys', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('Books', NOW(), NOW());
INSERT INTO category (name, created_at, updated_at) VALUES ('Sports', NOW(), NOW());

-- 상품 추가
INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Laptop', 'Dell', 'Powerful laptop', 'A high-end laptop with 16GB RAM and 512GB SSD.', 'image1.jpg', 50, 1200.00, 'laptop, Dell, computer');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Smartphone', 'Samsung', 'Latest smartphone', 'The newest model with 128GB storage.', 'image2.jpg', 200, 999.99, 'smartphone, Samsung, mobile');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Headphones', 'Sony', 'Noise-cancelling headphones', 'Premium quality noise-cancelling headphones.', 'image3.jpg', 150, 299.99, 'headphones, Sony, audio');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Smart TV', 'LG', '4K Smart TV', 'Ultra HD 4K Smart TV with AI technology.', 'image4.jpg', 100, 799.99, 'smart TV, LG, 4K');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Gaming Console', 'Microsoft', 'Next-gen console', 'Next-generation gaming console with 1TB storage.', 'image5.jpg', 80, 499.99, 'gaming console, Microsoft, Xbox');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Smartwatch', 'Apple', 'Stylish smartwatch', 'Advanced smartwatch with health tracking features.', 'image6.jpg', 300, 399.99, 'smartwatch, Apple, wearable');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Bluetooth Speaker', 'JBL', 'Portable speaker', 'Compact portable Bluetooth speaker with high-quality sound.', 'image7.jpg', 250, 149.99, 'Bluetooth speaker, JBL, audio');

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, search_keywords)
VALUES ('Camera', 'Canon', 'Digital camera', 'High-resolution digital camera with interchangeable lenses.', 'image8.jpg', 60, 1099.99, 'camera, Canon, photography');

-- 유저 추가
INSERT INTO users (email, password, name, nickname, role, created_at, updated_at)
VALUES
    ('john.doe@example.com', 'hashed_password_1', 'John Doe', 'johnd', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('jane.smith@example.com', 'hashed_password_2', 'Jane Smith', 'janes', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin@example.com', 'hashed_password_3', 'Admin User', 'admin', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 카트 추가
INSERT INTO cart (quantity, users_id, items_id)
VALUES
    (2, 1, 1),
    (3, 2, 2),
    (1, 1, 3),
    (4, 1, 2);