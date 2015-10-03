package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.AuditoriaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;
import br.com.milenio.vendingmachine.repository.PerfilRepository;

@Stateless
public class MaquinaServiceBean implements MaquinaService {
	
	@EJB
	private AuditoriaRepository auditoriaRepository;
	
	@EJB
	private FornecedorService fornecedorService;
	
	@EJB
	private MaquinaStatusRepository maquinaStatusRepository;
	
	@EJB
	private MaquinaRepository maquinaRepository;
	
	@EJB
	PerfilRepository perfilRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(MaquinaServiceBean.class);

	@Override
	public void cadastrar(Maquina maquina) throws ConteudoJaExistenteNoBancoDeDadosException, InconsistenciaException {
		fornecedorService.validarCodigoFornecedor(maquina.getFornecedor().getCodigo());
		
		// Verifica se j� existe uma m�quina com o mesmo c�digo cadastrado no banco de dados do sistema
		Maquina maquinaAtual = maquinaRepository.findByCodigo(maquina.getCodigo());
		
		if(maquinaAtual != null) {
			LOGGER.warn("J� existe uma m�quina com o c�digo " + maquina.getCodigo() + " cadastrada no banco de dados.");
			throw new ConteudoJaExistenteNoBancoDeDadosException("J� existe uma m�quina com o c�digo " + maquina.getCodigo() + " cadastrada no banco de dados.");
		}
		
		MaquinaStatus ms = maquinaStatusRepository.findByDescricao("EM ESTOQUE");
		maquina.setMaquinaStatus(ms);
		maquinaRepository.persist(maquina);
	}

	@Override
	public List<Maquina> buscarComFiltro(Maquina maquina) throws CadastroInexistenteException {
		List<Maquina> maquinas;
		
		// Se n�o houver filtros informados, far� a busca de todos os registros
		if((maquina.getCodigo() == null || maquina.getCodigo().isEmpty()) && (maquina.getModelo() == null || maquina.getModelo().isEmpty()) &&
				(maquina.getMaquinaStatus().getDescricao() == null || maquina.getMaquinaStatus().getDescricao().isEmpty()) && maquina.getDataAquisicao() == null) {
			maquinas = maquinaRepository.getAll();
			
			if(maquinas.isEmpty()) {
				throw new CadastroInexistenteException("N�o existem m�quinas cadastrados no sistema");
			}
			
			return maquinas;
		} else {
			maquinas = maquinaRepository.buscarComFiltro(maquina);
			
			if(maquinas.isEmpty()) {
				throw new CadastroInexistenteException("N�o existe nenhum cadastro de m�quina para o filtro informado.");
			}
			
			return maquinas;
		}
	}

	@Override
	public Maquina inativar(Long id) throws InconsistenciaException {
		Maquina maquina = maquinaRepository.findById(id);
		
		if(!"EM ESTOQUE".equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
			throw new InconsistenciaException("Apenas m�quinas em estoque podem ser inativadas!");
		}
		
		MaquinaStatus status = maquinaStatusRepository.findByDescricao("INATIVADA");
		maquina.setMaquinaStatus(status);
		maquinaRepository.merge(maquina);
		
		return maquina;
	}

	@Override
	public Maquina findById(Long id) {
		return maquinaRepository.findById(id);
	}

	@Override
	public void editar(Maquina maquina) throws ConteudoJaExistenteNoBancoDeDadosException, InconsistenciaException {
		// Carrega um objeto com os dados atuais da m�quina sendo editada
		Maquina maquinaAtual = maquinaRepository.findById(maquina.getId());
		
		fornecedorService.validarCodigoFornecedor(maquina.getFornecedor().getCodigo());
		
		Maquina resultado = null;
		// Se houve mudan�a no codigo, � necess�rio validar se o novo c�digo j� n�o existe no sistema
		if(!maquinaAtual.getCodigo().equalsIgnoreCase(maquina.getCodigo())) {
			// Se entrou aqui, ent�o houve altera��o no c�digo da m�quina
			// Verifica se j� existe uma m�quina com o mesmo c�digo cadastrado no banco de dados do sistema
			resultado = maquinaRepository.findByCodigo(maquina.getCodigo());
			
			if(resultado != null && resultado.getCodigo().equalsIgnoreCase(maquina.getCodigo())) {
				LOGGER.info("J� existe uma m�quina com o c�digo " + maquina.getCodigo() + " cadastrada no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("J� existe uma m�quina com o c�digo " + maquina.getCodigo() + " cadastrada no sistema.");
			}
		}
		
		maquinaRepository.merge(maquina);
	}
	
	@Override
	public Maquina findByCodigo(String codigo) {
		return maquinaRepository.findByCodigo(codigo);
	}

	@Override
	public List<Maquina> buscarComFiltroComVariosStatus(Maquina maquina, List<String> listMaquinaStatus)
			throws CadastroInexistenteException {
		List<Maquina> maquinas;
		
		// Se n�o houver filtros informados (INCLUSIVE A LISTA DE STATUS), ent�o far� a busca de todos os registros
		if((maquina.getCodigo() == null || maquina.getCodigo().isEmpty()) && (maquina.getModelo() == null || maquina.getModelo().isEmpty()) &&
				(maquina.getMaquinaStatus().getDescricao() == null || maquina.getMaquinaStatus().getDescricao().isEmpty()) && maquina.getDataAquisicao() == null
				&& listMaquinaStatus.isEmpty()) {
			maquinas = maquinaRepository.getAll();
			
			if(maquinas.isEmpty()) {
				throw new CadastroInexistenteException("N�o existem m�quinas cadastrados no sistema");
			}
			
			return maquinas;
		} else {
			maquinas = maquinaRepository.buscarComFiltroComVariosStatus(maquina, listMaquinaStatus);
			
			if(maquinas.isEmpty()) {
				throw new CadastroInexistenteException("N�o existe nenhum cadastro de m�quina para o filtro informado.");
			}
			
			return maquinas;
		}
	}

	@Override
	public Maquina ativar(Long id) throws InconsistenciaException {
		Maquina maquina = maquinaRepository.findById(id);
		
		if(!"INATIVADA".equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
			throw new InconsistenciaException("Apenas m�quinas inativas podem ser ativadas!");
		}
		
		MaquinaStatus status = maquinaStatusRepository.findByDescricao("EM ESTOQUE");
		maquina.setMaquinaStatus(status);
		maquinaRepository.merge(maquina);
		
		return maquina;
	}
}
