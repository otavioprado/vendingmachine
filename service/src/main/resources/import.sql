-- # You can use this file to load seed data into the database using SQL statements # --

--------------------------------------------
--  Cria os usu�rios default do sistema   --
--------------------------------------------
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (1, 'otavio', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'otavio@hotmail.com', 'Ot�vio Felipe do Prado', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (2, 'lucas', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'lucas@gmail.com', 'Lucas Eduardo Fran�a Rold�o', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (3, 'rafaella', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'rafaella@yahoo.com.br', 'Rafaella Negrello', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO, MOTIVO_BLOQUEIO) VALUES (4, 'bruna', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'bruna@gmail.com', 'Bruna Lecy', false, 'Afastamento');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (5, 'pedro', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'pedro@hotmail.com.com', 'Pedro da Silva', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (6, 'joao', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'joao@gmail.com', 'Jo�o Cunha J�nior', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (7, 'marcelo', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'marcelo@hotmail.com', 'Marcelo Silveira', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (8, 'silvia', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'silvia@uol.com.br', 'Silvia Castello', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (9, 'roberto', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'roberto@gmail.com', 'Roberto Jefferson Neto', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (10, 'jose', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'jose@gmail.com', 'Jos� Aparecido Portella', true);
-- Obs.: "a3d4a413a3f981422388a46cbf1292fb" � "gudiao" em MD5

--------------------------------------------
--  	Cria os perfils do sistema  	  --
--------------------------------------------
INSERT INTO PERFIL(ID, NOME) VALUES (1, 'ADMINISTRADOR');
INSERT INTO PERFIL(ID, NOME) VALUES (2, 'GESTOR');
INSERT INTO PERFIL(ID, NOME) VALUES (3, 'GERENTE');
INSERT INTO PERFIL(ID, NOME) VALUES (4, 'OPERADOR');

--------------------------------------------------------
--  	Vincula os usu�rios default a um perfil  	  --
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
--  	Insere todas as permiss�es cridadas at� o momento  	  --
----------------------------------------------------------------
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (1, 'ROLE_USUARIO_LOGADO_PAINEL_ADMIN', 'Login no painel administrativo', null, 1); 
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (2, 'ROLE_CAD_USUARIO', 'Cadastrar novos usu�rios', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (3, 'ROLE_CONS_USUARIO', 'Consultar usu�rios', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (4, 'ROLE_EDT_USUARIO', 'Editar usu�rios existentes', 3, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (5, 'ROLE_BLOQ_USUARIO', 'Bloquear usu�rios', 3, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (6, 'ROLE_CAD_ATIVIDADE', 'Agendar atividade', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (7, 'ROLE_CONS_AUDITORIA', 'Consultar auditoria', null, 0);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (8, 'ROLE_EDT_PERMISSOES', 'Editar permissoes', null, 0);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (9, 'ROLE_CONS_ATIVIDADE', 'Consultar atividade', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (10, 'ROLE_EDT_ATIVIDADE', 'Editar atividade', 9, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (11, 'ROLE_EXCLUIR_ATIVIDADE', 'Excluir atividade', 9, 1);


----------------------------------------------------------------
--  	Atribui as permissoes de cada perfil				  --
----------------------------------------------------------------
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 1); -- Admin: Usu�rio logado painel de Admin
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 2); -- Admin: Cadastrar novos usu�rios
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 3); -- Admin: Consultar usu�rios
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 4); -- Admin: Editar usu�rios existentes
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 5); -- Admin: Bloquear usu�rios
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 6); -- Admin: Agendar atividade
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 7); -- Admin: Consultar auditoria
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 8); -- Admin: Editar permiss�es
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 9); -- Admin: Consultar atividade
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 10); -- Admin: Editar atividade
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 11); -- Admin: Excluir atividade

INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 1); -- Gestor: Usu�rio logado painel de Admin
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 4); -- Gestor: Editar usu�rios existentes
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 5); -- Gestor: Bloquear usu�rios

INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (3, 1); -- Gerente: Usu�rio logado painel de Admin
-- INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (4, 1); -- Operador: Usu�rio logado painel de Admin

----------------------------------------------------------------
--  	Atribui as configura��es do sistema					  --
----------------------------------------------------------------
INSERT INTO CONFIGURACAO_SISTEMA(ID, NOME, DESCRICAO, VALOR) VALUES (1, 'QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA', 'Define a quantidade m�xima de vezes em que um usu�rio pode errar a senha ao tentar realizar login antes que a conta seja bloqueada', 3);









