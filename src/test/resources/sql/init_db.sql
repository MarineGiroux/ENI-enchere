--- STRUCTURE

-- ROLES
CREATE TABLE ROLES
(
    idRole int         NOT NULL,
    role   varchar(30) NOT NULL,
    CONSTRAINT idRole_pk PRIMARY KEY (idRole)
);

-- USERS
CREATE TABLE USERS
(
    idUser    int IDENTITY (1,1) NOT NULL,
    pseudo    varchar(30)        NOT NULL,
    lastName  varchar(30)        NOT NULL,
    firstName varchar(30)        NOT NULL,
    email     varchar(250)       NOT NULL,
    phone     varchar(15)        NULL,
    road      varchar(30)        NULL,
    zipPass   varchar(10)        NULL,
    city      varchar(30)        NULL,
    picture   varchar(250)       NULL,
    password  varchar(250)       NOT NULL,
    credit    int                NOT NULL,
    idRole    int                NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (idUser)
);

ALTER TABLE USERS
    ADD CONSTRAINT users_role_fk FOREIGN KEY (idRole) REFERENCES ROLES (idRole);

-- CATEGORIES
CREATE TABLE CATEGORIES
(
    idCategory   int         NOT NULL,
    categoryName varchar(30) NOT NULL,
    CONSTRAINT category_pk PRIMARY KEY (idCategory)
);

-- SOLD_ARTICLES
CREATE TABLE SOLD_ARTICLES
(
    idArticle         int IDENTITY (1,1) NOT NULL,
    nameArticle       varchar(30)        NOT NULL,
    description       varchar(300)       NOT NULL,
    startDateAuctions date               NOT NULL,
    endDateAuctions   date               NOT NULL,
    initialPrice      int                NULL,
    priceSale         int                NULL,
    saleStatus        bit                NULL,
    picture           varchar(250)       NULL,
    idUser            int                NOT NULL,
    idCategory        int                NOT NULL,
    CONSTRAINT articles_sold_pk PRIMARY KEY (idArticle)
);

ALTER TABLE SOLD_ARTICLES
    ADD CONSTRAINT sold_articles_categories_fk FOREIGN KEY (idCategory) REFERENCES CATEGORIES (idCategory);

ALTER TABLE SOLD_ARTICLES
    ADD CONSTRAINT sold_articles_users_sold_fk FOREIGN KEY (idUser) REFERENCES USERS (idUser);

-- PICKUP
CREATE TABLE PICKUP
(
    idPickUp  int IDENTITY (1,1) NOT NULL,
    idArticle int                NOT NULL,
    road      varchar(30)        NOT NULL,
    zipPass   varchar(15)        NOT NULL,
    city      varchar(30)        NOT NULL,
    CONSTRAINT pickup_pk PRIMARY KEY (idPickUp)
);

ALTER TABLE PICKUP
    ADD CONSTRAINT pickup_sold_articles_fk FOREIGN KEY (idArticle) REFERENCES SOLD_ARTICLES (idArticle);

-- AUCTIONS
CREATE TABLE AUCTIONS
(
    idUser         int      NOT NULL,
    idArticle      int      NOT NULL,
    dateAuctions   datetime NOT NULL,
    amountAuctions int      NOT NULL,
    CONSTRAINT auctions_pk PRIMARY KEY (idUser, idArticle)
);

ALTER TABLE AUCTIONS
    ADD CONSTRAINT auctions_sold_articles_fk FOREIGN KEY (idArticle) REFERENCES SOLD_ARTICLES (idArticle);

ALTER TABLE AUCTIONS
    ADD CONSTRAINT auctions_users_fk FOREIGN KEY (idUser) REFERENCES USERS (idUser);

--- DATAS
INSERT INTO CATEGORIES (idCategory, categoryName)
VALUES (1, 'Informatique'),
       (2, 'Meuble'),
       (3, 'Loisir'),
       (4, 'Vêtements'),
       (5, 'Animaux'),
       (6, 'Sport'),
       (7, 'Autre');

INSERT INTO ROLES (idRole, role)
VALUES (1, 'ADMIN'),
       (2, 'MEMBRE');