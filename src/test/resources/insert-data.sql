DELETE FROM pay_my_buddy_test.connexion;
DELETE FROM pay_my_buddy_test.transaction;
DELETE FROM pay_my_buddy_test.users_roles;
DELETE FROM pay_my_buddy_test.role;
DELETE FROM pay_my_buddy_test.user_buddy;
DELETE FROM pay_my_buddy_test.bank_account;
DELETE FROM pay_my_buddy_test.account;
INSERT INTO role(role_id, name)
values
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

INSERT INTO bank_account (bank_account_id, code_bank, bank_counter_code, account_number, rib_key,domiciliation, iban, bic, holder)
values
(1, 20056, 2000, '12345j25', 32, 'banque postale, lyon', '2228222555522222', 'psttfr58520', 'aurelia marchand'),
(2, 20056, 2000, '12345j25', 32, 'banque postale, lyon', '2228222555522222', 'psttfr58520', 'brice theret'),
(3, 20056, 2000, '12345j25', 32, 'banque postale, lyon', '2228222555522222', 'psttfr58520', 'louis dupuis'),
(4, 20056, 2000, '12345j25', 32, 'banque postale, lyon', '2228222555522222', 'psttfr58520', 'emmy dupont'),
(5, 20056, 2000, '12345j25', 32, 'banque postale, lyon', '2228222555522222', 'psttfr58520', 'aurelia theret');


INSERT INTO user_buddy(user_id, email, password, first_name, last_name, birthdate, address, zip, city, phone, bank_account_id, active)
VALUES
(1, 'aur@gmail.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'aurelia', 'marchand', '1984-04-13', 'rue de la plaine', 95420, 'magny',  '0766665544', (SELECT bank_account_id FROM bank_account WHERE holder = 'aurelia marchand'),1),
(2, 'brice@gmail.com', '1234', 'brice', 'theret', '1979-12-29', 'allee des bois', 95420, 'magny', '0658547858', (SELECT bank_account_id FROM bank_account WHERE holder = 'brice theret'),1),
(3, 'louis@gmail.com', '1111', 'louis', 'dupuis', '1986-09-29', 'avenue de la paix', 95420, 'magny', '', (SELECT bank_account_id FROM bank_account WHERE holder = 'louis dupuis'),1),
(4, 'emmy@gmail.com', 'aaaa', 'emmy', 'dupont', '1963-07-20', 'chemin du val', 95420, 'magny', '', (SELECT bank_account_id FROM bank_account WHERE holder = 'emmy dupont'),0),
(5, 'admin@paymybuddy.com', 'admin1', 'aurelia', 'theret', '1984-04-13', 'rue de la plaine', 95420, 'magny', '0766665544', (SELECT bank_account_id FROM bank_account WHERE holder = 'aurelia theret'),1);


INSERT INTO connexion(user_id, user_associate_id)
values
((SELECT user_id from user_buddy where email='aur@gmail.com'),(SELECT user_id from user_buddy where email='brice@gmail.com')),
((SELECT user_id from user_buddy where email='aur@gmail.com'),(SELECT user_id from user_buddy where email='louis@gmail.com'));

INSERT INTO account (account_id, balance, user_id)
values
(1,379, (SELECT user_id from user_buddy where email='aur@gmail.com')),
(2,20, (SELECT user_id from user_buddy where email='brice@gmail.com')),
(3,0, (SELECT user_id from user_buddy where email='louis@gmail.com')),
(4,0, (SELECT user_id from user_buddy where email='emmy@gmail.com')),
(5,401, (SELECT user_id from user_buddy where email='admin@paymybuddy.com'));

INSERT INTO transaction(transaction_id, amount, date_transaction, type, description, fee, account_sender_id, account_beneficiary_id)
values
(1,500, '2020/05/05', 'BANK_TRANSFER', 'premier versement', 0.0, (SELECT account_id from account where user_id = 1),(SELECT account_id from account where user_id = 1)),
(2,20, '2020/07/05', 'USER_TO_USER', 'remboursement cadeau', 1.0,(SELECT account_id from account where user_id = 1),(SELECT account_id from account where user_id = 2)),
(3,100, '2020/09/05', 'BANK_TRANSFER', 'retrait', 0.0,(SELECT account_id from account where user_id = 1),(SELECT account_id from account where user_id = 1));



INSERT INTO users_roles(user_id, role_id)
values
((SELECT user_id from user_buddy where email='aur@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
((SELECT user_id from user_buddy where email='brice@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
((SELECT user_id from user_buddy where email='louis@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
((SELECT user_id from user_buddy where email='emmy@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
((SELECT user_id from user_buddy where email='admin@paymybuddy.com'),(SELECT role_id from role where name='ROLE_ADMIN'));
