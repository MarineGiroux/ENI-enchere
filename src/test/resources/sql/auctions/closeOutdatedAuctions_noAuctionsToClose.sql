DELETE FROM SOLD_ARTICLES;
DELETE FROM USERS;

INSERT INTO USERS (pseudo, lastName, firstName, email, password, credit, idRole)
VALUES ('JohnDoe', 'Doe', 'John', 'john.doe@email.com', 'password of JohnDoe', 100, 2);

INSERT INTO SOLD_ARTICLES (nameArticle, description, startDateAuctions, endDateAuctions,
                           initialPrice, saleStatus, idUser, idCategory)
VALUES ('Souris', 'Souris ergo', '2024-12-15', '2025-01-02',
        50, false, (SELECT idUser FROM USERS WHERE email = 'john.doe@email.com'), 1);


