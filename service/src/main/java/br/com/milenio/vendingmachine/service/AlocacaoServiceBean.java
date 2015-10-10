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
import br.com.milenio.vendingmachine.domain.model.Reserva;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.AlocacaoRepository;
import br.com.milenio.vendingmachine.repository.ClienteRepository;
import br.com.milenio.vendingmachine.repository.ContratoRepository;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;
import br.com.milenio.vendingmachine.repository.ReservaRepository;
import br.com.milenio.vendingmachine.util.Constants;

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
	ReservaRepository reservaRepository;
	
	@EJB
	MaquinaStatusRepository maquinaStatusRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioServiceBean.class);
	
	@Override
	public void cadastrar(Alocacao alocacao) throws InconsistenciaException {
		LOGGER.info("Tentato cadastrar a aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		Maquina maquina = null;
		Contrato contrato = null;
		Cliente cliente = null;
		
		// Valida��o do contrato
		String codigoContrato = alocacao.getContrato().getCodigo();
		if(codigoContrato == null || codigoContrato.isEmpty()) {
			throw new InconsistenciaException("O c�digo do contrato n�o � v�lido");
		} else {
			contrato = contratoRepository.findByCodigo(codigoContrato);
			
			if(contrato == null) {
				throw new InconsistenciaException("O c�digo do contrato n�o � v�lido");
			}
			
			if(!contrato.getIndDisponivel()) {
				throw new InconsistenciaException("Apenas contratos que ainda n�o tenham sido usados podem ser utilizados para uma solicita��o de aloca��o.");
			}
		}
		
		// Valida��o do cliente
		String codigoCliente = alocacao.getCliente().getCodigo();
		if(codigoCliente == null || codigoCliente.isEmpty()) {
			throw new InconsistenciaException("O c�digo do cliente n�o � v�lido");
		} else {
			cliente = clienteRepository.findByCodigo(codigoCliente);
			
			if(cliente == null) {
				throw new InconsistenciaException("O c�digo do cliente n�o � v�lido");
			}
			
			if(!cliente.getIndAtivo()) {
				throw new InconsistenciaException("Apenas clientes ativos podem ser utilizados para uma solicita��o de aloca��o.");
			}
		}
		
		// Valida��o da m�quina
		String codigoMaquina = alocacao.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O c�digo da m�quina n�o � v�lido");
		} else {
			maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O c�digo da m�quina n�o � v�lido");
			}
			
			String descricao = maquina.getMaquinaStatus().getDescricao();
			
			if(!Constants.EM_ESTOQUE.equalsIgnoreCase(descricao) && !Constants.RESERVADA.equalsIgnoreCase(descricao)) {
				throw new InconsistenciaException("Apenas m�quinas que estejam em estoque ou reservadas podem ter uma solicita��o de aloca��o cadastrada.");
			}
			
			// Se a m�quina selecionada tiver uma reserva cadastrada, ent�o tem que ser para o cliente especifico
			if(Constants.RESERVADA.equalsIgnoreCase(descricao)) {
				Reserva resultado = reservaRepository.findByMaquina(maquina);
				
				if(resultado != null) {
					if(!alocacao.getCliente().getCodigo().equalsIgnoreCase(resultado.getCliente().getCodigo())) {
						throw new InconsistenciaException("A m�quina " + maquina.getCodigo() + " est� reservada para o cliente " + resultado.getCliente().getNomeFantasia() + 
								" e por isso n�o pode ser alocada para outros clientes.");
					}
					
					// Se o cliente informado � o mesmo da reserva, ent�o pode deletar a reserva
					reservaRepository.remove(resultado);
				}
			}
		}
		
		// Coloca a m�quina para pendente de aloca��o
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao(Constants.PENDENTE_DE_ALOCACAO);
		maquina.setMaquinaStatus(ms);
		maquinaRepository.persist(maquina);
		
		// Coloca o contrato como indispon�vel
		contrato.setIndDisponivel(false);
		contratoRepository.persist(contrato);
		
		// Salva a aloca��o
		alocacao.setDataCadastroAlocacao(new Date());
		alocacaoRepository.persist(alocacao);
		
		LOGGER.info("Aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com SUCESSO.");
		LOGGER.info("A m�quina " + alocacao.getMaquina().getCodigo() + " agora est� com status " + maquina.getMaquinaStatus().getDescricao());
	}

	@Override
	public List<Alocacao> findAlocacoesByCliente(Long clienteId) {
		List<Alocacao> alocacoes = alocacaoRepository.findAlocacoesByCliente(clienteId);
		
		return alocacoes;
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
	public void solicitarDesalocacao(Alocacao alocacao) throws InconsistenciaException {
		Maquina maquina = maquinaRepository.findById(alocacao.getMaquina().getId());
		
		if(!Constants.ALOCADA_PARA_CLIENTE.equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
			throw new InconsistenciaException("Apenas m�quinas alocadas em clientes podem ter uma solicita��o de desaloca��o cadastrada.");
		}
		
		// Coloca a m�quina para pendente de desaloca��o
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao(Constants.PENDENTE_DE_DESALOCACAO);
		maquina.setMaquinaStatus(ms);
		maquinaRepository.merge(maquina);
		
		// Informa a data de solicita��o de desaloca��o
		alocacao.setDataCadastroDesalocacao(new Date());
		alocacaoRepository.merge(alocacao);
	}

	@Override
	public Alocacao excluir(Long id) throws InconsistenciaException {
		Alocacao alocacao = alocacaoRepository.findById(id);
		
		if(alocacao.getDataAlocacao() != null) {
			throw new InconsistenciaException("Apenas solicita��es de aloca��o que ainda n�o tenham sido alocadas ao cliente podem ser exclu�das.");
		}
		
		// Volta a m�quina para em estoque
		Maquina maquina = alocacao.getMaquina();
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao(Constants.EM_ESTOQUE);
		maquina.setMaquinaStatus(ms);
		maquinaRepository.persist(maquina);
		
		alocacaoRepository.remove(alocacao);
		
		return alocacao;
	}

	@Override
	public List<Alocacao> buscarComFiltro(Alocacao alocacao) throws CadastroInexistenteException {
		List<Alocacao> alocacoes;
		
		String codCliente = alocacao.getCliente().getCodigo();
		String codContrato = alocacao.getContrato().getCodigo();
		String codMaquina = alocacao.getMaquina().getCodigo();
		
		// Se n�o houver filtros informados, far� a busca de todos os registros
		if((codCliente == null || codCliente.isEmpty()) && (codContrato == null || codContrato.isEmpty()) && (codMaquina == null || codMaquina.isEmpty()) &&
				alocacao.getDataAlocacao() == null && alocacao.getDataDesalocacao() == null) {
			alocacoes = alocacaoRepository.getAll();
			
			if(alocacoes.isEmpty()) {
				throw new CadastroInexistenteException("N�o existem aloca��es cadastradas no sistema");
			}
			
			return alocacoes;
		} else {
			alocacoes = alocacaoRepository.buscarComFiltro(alocacao);
			
			if(alocacoes.isEmpty()) {
				throw new CadastroInexistenteException("N�o existe nenhuma aloca��o para o filtro informado.");
			}
			
			return alocacoes;
		}
	
	}

	@Override
	public void alocar(Alocacao alocacao) throws InconsistenciaException {
		LOGGER.info("Tentato confirmar a aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		Maquina maquina = null;
		
		// Valida��o da m�quina
		String codigoMaquina = alocacao.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O c�digo da m�quina n�o � v�lido");
		} else {
			maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O c�digo da m�quina n�o � v�lido");
			}
			
			if(!Constants.PENDENTE_DE_ALOCACAO.equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
				throw new InconsistenciaException("Apenas m�quinas que estejam pendentes de aloca��o podem ter uma confirma��o de aloca��o cadastrada.");
			}
		}
		
		// Coloca a situa��o da m�quina em ALOCADA PARA CLIENTE
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao(Constants.ALOCADA_PARA_CLIENTE);
		maquina.setMaquinaStatus(ms);
		maquinaRepository.merge(maquina);
		
		// Salva a aloca��o
		alocacao.setDataAlocacao(new Date());
		alocacaoRepository.merge(alocacao);
		
		LOGGER.info("Confirma��o de aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com SUCESSO.");
		LOGGER.info("A m�quina " + alocacao.getMaquina().getCodigo() + " agora est� com status " + maquina.getMaquinaStatus().getDescricao());
	}
	
	@Override
	public void desalocar(Alocacao alocacao) throws InconsistenciaException {
		LOGGER.info("Tentato confirmar a desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		Maquina maquina = null;
		
		// Valida��o da m�quina
		String codigoMaquina = alocacao.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O c�digo da m�quina n�o � v�lido");
		} else {
			maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O c�digo da m�quina n�o � v�lido");
			}
			
			if(!Constants.PENDENTE_DE_DESALOCACAO.equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
				throw new InconsistenciaException("Apenas m�quinas que estejam pendentes de desaloca��o podem ter uma confirma��o de desaloca��o cadastrada.");
			}
		}
		
		// Coloca a situa��o da m�quina para EM ESTOQUE
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao(Constants.EM_ESTOQUE);
		maquina.setMaquinaStatus(ms);
		maquinaRepository.merge(maquina);
		
		// Salva a aloca��o
		alocacao.setDataDesalocacao(new Date());
		alocacaoRepository.merge(alocacao);
		
		LOGGER.info("Confirma��o de desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com SUCESSO.");
		LOGGER.info("A m�quina " + alocacao.getMaquina().getCodigo() + " agora est� com status " + maquina.getMaquinaStatus().getDescricao());
	}

	@Override
	public List<Alocacao> findAlocacoesPendentesAlocacao() {
		return alocacaoRepository.findAlocacoesPendentesAlocacao();
	}

	@Override
	public List<Alocacao> findAlocacoesAtivas() {
		return alocacaoRepository.findAlocacoesAtivas();
	}
}
