package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Contrato;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.ContratoRepository;

@Stateless
public class ContratoServiceBean implements ContratoService {
	
	@EJB
	ContratoRepository contratoRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(ContratoServiceBean.class);

	@Override
	public void cadastrar(Contrato novoContrato) throws ConteudoJaExistenteNoBancoDeDadosException {

		// Verifica se j� existe um contrato com o mesmo c�digo no banco de dados do sistema
		Contrato contrato = contratoRepository.findByCodigo(novoContrato.getCodigo());
		
		if(contrato != null && contrato.getCodigo().equalsIgnoreCase(novoContrato.getCodigo())) {
			LOGGER.info("J� existe um contrato com o c�digo " + contrato.getCodigo() + " cadastrado no banco de dados.");
			throw new ConteudoJaExistenteNoBancoDeDadosException("J� existe um contrato com o c�digo " + contrato.getCodigo() + " cadastrado no sistema.");
		}
		
		try{
			contratoRepository.persist(novoContrato);
		} catch(Exception e) {
			// Erro desconhecido ao tentar realizar a persist�ncia dos dados no banco de dados
			LOGGER.error("Erro ao tentar gravar o contrato " + novoContrato.getDescricao() + " no banco de dados");
		}
		
		LOGGER.info("Contrato " + novoContrato.getCodigo() + " salvo com sucesso no banco de dados.");
	}

	@Override
	public List<Contrato> buscarContratosComFiltro(Contrato contratoConsParam) throws CadastroInexistenteException {
		
		List<Contrato> listContratos;
		String codigo = contratoConsParam.getCodigo();
		String modalidade = contratoConsParam.getModalidade();
		Boolean indDisponivel = contratoConsParam.getIndDisponivel();
		
		// Se n�o houver filtros informados, far� a busca de todos os registros
		if(((codigo == null || codigo.isEmpty()) && indDisponivel == null && (modalidade == null || modalidade.isEmpty()))) {
			listContratos = contratoRepository.getAll();
			
			if(listContratos.isEmpty()) {
				throw new CadastroInexistenteException("N�o existem contratos cadastrados no sistema");
			}

			return listContratos;
		} else {
			listContratos = contratoRepository.buscarUsuariosComFiltro(codigo, modalidade, indDisponivel);
			
			if(listContratos.isEmpty()) {
				throw new CadastroInexistenteException("N�o existe nenhum cadastro de contrato para o filtro informado.");
			}
			
			return listContratos;
		}
	}

	@Override
	public Contrato excluir(Long id) throws InconsistenciaException {

		Contrato contrato = contratoRepository.findById(id);
		
		if(contrato.getIndDisponivel() == false) {
			throw new InconsistenciaException("Apenas contratos que n�o tenham sido atribu�dos a nenhum cliente podem ser exclu�dos.");
		}
		
		contratoRepository.remove(contrato);
		
		return contrato;
	}

	@Override
	public void editar(Contrato contrato) throws InconsistenciaException {
		if(contrato.getIndDisponivel() == false) {
			throw new InconsistenciaException("Apenas contratos que n�o tenham sido atribu�dos a nenhum cliente podem ser editados.");
		}
		
		contratoRepository.merge(contrato);
	}

	@Override
	public Contrato findById(Long id) {
		return contratoRepository.findById(id);
	}
	
}
