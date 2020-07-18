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


ALTER TABLE question ADD id BIGINT AUTO_INCREMENT PRIMARY KEY;
ALTER TABLE `user` MODIFY id BIGINT NOT NULL;
ALTER TABLE question MODIFY creator BIGINT NOT NULL;
ALTER TABLE `comment` MODIFY commentator BIGINT NOT NULL;

DROP TABLE USER;

ALTER TABLE question DROP COLUMN id;

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
/*创建评论表*/
CREATE TABLE COMMENT
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parentId BIGINT NOT NULL,
    commentType INT NOT NULL,
    commentator INT NOT NULL,
    gmtCreate BIGINT NOT NULL,
    gmtModified BIGINT NOT NULL,
    likeCount BIGINT DEFAULT 0,
    content VARCHAR(1024) NULL,
    commentCount BIGINT DEFAULT 0
);

alter table comment MODIFY commentCount bigint default 0;

/*删除表`user`*/
DROP TABLE question;

DROP TABLE comment;

/*删除表中一列*/
ALTER TABLE USER DROP COLUMN avatar_url;


create table notification
(
    id bigint auto_increment primary key,
    notifier bigint not null,
    receiver bigint not null,
    outerId bigint not null,
    notiType int not null,
    gmtCreate bigint not null,
    notiStatus int default 0 not null,
    notifierName varchar(100) null,
    outerTitle varchar(256) null
);