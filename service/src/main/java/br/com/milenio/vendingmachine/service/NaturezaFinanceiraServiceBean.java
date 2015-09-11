package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.NaturezaFinanceira;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.NaturezaFinanceiraRepository;

@Stateless
public class NaturezaFinanceiraServiceBean implements NaturezaFinanceiraService {
	
	@EJB
	NaturezaFinanceiraRepository naturezaFinanceiraRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(NaturezaFinanceiraServiceBean.class);
	@Override
	public void cadastrar(NaturezaFinanceira naturezaFinanceira)
			throws ConteudoJaExistenteNoBancoDeDadosException {

		// Verifica se j� existe uma natureza financeira com o mesmo C�DIGO e DESCRICAO cadastrado no banco de dados do sistema
		NaturezaFinanceira naturezaFinanceiraCadastrada = naturezaFinanceiraRepository.findNaturezaFinanceiraByCodigoDescricao(naturezaFinanceira);
		
		if(naturezaFinanceiraCadastrada != null){
			String codigo = naturezaFinanceiraCadastrada.getCodigo();
			String descricao = naturezaFinanceiraCadastrada.getDescricao();
			String msg = null;
			
			if(codigo.equalsIgnoreCase(naturezaFinanceira.getCodigo())){
				msg = "J� existe uma natureza financeira com o c�digo " + naturezaFinanceira.getCodigo() + " cadastrada no sistema.";
			} else if (descricao.equals(naturezaFinanceira.getDescricao())){
				msg = "J� existe uma natureza financeira com a descri��o " + naturezaFinanceira.getDescricao() + " cadastrada no sistema.";
			}
			
			LOGGER.info(msg);
			throw new ConteudoJaExistenteNoBancoDeDadosException(msg);
			
		}
	    
		naturezaFinanceiraRepository.persist(naturezaFinanceira);
	}

	@Override
	public List<NaturezaFinanceira> buscarNaturezasComFiltro(NaturezaFinanceira naturezaFinanceira) throws CadastroInexistenteException {
		List<NaturezaFinanceira> naturezasFinanceiras;
		
		// Se n�o houver filtros informados, far� a busca de todos os registros
		if(parametrosVazios(naturezaFinanceira)) {
			naturezasFinanceiras = naturezaFinanceiraRepository.getAll();
			
			if(naturezasFinanceiras.isEmpty()) {
				throw new CadastroInexistenteException("N�o existem naturezas financeiras cadastradas no sistema.");
			}
			
			return naturezasFinanceiras;
			
		} else {
			naturezasFinanceiras = naturezaFinanceiraRepository.buscarNaturezasComFiltro(naturezaFinanceira);
			
			if(naturezasFinanceiras.isEmpty()) {
				throw new CadastroInexistenteException("N�o existe nenhum cadastro de naturezas financeiras para o filtro informado.");
			}
			
			return naturezasFinanceiras;
		}
		
	}

	private boolean parametrosVazios(NaturezaFinanceira naturezaFinanceira) {
		String codigo = naturezaFinanceira.getCodigo();
		String descricao = naturezaFinanceira.getDescricao();
		String tipoNaturezaFinanceira = naturezaFinanceira.getTipoNaturezaFinanceira();
		
		if((codigo == null || codigo.isEmpty()) && (descricao == null || descricao.isEmpty()) &&
				(tipoNaturezaFinanceira == null || tipoNaturezaFinanceira.isEmpty())) {
			return true;
		}
		
		return false;
	}

	@Override
	public NaturezaFinanceira findById(Long idNaturezaFinanceira) {
		return naturezaFinanceiraRepository.findById(idNaturezaFinanceira);
	}

	@Override
	public void editar(NaturezaFinanceira naturezaFinanceira) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Carrega um objeto com os dados atuais da natureza financeira sendo editada
		NaturezaFinanceira naturezaAtual = naturezaFinanceiraRepository.findById(naturezaFinanceira.getId());
		
		NaturezaFinanceira resultado = null;
		// Se houve mudan�a na descri��o, � necess�rio validar se a nova descri��o j� n�o existe no sistema
		if(!naturezaAtual.getDescricao().equalsIgnoreCase(naturezaFinanceira.getDescricao())) {
			// Se entrou aqui, ent�o houve altera��o na descri��o da natureza financeira
			// Verifica se j� existe uma natureza financeira com a mesma descri��o cadastrado no banco de dados do sistema
			resultado = naturezaFinanceiraRepository.findByDescricao(naturezaFinanceira.getDescricao());
			
			if(resultado != null && resultado.getDescricao().equalsIgnoreCase(naturezaFinanceira.getDescricao())) {
				LOGGER.info("J� existe uma natureza financeira com descri��o " + naturezaFinanceira.getDescricao() + " cadastrada no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("J� existe uma natureza financeira com descri��o " + naturezaFinanceira.getDescricao() + " cadastrada no sistema.");
			}
		}
		
		naturezaFinanceiraRepository.merge(naturezaFinanceira);
	}

	@Override
	public NaturezaFinanceira excluirNaturezaFinanceira(Long idNatureza) throws InconsistenciaException{
		NaturezaFinanceira natureza = naturezaFinanceiraRepository.findById(idNatureza);
		naturezaFinanceiraRepository.remove(natureza);
		return natureza;
	}

}
