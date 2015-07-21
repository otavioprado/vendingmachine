-- # You can use this file to load seed data into the database using SQL statements # --

--------------------------------------------
--  Cria os usuários default do sistema   --
--------------------------------------------
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME) VALUES (1, 'otavio', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'fulano@gmail.com', 'Otávio Felipe do Prado');
-- Obs.: "a3d4a413a3f981422388a46cbf1292fb" é "gudiao" em MD5

--------------------------------------------
--  	Cria os perfils do sistema  	  --
--------------------------------------------
INSERT INTO PERFIL(ID, NOME) VALUES (1, 'ADMINISTRATOR');
INSERT INTO PERFIL(ID, NOME) VALUES (2, 'GESTOR');
INSERT INTO PERFIL(ID, NOME) VALUES (3, 'GERENTE');
INSERT INTO PERFIL(ID, NOME) VALUES (4, 'OPERADOR');

--------------------------------------------------------
--  	Vincula os usuários default a um perfil  	  --
--------------------------------------------------------
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (1, 1);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 1);

----------------------------------------------------------------
--  	Insere todas as permissões cridadas até o momento  	  --
----------------------------------------------------------------
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (1, 'ROLE_USUARIO_LOGADO', 'Usuário logado');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (2, 'ROLE_ADMIN_CAD_USUARIO', 'Cadastrar novos usuários no sistema');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (3, 'ROLE_ADMIN_EDT_USUARIO', 'Editar usuários existentes no sistema');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (4, 'ROLE_ADMIN_EXCL_USUARIO', 'Excluir usuários existentes no sistema');

----------------------------------------------------------------
--  	Atribui as permissoes de cada perfil				  --
----------------------------------------------------------------
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 1); -- Admin: Usuário logado
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 2); -- Admin: Cadastrar novos usuários no sistema
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 3); -- Admin: Editar usuários existentes no sistema
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 4); -- Admin: Excluir usuários existentes no sistema









