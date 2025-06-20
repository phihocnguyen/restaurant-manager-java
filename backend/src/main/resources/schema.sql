-- CREATE DATABASE QLNH; (If needed)
-- \c QLNH; (Switch to the database)

CREATE TABLE EMPLOYEES (
                           EMP_ID SERIAL PRIMARY KEY,
                           EMP_NAME VARCHAR(50) NOT NULL,
                           EMP_ADDR VARCHAR(100),
                           EMP_PHONE VARCHAR(20) NOT NULL,
                           EMP_CCCD VARCHAR(12) UNIQUE,
                           EMP_ROLE VARCHAR(50),
                           EMP_SDATE TIMESTAMP,
                           EMP_WORKED_DAYS INT,
                           EMP_SALARY NUMERIC(18,2) NOT NULL,
                           ISDELETED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE CUSTOMERS (
                           CUS_ID SERIAL PRIMARY KEY,
                           CUS_NAME VARCHAR(50) NOT NULL,
                           CUS_ADDR VARCHAR(100),
                           CUS_PHONE VARCHAR(20) UNIQUE,
                           CUS_CCCD VARCHAR(12),
                           CUS_EMAIL VARCHAR(50),
                           ISVIP BOOLEAN NOT NULL DEFAULT FALSE,
                           ISDELETED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE DINING_TABLES (
                               TAB_ID SERIAL PRIMARY KEY,
                               TAB_NUM SMALLINT,
                               TAB_STATUS BOOLEAN NOT NULL,
                               ISDELETED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE INGREDIENTS (
                             INGRE_ID SERIAL PRIMARY KEY,
                             INGRE_NAME VARCHAR(30) NOT NULL,
                             INSTOCK_KG FLOAT NOT NULL,
                             INGRE_PRICE NUMERIC(18,2) DEFAULT 0,
                             ISDELETED BOOLEAN NOT NULL
);

CREATE TABLE MENU_ITEMS (
                            ITEM_ID SERIAL PRIMARY KEY,
                            ITEM_TYPE VARCHAR(10) CHECK (ITEM_TYPE IN ('FOOD', 'DRINK', 'OTHER')) NOT NULL,
                            ITEM_NAME VARCHAR(50) NOT NULL,
                            ITEM_IMG TEXT,
                            ITEM_CPRICE NUMERIC(18,2) NOT NULL,
                            ITEM_SPRICE NUMERIC(18,2) NOT NULL,
                            INSTOCK FLOAT DEFAULT 0,
                            ISDELETED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE RECIPES (
                         ITEM_ID INT,
                         INGRE_ID INT,
                         INGRE_QUANTITY_KG FLOAT NOT NULL,
                         PRIMARY KEY (ITEM_ID, INGRE_ID),
                         FOREIGN KEY (ITEM_ID) REFERENCES MENU_ITEMS(ITEM_ID) ON DELETE CASCADE,
                         FOREIGN KEY (INGRE_ID) REFERENCES INGREDIENTS(INGRE_ID) ON DELETE CASCADE
);

CREATE TABLE BOOKINGS (
                          BK_ID SERIAL PRIMARY KEY,
                          EMP_ID INT REFERENCES EMPLOYEES(EMP_ID) ON UPDATE CASCADE,
                          CUS_ID INT REFERENCES CUSTOMERS(CUS_ID) ON DELETE CASCADE,
                          TAB_ID INT REFERENCES DINING_TABLES(TAB_ID) ON UPDATE CASCADE,
                          BK_STIME TIMESTAMP NOT NULL,
                          BK_OTIME TIMESTAMP NOT NULL,
                          BK_STATUS SMALLINT NOT NULL CHECK (BK_STATUS IN (0,1,2)),
                          ISDELETED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE RECEIPTS (
                          REC_ID SERIAL PRIMARY KEY,
                          EMP_ID INT REFERENCES EMPLOYEES(EMP_ID) ON UPDATE CASCADE,
                          CUS_ID INT REFERENCES CUSTOMERS(CUS_ID) ON DELETE CASCADE,
                          TAB_ID INT REFERENCES DINING_TABLES(TAB_ID) ON UPDATE CASCADE,
                          REC_TIME TIMESTAMP NOT NULL,
                          REC_PAY NUMERIC(18,2) NOT NULL,
                          ISDELETED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE RECEIPT_DETAILS (
                                 REC_ID INT,
                                 ITEM_ID INT,
                                 QUANTITY INT NOT NULL,
                                 PRICE NUMERIC(18,2) NOT NULL,
                                 PRIMARY KEY (REC_ID, ITEM_ID),
                                 FOREIGN KEY (REC_ID) REFERENCES RECEIPTS(REC_ID) ON DELETE CASCADE,
                                 FOREIGN KEY (ITEM_ID) REFERENCES MENU_ITEMS(ITEM_ID) ON DELETE CASCADE
);

CREATE TABLE STOCKIN (
                         STO_ID SERIAL PRIMARY KEY,
                         STO_DATE TIMESTAMP NOT NULL,
                         STO_PRICE NUMERIC(18,2) NOT NULL
);

CREATE TABLE STOCKIN_DETAILS_INGRE (
                                       STO_ID INT,
                                       INGRE_ID INT,
                                       QUANTITY_KG FLOAT NOT NULL,
                                       TOTAL_CPRICE NUMERIC(18,2) DEFAULT 0,
                                       CPRICE NUMERIC(18,2) NOT NULL,
                                       PRIMARY KEY (STO_ID, INGRE_ID),
                                       FOREIGN KEY (STO_ID) REFERENCES STOCKIN(STO_ID) ON DELETE CASCADE,
                                       FOREIGN KEY (INGRE_ID) REFERENCES INGREDIENTS(INGRE_ID) ON DELETE CASCADE
);

CREATE TABLE STOCKIN_DETAILS_DRINK_OTHER (
                                             STO_ID INT,
                                             ITEM_ID INT,
                                             QUANTITY_UNITS INT NOT NULL,
                                             CPRICE NUMERIC(18,2) NOT NULL,
                                             TOTAL_CPRICE NUMERIC(18,2) DEFAULT 0,
                                             PRIMARY KEY (STO_ID, ITEM_ID),
                                             FOREIGN KEY (STO_ID) REFERENCES STOCKIN(STO_ID) ON DELETE CASCADE,
                                             FOREIGN KEY (ITEM_ID) REFERENCES MENU_ITEMS(ITEM_ID) ON DELETE CASCADE
);

CREATE TABLE FINANCIAL_HISTORY (
                                   FIN_ID SERIAL PRIMARY KEY,
                                   FIN_DATE TIMESTAMP NOT NULL,
                                   DESCRIPTION VARCHAR(255),
                                   TYPE VARCHAR(10) CHECK (TYPE IN ('INCOME', 'EXPENSE', 'PROFIT')),
                                   AMOUNT NUMERIC(18,2) NOT NULL DEFAULT 0,
                                   REFERENCE_ID INT NULL, -- ID liên kết với bảng RECEIPT hoặc STOCKIN nếu cần
                                   REFERENCE_TYPE VARCHAR(20) NULL -- Loại liên kết ('RECEIPT', 'STOCKIN', 'OTHER')
);

CREATE TABLE ACCOUNT_ROLE (
                              ROLE_ID SERIAL PRIMARY KEY,
                              ROLE_NAME TEXT
);

CREATE TABLE ACCOUNT (
                         ACC_USERNAME VARCHAR(100) PRIMARY KEY,
                         ACC_PASSWORD VARCHAR(100) NOT NULL,
                         ACC_EMAIL VARCHAR(100) UNIQUE,
                         ACC_DISPLAYNAME VARCHAR(100) NOT NULL,
                         ACC_GENDER VARCHAR(5) NOT NULL,
                         ACC_BDAY DATE NOT NULL,
                         ACC_ADDRESS VARCHAR(100) NOT NULL,
                         ACC_PHONE VARCHAR(20) NOT NULL,
                         ROLE_ID INT REFERENCES ACCOUNT_ROLE(ROLE_ID) ON DELETE CASCADE,
                         ISDELETED BOOLEAN NOT NULL DEFAULT FALSE
);

-- Order Online table
CREATE TABLE IF NOT EXISTS order_online (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(100),
    customer_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    note TEXT,
    total_amount DECIMAL(10,2) NOT NULL,
    order_time TIMESTAMP NOT NULL,
    delivery_time TIMESTAMP,
    status VARCHAR(20) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES ACCOUNT(ACC_USERNAME)
);

CREATE TABLE IF NOT EXISTS order_online_details (
    id SERIAL PRIMARY KEY,
    order_online_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    note TEXT,
    FOREIGN KEY (order_online_id) REFERENCES order_online(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES MENU_ITEMS(ITEM_ID) ON DELETE CASCADE
);

ALTER TABLE receipts ADD COLUMN payment_method VARCHAR(50);
ALTER TABLE order_online ADD COLUMN payment_method VARCHAR(50);

ALTER TABLE CUSTOMERS
ADD COLUMN ACC_USERNAME_FK VARCHAR(100);

ALTER TABLE CUSTOMERS
ADD CONSTRAINT FK_CUSTOMER_ACCOUNT
FOREIGN KEY (ACC_USERNAME_FK) REFERENCES ACCOUNT(ACC_USERNAME)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE BOOKINGS ADD COLUMN SPECIAL_REQUEST TEXT;

ALTER TABLE order_online
ADD COLUMN employee_id INT NULL,
ADD CONSTRAINT fk_order_online_employee
    FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(EMP_ID) ON UPDATE CASCADE;

ALTER TABLE order_online
ADD COLUMN payment_image VARCHAR(255) NULL;

-- Thêm trường verified cho bảng account nếu chưa có
ALTER TABLE account ADD COLUMN IF NOT EXISTS verified BOOLEAN; 