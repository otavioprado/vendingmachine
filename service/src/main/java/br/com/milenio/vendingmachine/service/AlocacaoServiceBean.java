package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Contrato;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.AlocacaoRepository;
import br.com.milenio.vendingmachine.repository.ClienteRepository;
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
	ClienteRepository clienteRepository;
	
	@EJB
	MaquinaStatusRepository maquinaStatusRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioServiceBean.class);
	
	@Override
	public void cadastrar(Alocacao alocacao) throws InconsistenciaException {
		LOGGER.info("Tentato cadastrar a alocação da máquina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		Maquina maquina = null;
		Contrato contrato = null;
		Cliente cliente = null;
		
		// Validação da máquina
		String codigoMaquina = alocacao.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O código da máquina não é válido");
		} else {
			maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O código da máquina não é válido");
			}
			
			if(!"EM ESTOQUE".equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
				throw new InconsistenciaException("Apenas máquinas que estejam em estoque podem ter uma solicitação de alocação cadastrada.");
			}
		}
		
		// Validação do contrato
		String codigoContrato = alocacao.getContrato().getCodigo();
		if(codigoContrato == null || codigoContrato.isEmpty()) {
			throw new InconsistenciaException("O código do contrato não é válido");
		} else {
			contrato = contratoRepository.findByCodigo(codigoContrato);
			
			if(contrato == null) {
				throw new InconsistenciaException("O código do contrato não é válido");
			}
			
			if(!contrato.getIndDisponivel()) {
				throw new InconsistenciaException("Apenas contratos que ainda não tenham sido usados podem ser utilizados para uma solicitação de alocação.");
			}
		}
		
		// Validação do cliente
		String codigoCliente = alocacao.getCliente().getCodigo();
		if(codigoCliente == null || codigoCliente.isEmpty()) {
			throw new InconsistenciaException("O código do cliente não é válido");
		} else {
			cliente = clienteRepository.findByCodigo(codigoCliente);
			
			if(cliente == null) {
				throw new InconsistenciaException("O código do cliente não é válido");
			}
			
			if(!cliente.getIndAtivo()) {
				throw new InconsistenciaException("Apenas clientes ativos podem ser utilizados para uma solicitação de alocação.");
			}
		}
		
		// Coloca a máquina para pendente de alocação
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao("PENDENTE DE ALOCAÇÃO");
		maquina.setMaquinaStatus(ms);
		maquinaRepository.persist(maquina);
		
		// Coloca o contrato como indisponível
		contrato.setIndDisponivel(false);
		contratoRepository.persist(contrato);
		
		// Salva a alocação
		alocacao.setDataCadastroAlocacao(new Date());
		alocacaoRepository.persist(alocacao);
		
		LOGGER.info("Alocação da máquina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com SUCESSO.");
		LOGGER.info("A máquina " + alocacao.getMaquina().getCodigo() + " agora está com status " + maquina.getMaquinaStatus().getDescricao());
	}

	@Override
	public List<Alocacao> findAlocacoesAtivasByCliente(Long clienteId) {
		List<Alocacao> alocacoes = alocacaoRepository.findAlocacoesAtivasByCliente(clienteId);
		
		return alocacoes;
	}

	@Override
	public Alocacao findById(Long id) {
		return alocacaoRepository.findById(id);	
	}

	@Override
	public void desalocar(Alocacao alocacao) throws InconsistenciaException {
		Maquina maquina = alocacao.getMaquina();
		
		if(!"ALOCADA PARA CLIENTE".equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
			throw new InconsistenciaException("Apenas máquinas alocadas em clientes podem ter uma solicitação de desalocação cadastrada.");
		}
		
		// Coloca a máquina para pendente de desalocação
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao("PENDENTE DE DESALOCAÇÃO");
		maquina.setMaquinaStatus(ms);
		maquinaRepository.persist(maquina);
	}

	@Override
	public Alocacao excluir(Long id) throws InconsistenciaException {
		Alocacao alocacao = alocacaoRepository.findById(id);
		
		if(alocacao.getDataAlocacao() != null) {
			throw new InconsistenciaException("Apenas solicitações de alocação que ainda não tenham sido alocadas ao cliente podem ser excluídas.");
		}
		
		// Volta a máquina para em estoque
		Maquina maquina = alocacao.getMaquina();
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao("EM ESTOQUE");
		maquina.setMaquinaStatus(ms);
		maquinaRepository.persist(maquina);
		
		alocacaoRepository.remove(alocacao);
		
		return alocacao;
	}
}
