-- create customerDB database
drop database if exists customerDB;
create database customerDB;

use customerDB;

-- create customer table
drop table if exists customer;
create table customer(
	id varchar(12) not null,
    name varchar(10) not null,
    phone varchar(14) null,
    age int null,
    visitDate date not null,
    visitCount int not null,
    tasteScore int not null,
    cleanScore int not null,
    serviceScore int not null,
    total int null,
    avg decimal(2,1) null,
    grade char(1) null,
    review varchar(100) null,
    likeCount int null,
    reviewLength int,
	constraint pk_customer_id primary key(id),
    constraint idx_customer_phone unique index (phone)
);

-- check customer table
show index from customer;
describe customer;

-- create trigger deletecustomer table
drop table if exists deletecustomer;
create table deleteCustomer(
	id varchar(12) not null,
    name varchar(10) not null,
    phone varchar(14) null,
    age int null,
    visitDate date not null,
    visitCount int not null,
    tasteScore int not null,
    cleanScore int not null,
    serviceScore int not null,
    total int null,
    avg decimal(2,1) null,
    grade char(1) null,
    review varchar(100) null,
    likeCount int null,
    reviewLength int,
    deleteDate DATETIME
);

-- create trigger updatecustomer table
drop table if exists updatecustomer;
create table updateCustomer(
	id varchar(12) not null,
    name varchar(10) not null,
    phone varchar(14) null,
    age int null,
    visitDate date not null,
    visitCount int not null,
    tasteScore int not null,
    cleanScore int not null,
    serviceScore int not null,
    total int null,
    avg decimal(2,1) null,
    grade char(1) null,
    review varchar(100) null,
    likeCount int null,
    reviewLength int,
    updateDate DATETIME
);

-- check customer table
select * from customer;

-- insert a test record
insert into customer values('test01','김채은','010-2066-3333',24,'2021-10-10',2,3,3,3,9,3.6,'B',null,0,0);

-- delete the test record
delete from customer where id = 'test01';

-- create update procedure
drop procedure if exists procedure_updateName_customer;
delimiter $$
create procedure procedure_updateName_customer(
	IN up_name varchar(10),
    IN up_id varchar(12)
)
begin
	update customer set name = up_name where id = up_id;
end $$
delimiter ;

drop procedure if exists procedure_updatePhone_customer;
delimiter $$
create procedure procedure_updatePhone_customer(
	IN up_phone varchar(14),
    IN up_id varchar(12)
)
begin
	update customer set phone = up_phone where id = up_id;
end $$
delimiter ;

drop procedure if exists procedure_updateScore_customer;
delimiter $$
create procedure procedure_updateScore_customer(
	IN up_tastescore int,
    IN up_cleanscore int,
    IN up_servicescore int,
    IN up_total int,
    IN up_avg double,
    IN up_grade char(1),
    IN up_id varchar(12)
)
begin
	update customer set tasteScore = up_tastescore, cleanscore = up_cleanscore, servicescore = up_servicescore, 
					total = up_total, avg = up_avg, grade = up_grade where id = up_id;
end $$
delimiter ;

-- create delete procedure
drop procedure if exists procedure_delete_customer;
delimiter $$
create procedure procedure_delete_customer(
	IN del_id VARCHAR(12)
)
begin
	delete from customer where id = del_id;
end $$
delimiter ;

-- create insert procedure
drop procedure if exists procedure_insert_customer;
delimiter $$  
create procedure procedure_insert_customer(
	IN in_id varchar(12), 
	IN in_name varchar(10), 
    IN in_phone varchar(14),
    IN in_age int,
    IN in_visitdate date,
    IN in_visitcount int,
	IN in_tastescore int,
	IN in_cleanscore int,
	IN in_servicescore int,
    IN in_review varchar(100),
    IN in_likecount int,
    IN in_reviewlength int
)
begin
    DECLARE in_total int ;
    DECLARE in_avg decimal(2,1) ;
    DECLARE in_stargrade varchar(2) ;

    SET in_total = in_tastescore + in_cleanscore  + in_servicescore; 
    SET in_avg = in_total / 3.0; 
    SET in_stargrade = 
		CASE
			WHEN in_avg >= 5.0 THEN 'S'
            WHEN in_avg >= 4.0 THEN 'A'
            WHEN in_avg >= 3.0 THEN 'B'
            WHEN in_avg >= 2.0 THEN 'C'
            WHEN in_avg >= 1.0 THEN 'D'
            ELSE 'F'
		END;

    insert into customer(id, name, phone, age, visitdate, visitcount, tastescore, cleanscore, servicescore, review, likecount, reviewlength) 
		values(in_id, in_name, in_phone, in_age, in_visitdate, in_visitcount, in_tastescore, in_cleanscore, in_servicescore, in_review, in_likecount, in_reviewlength); 

    UPDATE customer set total = in_total, avg = in_avg, grade = in_stargrade where id = in_id; 
end $$
delimiter ;

-- set function setting
SET GLOBAL log_bin_trust_function_creators = 1;

-- create function Max/Min Average
drop function if exists getMaxAvg;
delimiter $$
create function getMaxAvg()
	returns decimal(2,1)
begin
	DECLARE result decimal(2,1);
    set result = 0.0;
    select max(avg) into result from customer;
    return result;
end $$
delimiter ;
select getMaxAvg();

drop function if exists getMinAvg;
delimiter $$
create function getMinAvg()
	returns double
begin
	DECLARE result double;
    set result = 0.0;
    select min(avg) into result from customer;
    return result;
end $$
delimiter ;

-- create function Max/Min LikeCount
drop function if exists getMaxLikeCount;
delimiter $$
create function getMaxLikeCount()
	returns int
begin
	DECLARE result int;
    set result = 0;
    select max(likeCount) into result from customer;
    return result;
end $$
delimiter ;

drop function if exists getMinLikeCount;
delimiter $$
create function getMinLikeCount()
	returns int
begin
	DECLARE result int;
    set result = 0;
    select min(likecount) into result from customer;
    return result;
end $$
delimiter ;

-- create function Max/Min ReviewLength
drop function if exists getMaxReviewLength;
delimiter $$
create function getMaxReviewLength()
	returns int
begin
	DECLARE result int;
    set result = 0;
    select max(reviewLength) into result from customer;
    return result;
end $$
delimiter ;

drop function if exists getMinReviewLength;
delimiter $$
create function getMinReviewLength()
	returns int
begin
	DECLARE result int;
    set result = 0;
    select min(reviewLength) into result from customer;
    return result;
end $$
delimiter ;

-- create deletecustomer trigger
drop trigger if exists trg_deleteCustomer;
delimiter $$
create trigger trg_deleteCustomer
	after delete
    on customer
    for each row
begin
	INSERT INTO `deleteCustomer` VALUES(old.id, old.name, old.phone, old.age, old.visitDate, old.visitCount,old.tasteScore,old.cleanScore,old.serviceScore,old.total,old.avg,old.grade,old.review,old.likecount,old.reviewlength,now());
end $$
delimiter ;

-- create updatecustomer trigger
drop trigger if exists trg_updateCustomer;
delimiter $$
create trigger trg_updateCustomer
	after update
    on customer
    for each row
begin
	INSERT INTO `updateCustomer` VALUES(old.id, old.name, old.phone, old.age, old.visitdate, old.visitcount,old.tastescore,old.cleanscore,old.servicescore,old.total,old.avg,old.grade,old.review,old.likecount,old.reviewlength,now());
end $$
delimiter ;

-- check delete/update trigger table
select * from updateCustomer;
select * from deleteCustomer;
