package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Produto;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
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

	@Override
	public List<Produto> buscarComFiltro(Produto produto) throws CadastroInexistenteException {
		
		String codigo = produto.getCodigo();
		String descricao = produto.getDescricao();
		Boolean indPerecivel = produto.getIndPerecivel();
		String nomeFantasia = produto.getFornecedor().getNomeFantasia();
		
		List<Produto> produtos;
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if((codigo == null || codigo.isEmpty()) && (descricao == null || descricao.isEmpty()) && (indPerecivel == null) && (nomeFantasia == null || nomeFantasia.isEmpty())) {
			produtos = produtoRepository.getAll();
			
			if(produtos.isEmpty()) {
				throw new CadastroInexistenteException("Não existem produtos cadastrados no sistema");
			}
			
		} else {
			produtos = produtoRepository.buscarComFiltro(produto);
			
			if(produtos.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhum cadastro de produto para o filtro informado.");
			}
		}
		
		// Coloca a máscara de R$ no campo auxiliar utilizado pela view
		for(Produto p : produtos) {
			Double valorUnitario = p.getValorUnitario();
			Double precoVenda = p.getPrecoVenda();
			
			p.setValUnitAux("R$" + valorUnitario);
			p.setValVendaAux("R$" + precoVenda);
		}
		
		return produtos;
	}

	@Override
	public Produto excluir(Long id) {
		Produto produto = produtoRepository.findById(id);
		
		/*if(produto.get == false) {
			throw new InconsistenciaException("Apenas contratos que não tenham sido atribuídos a nenhum cliente podem ser excluídos.");
		}*/
		
		produtoRepository.remove(produto);
		
		return produto;
	}

	@Override
	public Produto findById(Long id) {
		return produtoRepository.findById(id);
	}

	@Override
	public void editar(Produto produto) {
		produtoRepository.merge(produto);
	}
	
}
