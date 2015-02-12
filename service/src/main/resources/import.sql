-- You can use this file to load seed data into the database using SQL statements
INSERT INTO USUARIO_SISTEMA(ID_USUARIO_SISTEMA, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL) VALUES (2, 'otavio', 'gudiao', NOW(), 'fulano@gmail.com');

INSERT INTO PAPEL(ID, NOME) VALUES (1, 'ROLE_ADMINISTRATOR');
INSERT INTO PAPEL(ID, NOME) VALUES (2, 'ROLE_USER');

INSERT INTO USUARIO_SISTEMA_PAPEL(USUARIO_SISTEMA_ID_USUARIO_SISTEMA, papeis_id) VALUES (2, 1);
INSERT INTO USUARIO_SISTEMA_PAPEL(USUARIO_SISTEMA_ID_USUARIO_SISTEMA, papeis_id) VALUES (2, 2);