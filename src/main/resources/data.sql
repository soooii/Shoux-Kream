-- 카테고리 추가
INSERT INTO category (title, description, theme_class, image_url, created_at, updated_at)
VALUES ('아티스트협업', '아티스트와의 협업 상품', '#ebfffc', '/img/item/item1.png', NOW(), NOW());

INSERT INTO category (title, description, theme_class, image_url, created_at, updated_at)
VALUES ('#러닝', '러닝 관련 상품', '#eff1fa', '/img/item/item2.png', NOW(), NOW());

INSERT INTO category (title, description, theme_class, image_url, created_at, updated_at)
VALUES ('추천', '추천 상품', '#eff5fb', '/img/item/item3.png', NOW(), NOW());

INSERT INTO category (title, description, theme_class, image_url, created_at, updated_at)
VALUES ('랭킹', '랭킹 상위 상품', '#effaf5', '/img/item/item4.png', NOW(), NOW());

INSERT INTO category (title, description, theme_class, image_url, created_at, updated_at)
VALUES ('럭셔리', '럭셔리 상품', '#fffaeb', '/img/item/item5.png', NOW(), NOW());

INSERT INTO category (title, description, theme_class, image_url, created_at, updated_at)
VALUES ('남성', '남성용 상품', '#feecf0', '/img/item/item6.png', NOW(), NOW());

INSERT INTO category (title, description, theme_class, image_url, created_at, updated_at)
VALUES ('여성', '여성용 상품', '#00d1b2', '/img/item/item7.png', NOW(), NOW());

-- 상품 추가
-- Item 데이터 삽입
INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Laptop', 'Dell', 'Powerful laptop', 'A high-end laptop with 16GB RAM and 512GB SSD.', '/img/item/item1.png', 50, 1200.00, 1);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Smartphone', 'Samsung', 'Latest smartphone', 'The newest model with 128GB storage.', '/img/item/item2.png', 200, 999.99, 2);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Headphones', 'Sony', 'Noise-cancelling headphones', 'Premium quality noise-cancelling headphones.', '/img/item/item3.png', 150, 299.99, 3);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Smart TV', 'LG', '4K Smart TV', 'Ultra HD 4K Smart TV with AI technology.', '/img/item/item4.png', 100, 799.99, 4);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Gaming Console', 'Microsoft', 'Next-gen console', 'Next-generation gaming console with 1TB storage.', '/img/item/item5.png', 80, 499.99, 5);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Smartwatch', 'Apple', 'Stylish smartwatch', 'Advanced smartwatch with health tracking features.', '/img/item/item6.png', 300, 399.99, 6);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Bluetooth Speaker', 'JBL', 'Portable speaker', 'Compact portable Bluetooth speaker with high-quality sound.', '/img/item/item7.png', 250, 149.99, 7);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Camera', 'Canon', 'Digital camera', 'High-resolution digital camera with interchangeable lenses.', '/img/item/item8.png', 60, 1099.99, 1);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Tablet', 'Apple', 'Sleek and powerful tablet', 'High-performance tablet with 256GB storage and Retina display.', '/img/item/item9.png', 120, 799.99, 2);

INSERT INTO item (title, manufacturer, short_description, detail_description, image_key, inventory, price, category_id)
VALUES ('Wireless Mouse', 'Logitech', 'Ergonomic wireless mouse', 'Comfortable and precise wireless mouse with long battery life.', '/img/item/item10.png', 400, 49.99, 3);


-- 유저 추가
INSERT INTO users (email, password, name, nickname, role, created_at, updated_at)
VALUES
    ('1@xx.xx', '$2a$10$OHFb4wh54/5Lzeh2lJWvdOHH9SGvSNuJKWhZKoQD7eFzyZWFeFFM2', 'John Doe', 'johnd', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('1@1', '$2a$12$VzW8rQpaVyQU87gaE/p7KOdRauBsGL3QINCWqau..E197q4RTCVOK', 'Jane Smith', 'janes', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin@example.com', 'hashed_password_3', 'Admin User', 'admin', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 카트 추가
INSERT INTO cart (quantity, users_id, items_id, selected)
VALUES
    (2, 1, 1, true),
    (3, 2, 2, true),
    (1, 1, 3, false),
    (4, 1, 2, true);


-- 주소 추가
-- Insert into UserAddress (for user_id = 1)
INSERT INTO addresses (recipient_name, recipient_phone, postal_code, address1, address2, user_id)
VALUES ('John Doe', '123-456-7890', '10001', '123 Main St', 'Apt 4B', 2);

-- Insert into UserAddress (for user_id = 2)
INSERT INTO addresses (recipient_name, recipient_phone, postal_code, address1, address2, user_id)
VALUES ('Jane Smith', '987-654-3210', '20002', '456 Elm St', 'Suite 5C', 2);

-- Insert into UserAddress (for user_id = 3)
INSERT INTO addresses (recipient_name, recipient_phone, postal_code, address1, address2, user_id)
VALUES ('Alice Brown', '456-789-1234', '30003', '789 Oak St', 'House 10', 2);

-- Insert into UserAddress (for user_id = 4)
INSERT INTO addresses (recipient_name, recipient_phone, postal_code, address1, address2, user_id)
VALUES ('Bob Johnson', '321-654-9870', '40004', '321 Pine St', 'Apt 12A', 2);

-- Insert into UserAddress (for user_id = 5)
INSERT INTO addresses (recipient_name, recipient_phone, postal_code, address1, address2, user_id)
VALUES ('Charlie Davis', '654-321-0987', '50005', '654 Maple St', 'Unit 3', 2);

-- 주문 내역 없는 주소지 추가 (주소록 페이지에서 삭제 가능한 배송지)
INSERT INTO addresses (recipient_name, recipient_phone, postal_code, address1, address2, user_id)
VALUES ('Evelyn Brown', '321-654-0987', '50009', '987 Cedar Dr', 'Suite 100', 2);


-- 주문 내역 추가

INSERT INTO check_out (summary_title, total_price, delivery_status, user_id, addresses_id, request, created_at, updated_at)
VALUES ('Order Summary 1', 10000, 'READY', 2, 1, 'Please leave at the door', NOW(), NOW());

-- Insert into CheckOut with DeliveryStatus as START
INSERT INTO check_out (summary_title, total_price, delivery_status, user_id, addresses_id, request, created_at, updated_at)
VALUES ('Order Summary 2', 15000, 'START', 2, 2, 'Handle with care', NOW(), NOW());

-- Insert into CheckOut with DeliveryStatus as SHIPPING
INSERT INTO check_out (summary_title, total_price, delivery_status, user_id, addresses_id, request, created_at, updated_at)
VALUES ('Order Summary 3', 20000, 'SHIPPING', 2, 3, 'Call me upon arrival', NOW(), NOW());

-- Insert into CheckOut with DeliveryStatus as ARRIVED
INSERT INTO check_out (summary_title, total_price, delivery_status, user_id, addresses_id, request, created_at, updated_at)
VALUES ('Order Summary 4', 25000, 'ARRIVED', 2, 4, 'Leave it at the front desk', NOW(), NOW());

-- Insert into CheckOut with DeliveryStatus as READY (another example)
INSERT INTO check_out (summary_title, total_price, delivery_status, user_id, addresses_id, request, created_at, updated_at)
VALUES ('Order Summary 5', 30000, 'READY', 2, 5, 'Ring the bell', NOW(), NOW());