create database auctionDB;
use auctionDB;

CREATE TABLE Auction (
auction_id BIGINT NOT NULL AUTO_INCREMENT,
start_time datetime,
finish_time datetime,
auction_type varchar(20) COLLATE utf8_bin NOT NULL,
description varchar(200) COLLATE utf8_bin,
PRIMARY KEY(auction_id)
)ENGINE=InnoDB;

CREATE TABLE User (
user_id BIGINT NOT NULL AUTO_INCREMENT,
role_id BIGINT NOT NULL,
login varchar(20) COLLATE utf8_bin NOT NULL,
hash varchar(50) COLLATE utf8_bin NOT NULL,
first_name varchar(20) COLLATE utf8_bin NOT NULL,
last_name varchar(20) COLLATE utf8_bin NOT NULL,
email varchar(20) COLLATE utf8_bin,
phone varchar(20),
money BIGINT NOT NULL,
frozen_money BIGINT NOT NULL,
is_blocked BIGINT NOT NULL,
PRIMARY KEY(user_id),
FOREIGN KEY fk_user_to_role(role_id) REFERENCES role(role_id),
CONSTRAINT unique_login_password UNIQUE (login, hash)
)ENGINE=InnoDB;

CREATE TABLE Lot (
lot_id BIGINT NOT NULL AUTO_INCREMENT,
lot_name varchar (50) COLLATE utf8_bin NOT NULL DEFAULT "lot",
description varchar(200) COLLATE utf8_bin,
category_id BIGINT  NOT NULL,
auction_id BIGINT  NOT NULL,
user_id BIGINT  NOT NULL,
start_price int(20) NOT NULL,
min_step int(20),
is_paid BOOLEAN NOT NULL,
is_blocked INT NOT NULL,
PRIMARY KEY(lot_id),
FOREIGN KEY fk_lot_to_category(category_id) REFERENCES category(category_id),
FOREIGN KEY fk_lot_to_auction(auction_id) REFERENCES auction(auction_id),
FOREIGN KEY fk_lot_to_user(user_id) REFERENCES user (user_id)
)ENGINE=InnoDB;

CREATE TABLE Bid (
bid_id BIGINT NOT NULL AUTO_INCREMENT,
bid_amount int,
user_id BIGINT  NOT NULL,
lot_id BIGINT  NOT NULL,
PRIMARY KEY(bid_id),
FOREIGN KEY fk_bid_to_user(user_id) REFERENCES user (user_id),
FOREIGN KEY fk_bid_to_lot(lot_id) REFERENCES lot(lot_id)
)ENGINE=InnoDB;

CREATE TABLE Credit (
credit_id BIGINT NOT NULL AUTO_INCREMENT,
user_id BIGINT  NOT NULL,
debt_sum int,
recieved_data datetime,
payment_data datetime,
credit_percent int, 
PRIMARY KEY(credit_id),
FOREIGN KEY fk_credit_to_user(user_id) REFERENCES lot(user_id)
)ENGINE=InnoDB;


CREATE TABLE Category (
category_id BIGINT  NOT NULL AUTO_INCREMENT,
category_name varchar(20) COLLATE utf8_bin NOT NULL DEFAULT "category",
PRIMARY KEY(category_id)
)ENGINE=InnoDB ;

CREATE TABLE Role (
role_id BIGINT NOT NULL AUTO_INCREMENT,
role_name varchar(20) COLLATE utf8_bin NOT NULL DEFAULT "user",
PRIMARY KEY(role_id)
)ENGINE=InnoDB;

