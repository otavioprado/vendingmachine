package br.com.milenio.vendingmachine.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.domain.model.Reserva;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;
import br.com.milenio.vendingmachine.repository.ReservaRepository;

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
		// Verifica se já existe uma reserva para a máquina
		if(reservaRepository.findByMaquina(reserva.getMaquina()) != null) {
			LOGGER.info("Já existe uma reserva para a máquina " + reserva.getMaquina().getCodigo());
			throw new InconsistenciaException("Já existe uma reserva para a máquina " + reserva.getMaquina().getCodigo());
		}
		
		Maquina maquina = maquinaRepository.findById(reserva.getMaquina().getId());
		if(!"EM ESTOQUE".equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
			throw new InconsistenciaException("Apenas máquinas que estejam em estoque podem ser reservadas para um cliente.");
		}
		
		reservaRepository.persist(reserva);
		
		// Coloca a máquina para reservada
		MaquinaStatus status = maquinaStatusRepository.findByDescricao("RESERVADA");
		maquina.setMaquinaStatus(status);
		maquinaRepository.merge(maquina);
		
		LOGGER.info("Reserva da máquina " + reserva.getMaquina().getCodigo() + " cadastrada com sucesso no banco de dados.");
	}

	@Override
	public Reserva buscarComFiltro(Reserva reserva) {
		return reservaRepository.buscarComFiltro(reserva);
	}
}