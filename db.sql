CREATE TABLE roles (
	id VARCHAR(36),
	value VARCHAR(35),

	PRIMARY KEY ( id )
);

INSERT INTO roles VALUES ( '2fdc5b4b-dbe1-4770-9465-5ab9bf959c88', 'ROLE_USER' )

CREATE TABLE users (
	id VARCHAR(36),
	username VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	role_id VARCHAR(36),
	created_by VARCHAR(36),
	dt_ins DATETIME,
	updated_by VARCHAR(36),
	dt_upd DATETIME,

	PRIMARY KEY ( id ),
	CONSTRAINT fk_user_role FOREIGN KEY ( role_id ) REFERENCES roles( id )
);