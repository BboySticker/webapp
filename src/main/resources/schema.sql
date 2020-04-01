create table if not exists users
(
	username varchar(50) not null
		primary key,
	password varchar(70) not null,
	enabled tinyint(1) not null
);

create table if not exists user
(
	id char(40) null,
	first_name varchar(20) null,
	last_name varchar(20) null,
	email_address char(50) null,
	password char(70) null,
	account_created char(50) null,
	account_updated char(30) null
);

create table if not exists authorities
(
	username varchar(50) not null,
	authority varchar(50) not null
);

create table if not exists bill
(
	id char(40) null,
	created_ts char(40) null,
	updated_ts char(40) null,
	owner_id char(40) null,
	vendor char(20) null,
	bill_date char(40) null,
	due_date char(40) null,
	amount_due double null,
	categories char(40) null,
	payment_status enum('paid', 'due', 'past_due', 'no_payment_required') null,
	attachment_id char(50) null
);

create table if not exists bill_categories
(
	bill_id char(60) null,
	categories char(20) null
);

create table if not exists file
(
	id char(50) not null
		primary key,
	file_name char(100) null,
	url char(150) null,
	upload_date char(30) null,
	size mediumtext null,
	bill_id char(50) null,
	owner_id char(50) null,
	s3_metadata varchar(255) null
);


create table if not exists image
(
	id varchar(50) not null
		primary key,
	url varchar(255) null
);

create table if not exists nutrition_information
(
	id varchar(50) not null
		primary key,
	calories int null,
	cholesterol_in_mg float null,
	sodium_in_mg int null,
	carbohydrates_in_grams float null,
	protein_in_grams float null
);

create table if not exists ordered_list
(
	id varchar(50) not null
		primary key,
	position int null,
	items varchar(255) null,
	recipe_id varchar(50) null
);

create table if not exists recipe
(
	id varchar(50) not null
		primary key,
	image_id varchar(50) null,
	created_ts varchar(50) null,
	updated_ts varchar(50) null,
	author_id varchar(50) null,
	cook_time_in_min int null,
	prep_time_in_min int null,
	total_time_in_min int null,
	title varchar(100) null,
	cuisine varchar(50) null,
	servings varchar(50) null,
	ingredients varchar(50) null,
	steps varchar(255) null,
	nutrition_information varchar(50) null
);

create table if not exists recipe_ingredients
(
	recipe_id varchar(50) null,
	ingredients varchar(255) null
);

create table if not exists record_duebills
(
	record_id varchar(40) not null,
	due_bill varchar(40) not null
);

drop table if exists record;

create table if not exists record
(
	id varchar(40) not null
		primary key,
	owner_id varchar(40) not null,
	due_bill varchar(40) null
);


