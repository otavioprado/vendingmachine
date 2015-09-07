package br.com.milenio.vendingmachine.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.repository.AuditoriaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;
import br.com.milenio.vendingmachine.repository.PerfilRepository;

@Stateless
public class MaquinaServiceBean implements MaquinaService {
	
	@EJB
	private AuditoriaRepository auditoriaRepository;
	
	@EJB
	private MaquinaRepository maquinaRepository;
	
	@EJB
	PerfilRepository perfilRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(MaquinaServiceBean.class);

	@Override
	public void cadastrar(Maquina maquina) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Verifica se j� existe uma m�quina com o mesmo c�digo cadastrado no banco de dados do sistema
		Maquina maquinaAtual = maquinaRepository.findByCodigo(maquina.getCodigo());
		
		if(maquinaAtual != null) {
			LOGGER.warn("J� existe uma m�quina com o c�digo " + maquina.getCodigo() + " cadastrada no banco de dados.");
			throw new ConteudoJaExistenteNoBancoDeDadosException("J� existe uma m�quina com o c�digo " + maquina.getCodigo() + " cadastrada no banco de dados.");
		}
		
		maquinaRepository.persist(maquina);
	}
	
	
}
