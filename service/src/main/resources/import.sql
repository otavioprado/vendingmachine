-- # You can use this file to load seed data into the database using SQL statements # --

--------------------------------------------
--  Cria os usu�rios default do sistema   --
--------------------------------------------
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (1, 'otavio', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'otavio@hotmail.com', 'Ot�vio Felipe do Prado', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (2, 'lucas', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'lucas@gmail.com', 'Lucas Eduardo Fran�a Rold�o', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (3, 'rafaella', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'rafaella@yahoo.com.br', 'Rafaella Negrello', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO, MOTIVO_BLOQUEIO) VALUES (4, 'bloqueado', 'a3d4a413a3f981422388a46cbf1292fb', NOW(), 'bloqueado@gmail.com', 'Bloqueado', false, 'Afastamento');
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (5, 'administrador', '91f5167c34c400758115c2a6826ec2e3', NOW(), 'administrador@hotmail.com.com', 'Pedro da Silva', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (6, 'gestor', 'a55607442fca264cf37e935503d646c2', NOW(), 'gestor@gmail.com', 'Jo�o Cunha J�nior', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (7, 'gerente', '740d9c49b11f3ada7b9112614a54be41', NOW(), 'gerente@hotmail.com', 'Marcelo Silveira', true);
INSERT INTO USUARIO_SISTEMA(ID, LOGIN, SENHA_APLICACAO, DATA_CADASTRO, EMAIL, NOME, IND_ATIVO) VALUES (8, 'operador', '6d4f07c943a4da1c8bfe591abbc3579', NOW(), 'operador@uol.com.br', 'Silvia Castello', true);
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
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (5, 1);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (6, 2);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (7, 3);
INSERT INTO USUARIO_SISTEMA_PERFIL(USUARIO_SISTEMA_ID, PERFIL_ID) VALUES (8, 4);


----------------------------------------------------------------
--  	Insere todas as permiss�es cridadas at� o momento  	  --
----------------------------------------------------------------
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (1, 'ROLE_USUARIO_LOGADO_PAINEL_ADMIN', 'Login no painel administrativo', null, 0); 
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (2, 'ROLE_CAD_USUARIO', 'Cadastrar novos usu�rios', null, 0);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (3, 'ROLE_CONS_USUARIO', 'Consultar usu�rios', null, 0);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (4, 'ROLE_EDT_USUARIO', 'Editar usu�rios existentes', 3, 0);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (5, 'ROLE_BLOQ_USUARIO', 'Bloquear usu�rios', 3, 0);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (6, 'ROLE_CAD_ATIVIDADE', 'Agendar atividade', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (7, 'ROLE_CONS_AUDITORIA', 'Consultar auditoria', null, 0);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (8, 'ROLE_EDT_PERMISSOES', 'Editar permissoes', null, 0);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (9, 'ROLE_CONS_ATIVIDADE', 'Consultar atividade', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (10, 'ROLE_EDT_ATIVIDADE', 'Editar atividade', 9, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (11, 'ROLE_EXCLUIR_ATIVIDADE', 'Excluir atividade', 9, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (12, 'ROLE_CAD_CLIENTE', 'Cadastrar cliente', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (13, 'ROLE_CONS_CLIENTE', 'Consultar cliente', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (14, 'ROLE_EDT_CLIENTE', 'Editar cliente', 13, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (15, 'ROLE_BLOQ_CLIENTE', 'Bloquear cliente', 13, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (16, 'ROLE_CAD_CONTRATO', 'Cadastrar contrato', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (17, 'ROLE_CONS_CONTRATO', 'Consultar contrato', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (18, 'ROLE_EXCLUIR_CONTRATO', 'Excluir contrato', 17, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (19, 'ROLE_EDT_CONTRATO', 'Editar contrato', 17, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (20, 'ROLE_CAD_FORNECEDOR', 'Cadastrar fornecedor', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (21, 'ROLE_CONS_FORNECEDOR', 'Consultar fornecedor', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (22, 'ROLE_EXCLUIR_FORNECEDOR', 'Excluir fornecedor', 21, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (23, 'ROLE_EDT_FORNECEDOR', 'Editar fornecedor', 21, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (24, 'ROLE_CAD_PRODUTO', 'Cadastrar produto', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (25, 'ROLE_CONS_PRODUTO', 'Consultar produto', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (26, 'ROLE_EXCLUIR_PRODUTO', 'Excluir produto', 25, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (27, 'ROLE_EDT_PRODUTO', 'Editar produto', 25, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (28, 'ROLE_CAD_MAQUINA', 'Cadastrar m�quina', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (29, 'ROLE_CONS_MAQUINA', 'Consultar m�quina', null, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (30, 'ROLE_EDT_MAQUINA', 'Editar m�quina', 29, 1);
INSERT INTO PERMISSAO(ID, NOME, DESCRICAO, DEPENDENCIA_ID, IND_ATRIB_ANY_PERFIL) VALUES (31, 'ROLE_INATIVAR_MAQUINA', 'Inativar m�quina', 29, 1);

----------------------------------------------------------------
--  	Atribui as permissoes de cada perfil				  --
----------------------------------------------------------------
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 1); -- Admin: Usu�rio logado painel de Admin
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 2); -- Admin: Cadastrar novos usu�rios
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 3); -- Admin: Consultar usu�rios
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 4); -- Admin: Editar usu�rios existentes
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 5); -- Admin: Bloquear usu�rios
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 7); -- Admin: Consultar auditoria
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 8); -- Admin: Editar permiss�es
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 10); -- Admin: Editar atividade
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (1, 11); -- Admin: Excluir atividade



INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 1); -- Gestor: Usu�rio logado painel de Admin
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 4); -- Gestor: Editar usu�rios existentes
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 5); -- Gestor: Bloquear usu�rios
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 6); -- Gestor: Agendar atividade
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 9); -- Gestor: Consultar atividade
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 10); -- Gestor: Editar atividade
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 11); -- Gestor: Excluir atividade
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 12); -- Gestor: Cadastrar cliente
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 13); -- Gestor: Consultar cliente
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 14); -- Gestor: Editar cliente
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 15); -- Gestor: Bloquear cliente
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 16); -- Gestor: Cadastrar contrato
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 17); -- Gestor: Consultar contrato
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 18); -- Gestor: Excluir contrato
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 19); -- Gestor: Editar contrato
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 20); -- Gestor: Cadastrar fornecedor
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 21); -- Gestor: Consultar fornecedor
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 22); -- Gestor: Excluir fornecedor
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 23); -- Gestor: Editar fornecedor
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 24); -- Gestor: Cadastrar produto
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 25); -- Gestor: Consultar produto
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 26); -- Gestor: Excluir produto
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 27); -- Gestor: Editar produto
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 28); -- Gestor: Cadastrar m�quina
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 29); -- Gestor: Consultar m�quina
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 30); -- Gestor: Editar m�quina
INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (2, 31); -- Gestor: Inativar m�quina


INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (3, 1); -- Gerente: Usu�rio logado painel de Admin
-- INSERT INTO PERFIL_PERMISSAO(PERFIL_ID, PERMISSAO_ID) VALUES (4, 1); -- Operador: Usu�rio logado painel de Admin

----------------------------------------------------------------
--  	Atribui as configura��es do sistema					  --
----------------------------------------------------------------
INSERT INTO CONFIGURACAO_SISTEMA(ID, NOME, DESCRICAO, VALOR) VALUES (1, 'QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA', 'Define a quantidade m�xima de vezes em que um usu�rio pode errar a senha ao tentar realizar login antes que a conta seja bloqueada', 3);



----------------------------------------------------------------
--  		Cadastro de clientes do sistema					  --
----------------------------------------------------------------
INSERT INTO ENDERECO(BAIRRO, CEP, CIDADE, COMPLEMENTO, ESTADO, LOGRADOURO, NUMERO) VALUES ('Vila Industrial', '13035-500', 'Campinas', 'Apartamento 57', 'SP', 'R. Dr. Sales de Oliveira', 1661);
INSERT INTO CLIENTE(CELULAR, CLIENTE_DESDE, CODIGO, CPF_CNPJ, EMAIL, FISICA_JURIDICA, IND_ATIVO, MOTIVO_BLOQUEIO, NOME_CONTATO, NOME_FANTASIA, SITE, TELEFONE_FIXO, ENDERECO_ID) VALUES('(19) 9784-4321', '2015-08-24', 'CODIGO007', '354.345.346-54', 'jose@gmail.com', 'PF', 1, null, 'Jos� da Silva', 'Loja do Jos�', 'www.lojadojose.com.br', '(19) 3271-8423', 1);

INSERT INTO ENDERECO(BAIRRO, CEP, CIDADE, COMPLEMENTO, ESTADO, LOGRADOURO, NUMERO) VALUES ('Jardim Campos El�seos', '20511-330', 'S�o Paulo', null, 'SP', 'Rua Dr. Abelardo de Barros', 372);
INSERT INTO CLIENTE(CELULAR, CLIENTE_DESDE, CODIGO, CPF_CNPJ, EMAIL, FISICA_JURIDICA, IND_ATIVO, MOTIVO_BLOQUEIO, NOME_CONTATO, NOME_FANTASIA, SITE, TELEFONE_FIXO, ENDERECO_ID) VALUES('(19) 9942-6533', '2014-06-29', 'CLI0001', '36.216.296/0001-20', 'fulano@hotmail.com', 'PJ', 0, 'Vencimento de contrato', 'Jos� da Silva', 'Fulano da Silva', 'www.fulano.com.br', '(19) 3345-7542', 2);

INSERT INTO ENDERECO(BAIRRO, CEP, CIDADE, COMPLEMENTO, ESTADO, LOGRADOURO, NUMERO) VALUES ('Vila Industrial', '13035-500', 'Campinas', 'Apartamento 57', 'SP', 'R. Dr. Sales de Oliveira', 1661);
INSERT INTO FORNECEDOR(CELULAR, CODIGO, CPF_CNPJ, EMAIL, FISICA_JURIDICA, NOME_CONTATO, NOME_FANTASIA, SITE, TELEFONE_FIXO, ENDERECO_ID) VALUES('(19) 9784-4321', 'CODIGO007', '354.345.346-54', 'jose@gmail.com', 'PF', 'Jos� da Silva', 'Loja do Jos�', 'www.lojadojose.com.br', '(19) 3271-8423', 1);

INSERT INTO ENDERECO(BAIRRO, CEP, CIDADE, COMPLEMENTO, ESTADO, LOGRADOURO, NUMERO) VALUES ('Jardim Campos El�seos', '20511-330', 'S�o Paulo', null, 'SP', 'Rua Dr. Abelardo de Barros', 372);
INSERT INTO FORNECEDOR(CELULAR, CODIGO, CPF_CNPJ, EMAIL, FISICA_JURIDICA, NOME_CONTATO, NOME_FANTASIA, SITE, TELEFONE_FIXO, ENDERECO_ID) VALUES('(19) 9942-6533', 'FORN0001', '36.216.296/0001-20', 'fulano@hotmail.com', 'PJ', 'Jos� da Silva', 'Fulano da Silva', 'www.fulano.com.br', '(19) 3345-7542', 2);

----------------------------------------------------------------
--  		Cadastro de produtos do sistema					  --
----------------------------------------------------------------
INSERT INTO PRODUTO(CODIGO, DESCRICAO, IND_PERECIVEL, PRECO_VENDA, VALOR_UNITARIO, FORNECEDOR_ID) VALUES ('P123','Coca-cola', true, 5, 4, 1);
INSERT INTO PRODUTO(CODIGO, DESCRICAO, IND_PERECIVEL, PRECO_VENDA, VALOR_UNITARIO, FORNECEDOR_ID) VALUES ('P456','Pepsi', true, 4, 3.5, 1);
INSERT INTO PRODUTO(CODIGO, DESCRICAO, IND_PERECIVEL, PRECO_VENDA, VALOR_UNITARIO, FORNECEDOR_ID) VALUES ('P678','Guaran�', true, 4, 3.0, 1);

----------------------------------------------------------------
--  		Cadastro de status de m�quina					  --
----------------------------------------------------------------
INSERT INTO MAQUINA_STATUS(DESCRICAO) VALUES ('EM ESTOQUE');
INSERT INTO MAQUINA_STATUS(DESCRICAO) VALUES ('RESERVADA');
INSERT INTO MAQUINA_STATUS(DESCRICAO) VALUES ('EM MANUTEN��O');
INSERT INTO MAQUINA_STATUS(DESCRICAO) VALUES ('INATIVADA');
INSERT INTO MAQUINA_STATUS(DESCRICAO) VALUES ('PENDENTE DE ALOCA��O');
INSERT INTO MAQUINA_STATUS(DESCRICAO) VALUES ('ALOCADA PARA CLIENTE');
INSERT INTO MAQUINA_STATUS(DESCRICAO) VALUES ('PENDENTE DE DESALOCA��O');





