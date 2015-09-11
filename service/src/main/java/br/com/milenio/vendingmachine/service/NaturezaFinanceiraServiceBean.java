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

		// Verifica se já existe uma natureza financeira com o mesmo CÓDIGO e DESCRICAO cadastrado no banco de dados do sistema
		NaturezaFinanceira naturezaFinanceiraCadastrada = naturezaFinanceiraRepository.findNaturezaFinanceiraByCodigoDescricao(naturezaFinanceira);
		
		if(naturezaFinanceiraCadastrada != null){
			String codigo = naturezaFinanceiraCadastrada.getCodigo();
			String descricao = naturezaFinanceiraCadastrada.getDescricao();
			String msg = null;
			
			if(codigo.equalsIgnoreCase(naturezaFinanceira.getCodigo())){
				msg = "Já existe uma natureza financeira com o código " + naturezaFinanceira.getCodigo() + " cadastrada no sistema.";
			} else if (descricao.equals(naturezaFinanceira.getDescricao())){
				msg = "Já existe uma natureza financeira com a descrição " + naturezaFinanceira.getDescricao() + " cadastrada no sistema.";
			}
			
			LOGGER.info(msg);
			throw new ConteudoJaExistenteNoBancoDeDadosException(msg);
			
		}
	    
		naturezaFinanceiraRepository.persist(naturezaFinanceira);
	}

	@Override
	public List<NaturezaFinanceira> buscarNaturezasComFiltro(NaturezaFinanceira naturezaFinanceira) throws CadastroInexistenteException {
		List<NaturezaFinanceira> naturezasFinanceiras;
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if(parametrosVazios(naturezaFinanceira)) {
			naturezasFinanceiras = naturezaFinanceiraRepository.getAll();
			
			if(naturezasFinanceiras.isEmpty()) {
				throw new CadastroInexistenteException("Não existem naturezas financeiras cadastradas no sistema.");
			}
			
			return naturezasFinanceiras;
			
		} else {
			naturezasFinanceiras = naturezaFinanceiraRepository.buscarNaturezasComFiltro(naturezaFinanceira);
			
			if(naturezasFinanceiras.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhum cadastro de naturezas financeiras para o filtro informado.");
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
		// Se houve mudança na descrição, é necessário validar se a nova descrição já não existe no sistema
		if(!naturezaAtual.getDescricao().equalsIgnoreCase(naturezaFinanceira.getDescricao())) {
			// Se entrou aqui, então houve alteração na descrição da natureza financeira
			// Verifica se já existe uma natureza financeira com a mesma descrição cadastrado no banco de dados do sistema
			resultado = naturezaFinanceiraRepository.findByDescricao(naturezaFinanceira.getDescricao());
			
			if(resultado != null && resultado.getDescricao().equalsIgnoreCase(naturezaFinanceira.getDescricao())) {
				LOGGER.info("Já existe uma natureza financeira com descrição " + naturezaFinanceira.getDescricao() + " cadastrada no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("Já existe uma natureza financeira com descrição " + naturezaFinanceira.getDescricao() + " cadastrada no sistema.");
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
