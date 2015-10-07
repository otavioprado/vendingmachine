package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.domain.model.Reserva;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;
import br.com.milenio.vendingmachine.repository.ReservaRepository;
import br.com.milenio.vendingmachine.util.Constants;

@Stateless
public class ReservaServiceBean implements ReservaService {
	
	@EJB
	ReservaRepository reservaRepository;
	
	@EJB
	MaquinaRepository maquinaRepository;
	
	@EJB
	MaquinaStatusRepository maquinaStatusRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(ReservaServiceBean.class);

	@Override
	public void cadastrar(Reserva reserva) throws InconsistenciaException {
		// Verifica se j� existe uma reserva para a m�quina
		if(reservaRepository.findByMaquina(reserva.getMaquina()) != null) {
			LOGGER.info("J� existe uma reserva para a m�quina " + reserva.getMaquina().getCodigo());
			throw new InconsistenciaException("J� existe uma reserva para a m�quina " + reserva.getMaquina().getCodigo());
		}
		
		Maquina maquina = maquinaRepository.findById(reserva.getMaquina().getId());
		if(!Constants.EM_ESTOQUE.equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
			throw new InconsistenciaException("Apenas m�quinas que estejam em estoque podem ser reservadas para um cliente.");
		}
		
		reserva.setData(new Date());
		reservaRepository.persist(reserva);
		
		// Coloca a m�quina para reservada
		MaquinaStatus status = maquinaStatusRepository.findByDescricao(Constants.RESERVADA);
		maquina.setMaquinaStatus(status);
		maquinaRepository.merge(maquina);
		
		LOGGER.info("Reserva da m�quina " + reserva.getMaquina().getCodigo() + " cadastrada com sucesso no banco de dados.");
	}

	@Override
	public List<Reserva> buscarComFiltro(Reserva reserva) throws CadastroInexistenteException {
		
		List<Reserva> reservas;
		
		String cliCodigo = reserva.getCliente().getCodigo();
		String maqCodigo = reserva.getMaquina().getCodigo();
		
		// Se n�o houver filtros informados, far� a busca de todos os registros
		if((cliCodigo == null || cliCodigo.isEmpty()) && (maqCodigo == null || maqCodigo.isEmpty()) && reserva.getData() == null) {
			reservas = reservaRepository.getAll();
			
			if(reservas.isEmpty()) {
				throw new CadastroInexistenteException("N�o existem reservas cadastrados no sistema");
			}
			
			return reservas;
		} else {
			reservas = reservaRepository.buscarComFiltro(reserva);
			
			if(reservas.isEmpty()) {
				throw new CadastroInexistenteException("N�o existe nenhum cadastro de reserva para o filtro informado.");
			}
			
			return reservas;
		}
	}

	@Override
	public Reserva findByMaquina(Maquina maquina) {
		return reservaRepository.findByMaquina(maquina);
	}

	@Override
	public Reserva excluir(Long id) throws InconsistenciaException {
		Reserva reserva = reservaRepository.findById(id);
		Maquina maquina = reserva.getMaquina();
		
		if(Constants.RESERVADA.equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
			// Salva o status da m�quina para "EM ESTOQUE"
			MaquinaStatus maquinaStatus = maquinaStatusRepository.findByDescricao(Constants.EM_ESTOQUE);
			maquina.setMaquinaStatus(maquinaStatus);
			maquinaRepository.merge(maquina);
		}
		
		reservaRepository.remove(reserva);
		
		return reserva;
	}
}