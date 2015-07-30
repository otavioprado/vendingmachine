-- # You can use this file to load seed data into the database using SQL statements # --

--------------------------------------------
--  Cria os usuários default do sistema   --
--------------------------------------------
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (1, 'otavio', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'otavio@hotmail.com.com', 'Otávio Felipe do Prado', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (2, 'lucas', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'lucas@gmail.com', 'Lucas Eduardo França Roldão', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (3, 'rafaella', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'rafaella@yahoo.com.br', 'Rafaella Negrello', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO, MOTIVO_BLOQUEIO) VALUES (4, 'bruna', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'bruna@gmail.com', 'Bruna Lecy', false, 'Afastamento');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (5, 'pedro', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'pedro@hotmail.com.com', 'Pedro da Silva', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (6, 'joao', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'joao@gmail.com', 'João Cunha Júnior', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (7, 'marcelo', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'marcelo@hotmail.com', 'Marcelo Silveira', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (8, 'silvia', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'silvia@uol.com.br', 'Silvia Castello', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (9, 'roberto', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'roberto@gmail.com', 'Roberto Jefferson Neto', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (10, 'jose', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'jose@gmail.com', 'José Aparecido Portella', true);
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
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (2, 2);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (3, 3);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (4, 4);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (5, 4);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (6, 2);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (7, 3);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (8, 4);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (9, 1);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (10, 2);


----------------------------------------------------------------
--  	Insere todas as permissões cridadas até o momento  	  --
----------------------------------------------------------------
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (1, 'ROLE_USUARIO_LOGADO_PAINEL_ADMIN', 'Usuário logado no painel administrativo');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (2, 'ROLE_ADMIN_CAD_USUARIO', 'Cadastrar novos usuários no sistema');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (3, 'ROLE_ADMIN_EDT_USUARIO', 'Editar usuários existentes no sistema');
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO) VALUES (4, 'ROLE_ADMIN_EXCL_USUARIO', 'Excluir usuários existentes no sistema');

----------------------------------------------------------------
--  	Atribui as permissoes de cada perfil				  --
----------------------------------------------------------------
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 1); -- Admin: Usuário logado painel de Admin
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 2); -- Admin: Cadastrar novos usuários no sistema
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 3); -- Admin: Editar usuários existentes no sistema
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 4); -- Admin: Excluir usuários existentes no sistema

INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 1); -- Gestor: Usuário logado painel de Admin
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (3, 1); -- Gerente: Usuário logado painel de Admin
-- INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (4, 1); -- Operador: Usuário logado painel de Admin

----------------------------------------------------------------
--  	Atribui as configurações do sistema					  --
----------------------------------------------------------------
INSERT INTO CONFIGURACAO_SISTEMA(ID, NOME, DESCRICAO, VALOR) VALUES (1, 'QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA', 'Define a quantidade máxima de vezes em que um usuário pode errar a senha ao tentar realizar login antes que a conta seja bloqueada', 3);









