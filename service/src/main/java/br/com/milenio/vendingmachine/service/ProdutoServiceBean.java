package br.com.milenio.vendingmachine.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Produto;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.repository.FornecedorRepository;
import br.com.milenio.vendingmachine.repository.ProdutoRepository;

@Stateless
public class ProdutoServiceBean implements ProdutoService {

	@EJB
	ProdutoRepository produtoRepository;
	
	@EJB
	FornecedorRepository fornecedorRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(ProdutoServiceBean.class);
	
	@Override
	public void cadastrar(Produto novoProduto) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Verifica se já existe um produto com o mesmo código cadastrado no banco de dados do sistema
		Produto produto = produtoRepository.findByCodigo(novoProduto.getCodigo());
		
		if(produto != null) {
			String codigo = produto.getCodigo();
			
			if(codigo.equalsIgnoreCase(novoProduto.getCodigo())) {
				LOGGER.info("Já existe um produto com o código " + novoProduto.getCodigo() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("Já existe um produto com o código " + novoProduto.getCodigo() + " cadastrado no sistema.");
			}
		}
		
		produtoRepository.persist(novoProduto);
		
		LOGGER.info("Cadastro do produto " + novoProduto.getCodigo() + " salvo com sucesso no banco de dados.");
	}
}
