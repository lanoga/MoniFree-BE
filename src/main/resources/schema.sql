--DROP TABLE IF EXISTS service_log;
--DROP TABLE IF EXISTS service;
--DROP TABLE IF EXISTS service_result;
--DROP TABLE IF EXISTS users;
--DROP TABLE IF EXISTS user_log;
--DROP TABLE IF EXISTS application_data;
--DROP TABLE IF EXISTS monitor_endpoint;
--DROP TABLE IF EXISTS dashboard;

-- =========================
-- SERVICE LOG
-- =========================
CREATE TABLE if not exists service_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(255),
    date_time TIMESTAMP,
    result_code INTEGER,
    error_message VARCHAR(2000)
);

-- =========================
-- SERVICE REQUEST
-- =========================
CREATE TABLE if not exists service (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    server_url VARCHAR(500),
    fragment VARCHAR(255),
    name VARCHAR(255),
    interval_sec INTEGER,
    date_time TIMESTAMP,
    application_id INTEGER
);

-- =========================
-- SERVICE RESULT
-- =========================
CREATE TABLE if not exists service_result (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(255),
    service_id BIGINT,
    result CLOB,
    fragment VARCHAR(255),
    date_time TIMESTAMP
);

-- =========================
-- USERS
-- =========================
CREATE TABLE if not exists users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(255),
    family_name VARCHAR(255),
    role VARCHAR(50),
    password VARCHAR(255),
    totp_secret VARCHAR(255),
    two_factor_enabled boolean
);

-- =========================
-- USER LOG
-- =========================
CREATE TABLE if not exists user_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    date_time TIMESTAMP
);

CREATE TABLE if not exists monitor_endpoint (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fragment VARCHAR(100),
    interval_sec INTEGER,
    application_id INTEGER
);

CREATE TABLE if not exists application_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    url VARCHAR(500),
    auth_user VARCHAR(255),
    auth_password VARCHAR(255)
);

CREATE TABLE if not exists dashboard (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL,
    fragment VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    visual_appering VARCHAR(100),
    col_indx INTEGER,
    row_indx INTEGER,
    color VARCHAR(50)
);


