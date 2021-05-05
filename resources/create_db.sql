-- MySQL Script generated by MySQL Workbench
-- Fri Apr 30 16:40:35 2021
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `pay_my_buddy_test` DEFAULT CHARACTER SET utf8 ;
USE `pay_my_buddy_test` ;

-- -----------------------------------------------------
-- Table `pay_my_buddy_test`.`bank_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.bank_account (
  bank_account_id 	BIGINT NOT NULL 	AUTO_INCREMENT,
  account_number 	VARCHAR(255) NULL 	DEFAULT NULL,
  bank_counter_code INT NULL 			DEFAULT NULL,
  bic 				VARCHAR(255) NULL 	DEFAULT NULL,
  code_bank 		INT NULL 			DEFAULT NULL,
  domiciliation 	VARCHAR(255) NULL 	DEFAULT NULL,
  holder 			VARCHAR(255) NULL 	DEFAULT NULL,
  iban 				VARCHAR(255) NULL 	DEFAULT NULL,
  rib_key 			INT NULL 			DEFAULT NULL,
  PRIMARY KEY (bank_account_id))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`user_buddy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.user_buddy (
  user_id BIGINT 	NOT NULL 			AUTO_INCREMENT,
  address 			VARCHAR(255) NULL 	DEFAULT NULL,
  birthdate 		VARCHAR(255) NULL 	DEFAULT NULL,
  city 				VARCHAR(255) NULL 	DEFAULT NULL,
  email 			VARCHAR(255) NOT NULL,
  first_name 		VARCHAR(255) NULL 	DEFAULT NULL,
  last_name 		VARCHAR(255) NULL 	DEFAULT NULL,
  password 			VARCHAR(255) NOT NULL,
  phone 			VARCHAR(255) NULL 	DEFAULT NULL,
  zip 				INT NOT NULL,
  bank_account_id	BIGINT NULL 		DEFAULT NULL,
  PRIMARY KEY (user_id),
  UNIQUE INDEX UK_ccp2fp4vi9mqojrey84qf1qlm (email ASC) VISIBLE,
  UNIQUE INDEX UKccp2fp4vi9mqojrey84qf1qlm (email ASC) VISIBLE,
  INDEX FKretly0sw2ohces00thx1adn8q (bank_account_id ASC) VISIBLE,
  CONSTRAINT FKretly0sw2ohces00thx1adn8q
    FOREIGN KEY (bank_account_id)
    REFERENCES pay_my_buddy_test.bank_account (bank_account_id))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.account (
  account_id 	BIGINT NOT NULL 	AUTO_INCREMENT,
  balance 		DECIMAL(19,2) NULL 	DEFAULT NULL,
  user_id 		BIGINT NULL		 	DEFAULT NULL,
  PRIMARY KEY (account_id),
  UNIQUE INDEX UK_h6dr47em6vg85yuwt4e2roca4 (user_id ASC) VISIBLE,
  CONSTRAINT FKmaybqgj6htnp4iyblfusl9t8k
    FOREIGN KEY (user_id)
    REFERENCES pay_my_buddy_test.user_buddy (user_id))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`connexion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.connexion (
  user_id 			BIGINT NOT NULL,
  user_associate_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, user_associate_id),
  INDEX FK1f6pkrrtweawxbqm3dj8kklcr (user_associate_id ASC) VISIBLE,
  CONSTRAINT FK1f6pkrrtweawxbqm3dj8kklcr
    FOREIGN KEY (user_associate_id)
    REFERENCES pay_my_buddy.user_buddy (user_id),
  CONSTRAINT FK5p5qp0vv50deg2f3fj8gn4tla
    FOREIGN KEY (user_id)
    REFERENCES pay_my_buddy_test.user_buddy (user_id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.role (
  role_id 	BIGINT NOT NULL 	AUTO_INCREMENT,
  name 		VARCHAR(255) NULL 	DEFAULT NULL,
  PRIMARY KEY (role_id))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.transaction (
  transaction_id 			BIGINT NOT NULL 	AUTO_INCREMENT,
  amount 					DECIMAL(19,2) NULL 	DEFAULT NULL,
  date_transaction 			DATE NULL 			DEFAULT NULL,
  description 				VARCHAR(255) NULL 	DEFAULT NULL,
  fee 						DECIMAL(19,2) NULL 	DEFAULT NULL,
  type 						VARCHAR(255) NULL 	DEFAULT NULL,
  account_beneficiary_id 	BIGINT NULL 		DEFAULT NULL,
  account_sender_id 		BIGINT NULL 		DEFAULT NULL,
  PRIMARY KEY (transaction_id),
  INDEX FK6svsudycdnw1ycti1ey3yiu7f (account_beneficiary_id ASC) VISIBLE,
  INDEX FKcdyr60e14l2mm9y2hbups19n7 (account_sender_id ASC) VISIBLE,
  CONSTRAINT FK6svsudycdnw1ycti1ey3yiu7f
    FOREIGN KEY (account_beneficiary_id)
    REFERENCES pay_my_buddy.account (user_id),
  CONSTRAINT FKcdyr60e14l2mm9y2hbups19n7
    FOREIGN KEY (account_sender_id)
    REFERENCES pay_my_buddy_test.account (user_id))
ENGINE = InnoDB
AUTO_INCREMENT = 30
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.users_roles (
  user_id 	BIGINT 	NOT NULL,
  role_id 	BIGINT 	NOT NULL,
  PRIMARY KEY(user_id, role_id))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;

ALTER TABLE users_roles
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES user_buddy (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT fk_role_id
        FOREIGN KEY (role_id)
            REFERENCES role (role_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
