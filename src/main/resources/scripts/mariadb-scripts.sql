DROP DATABASE sfg_dev;
DROP DATABASE sfg_prod;
CREATE DATABASE sfg_dev;
CREATE DATABASE sfg_prod;

#Create database service accounts
DROP USER 'sfg_dev_user'@'localhost';
DROP USER 'sfg_dev_user'@'%';
DROP USER 'sfg_prod_user'@'localhost';
DROP USER 'sfg_prod_user'@'%';

CREATE USER 'sfg_dev_user'@'localhost' IDENTIFIED BY 'pa55w0rd';
CREATE USER 'sfg_prod_user'@'localhost' IDENTIFIED BY 'pa55w0rd';
CREATE USER 'sfg_dev_user'@'%' IDENTIFIED BY 'pa55w0rd';
CREATE USER 'sfg_prod_user'@'%' IDENTIFIED BY 'pa55w0rd';

#Database grants
GRANT SELECT ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT INSERT ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT DELETE ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT UPDATE ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT SELECT ON sfg_prod.* to 'sfg_prod_user'@'localhost';
GRANT INSERT ON sfg_prod.* to 'sfg_prod_user'@'localhost';
GRANT DELETE ON sfg_prod.* to 'sfg_prod_user'@'localhost';
GRANT UPDATE ON sfg_prod.* to 'sfg_prod_user'@'localhost';
GRANT SELECT ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT INSERT ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT DELETE ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT UPDATE ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT SELECT ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT INSERT ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT DELETE ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT UPDATE ON sfg_prod.* to 'sfg_prod_user'@'%';