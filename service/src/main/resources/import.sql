-- # You can use this file to load seed data into the database using SQL statements # --

--------------------------------------------
--  Cria os usu�rios default do sistema   --
--------------------------------------------
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (1, 'otavio', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (2, 'lucas', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');

INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (3, 'bruna', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (3, 'rafaela', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (4, 'pedro', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (5, 'joao', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (6, 'marcelo', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (7, 'silvia', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (8, 'roberto', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (9, 'jose', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Ot�vio Felipe do Prado');
-- Obs.: "a3d4a413a3f981422388a46cbf1292fb" � "gudiao" em MD5

--------------------------------------------
--  	Cria os perfils do sistema  	  --
--------------------------------------------
INSERT INTO PERFIL(ID, NOME) VALUES (1, 'ADMINISTRATOR');
INSERT INTO PERFIL(ID, NOME) VALUES (2, 'GESTOR');
INSERT INTO PERFIL(ID, NOME) VALUES (3, 'GERENTE');
INSERT INTO PERFIL(ID, NOME) VALUES (4, 'OPERADOR');

--------------------------------------------------------
--  	Vincula os usu�rios default a um perfil  	  --
--------------------------------------------------------
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 1);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 1);

INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 2);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 2);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 3);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 4);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 4);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 5);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 5);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 6);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 6);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 7);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 7);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 8);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 8);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 9);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 9);

----------------------------------------------------------------
--  	Insere todas as permiss�es cridadas at� o momento  	  --
----------------------------------------------------------------
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (1, 'ROLE_USUARIO_LOGADO', 'Usu�rio logado');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (2, 'ROLE_ADMIN_CAD_USUARIO', 'Cadastrar novos usu�rios no sistema');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (3, 'ROLE_ADMIN_EDT_USUARIO', 'Editar usu�rios existentes no sistema');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (4, 'ROLE_ADMIN_EXCL_USUARIO', 'Excluir usu�rios existentes no sistema');

----------------------------------------------------------------
--  	Atribui as permissoes de cada perfil				  --
----------------------------------------------------------------
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 1); -- Admin: Usu�rio logado
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 2); -- Admin: Cadastrar novos usu�rios no sistema
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 3); -- Admin: Editar usu�rios existentes no sistema
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 4); -- Admin: Excluir usu�rios existentes no sistema









