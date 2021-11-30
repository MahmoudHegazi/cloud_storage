
CREATE TABLE IF NOT EXISTS USERS (
  `userid` int(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20),
  `salt` VARCHAR(255) NULL,
  `password` VARCHAR(255),
  `firstname` VARCHAR(20),
  `lastname` VARCHAR(20)
);

'''Drop TABLE IF EXISTS NOTES;'''
CREATE TABLE IF NOT EXISTS NOTES (
    note_id INT PRIMARY KEY auto_increment,
    note_title VARCHAR(20),
    note_description VARCHAR (1000),
    user_id INT,
    foreign key (user_id) references USERS(user_id)
);

'''Drop TABLE IF EXISTS FILES;'''
CREATE TABLE IF NOT EXISTS FILES (
    `file_id` INT PRIMARY KEY auto_increment,
    `file_name` VARCHAR,
    `file_size` VARCHAR,
    `content_type` VARCHAR,
    `user_id` INT,
    `file_data` BLOB NULL,
    foreign key (user_id) references USERS(user_id)
);


'''Drop TABLE IF EXISTS CREDENTIALS;'''
CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentials_id INT PRIMARY KEY auto_increment,
    url VARCHAR(100) NOT NULL,
    uname VARCHAR NOT NULL,
    secret_key VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    user_id INT NOT NULL,
    foreign key (user_id) references USERS(user_id)
);