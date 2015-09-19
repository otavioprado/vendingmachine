package br.com.milenio.vendingmachine.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.domain.model.Contrato;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.AlocacaoRepository;
import br.com.milenio.vendingmachine.repository.ContratoRepository;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;

@Stateless
public class AlocacaoServiceBean implements AlocacaoService {
	
	@EJB
	AlocacaoRepository alocacaoRepository;
	
	@EJB
	MaquinaRepository maquinaRepository;
	
	@EJB
	ContratoRepository contratoRepository;
	
	@EJB
	MaquinaStatusRepository maquinaStatusRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioServiceBean.class);
	
	@Override
	public void cadastrar(Alocacao alocacao) throws InconsistenciaException {
		LOGGER.info("Tentato cadastrar a aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		Maquina maquina = null;
		Contrato contrato = null;
		
		// Valida��o da m�quina
		String codigoMaquina = alocacao.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O c�digo da m�quina n�o � inv�lido");
		} else {
			maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O c�digo da m�quina n�o � inv�lido");
			}
			
			if(!"EM ESTOQUE".equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
				throw new InconsistenciaException("Apenas m�quinas que estejam em estoque podem ter uma solicita��o de aloca��o cadastrada.");
			}
		}
		
		// Valida��o do contrato
		String codigoContrato = alocacao.getContrato().getCodigo();
		if(codigoContrato == null || codigoContrato.isEmpty()) {
			throw new InconsistenciaException("O c�digo do contrato n�o � inv�lido");
		} else {
			contrato = contratoRepository.findByCodigo(codigoContrato);
			
			if(contrato == null) {
				throw new InconsistenciaException("O c�digo do contrato n�o � inv�lido");
			}
			
			if(!contrato.getIndDisponivel()) {
				throw new InconsistenciaException("Apenas contratos que ainda n�o tenham sido usados podem ser utilizados para uma solicita��o de aloca��o.");
			}
		}
		
		// Coloca a m�quina para pendente de aloca��o
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao("PENDENTE DE ALOCA��O");
		maquina.setMaquinaStatus(ms);
		maquinaRepository.persist(maquina);
		
		// Coloca o contrato como indispon�vel
		contrato.setIndDisponivel(false);
		contratoRepository.persist(contrato);
		
		// Salva a aloca��o
		alocacaoRepository.persist(alocacao);
		
		LOGGER.info("Aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com SUCESSO.");
		LOGGER.info("A m�quina " + alocacao.getMaquina().getCodigo() + " agora est� com status " + maquina.getMaquinaStatus().getDescricao());
	}
}
