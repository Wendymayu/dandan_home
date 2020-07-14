`information_schema`CREATE DATABASE community;

CREATE TABLE USER
(
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    accountId VARCHAR(100),
    NAME VARCHAR(50),
    token VARCHAR(36),
    gmtCreate BIGINT,
    gmtModified BIGINT
);

DROP TABLE USER;

CREATE TABLE question
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50),
    description TEXT,
    gmtCreate BIGINT,
    gmtModified BIGINT,
    creator INT,
    commentCount INT DEFAULT 0,
    viewCount INT DEFAULT 0,
    likeCount INT DEFAULT 0,
    tag VARCHAR(256)
);

/*删除表`user`*/
DROP TABLE question;

/*删除表中一列*/
ALTER TABLE USER DROP COLUMN avatar_url;