CREATE TABLE product (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(128) NOT NULL,
                         price DECIMAL(10,2) NOT NULL,
                         status TINYINT NOT NULL DEFAULT 1 COMMENT '1上架 0下架',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '商品';

CREATE TABLE device (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        device_no VARCHAR(64) NOT NULL UNIQUE COMMENT '设备编号',
                        location VARCHAR(255) NOT NULL COMMENT '投放位置',
                        online_status TINYINT NOT NULL DEFAULT 0 COMMENT '0离线 1在线',
                        last_heartbeat_at DATETIME NULL COMMENT '最后心跳时间',
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '售货机设备';

CREATE TABLE device_slot (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             device_id BIGINT NOT NULL,
                             slot_no VARCHAR(16) NOT NULL COMMENT '货道号',
                             product_id BIGINT NOT NULL,
                             local_stock INT NOT NULL DEFAULT 0 COMMENT '设备本地库存',
                             capacity INT NOT NULL DEFAULT 0 COMMENT '货道容量',
                             UNIQUE KEY uk_device_slot (device_id, slot_no)
) COMMENT '设备货道';

CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        order_no VARCHAR(64) NOT NULL UNIQUE,
                        device_id BIGINT NOT NULL,
                        slot_no VARCHAR(16) NOT NULL,
                        product_id BIGINT NOT NULL,
                        amount DECIMAL(10,2) NOT NULL,
                        status VARCHAR(32) NOT NULL COMMENT 'CREATED/PAID/DISPENSING/SUCCESS/FAILED/REFUNDING/REFUNDED',
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '订单';

ALTER TABLE orders
    ADD COLUMN expire_at DATETIME NULL,
    ADD COLUMN paid_at DATETIME NULL,
    ADD COLUMN dispensed_at DATETIME NULL,
    ADD COLUMN fail_reason VARCHAR(64) NULL,
    ADD COLUMN version INT NOT NULL DEFAULT 0;

CREATE TABLE payment (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         order_id BIGINT NOT NULL,
                         payment_no VARCHAR(64) NOT NULL,
                         idempotency_key VARCHAR(128) NOT NULL,
                         callback_status VARCHAR(32) NOT NULL COMMENT 'RECEIVED/PROCESSED/IGNORED',
                         callback_body TEXT NULL,
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         UNIQUE KEY uk_idempotency_key (idempotency_key),
                         UNIQUE KEY uk_payment_no (payment_no)
) COMMENT '支付流水';