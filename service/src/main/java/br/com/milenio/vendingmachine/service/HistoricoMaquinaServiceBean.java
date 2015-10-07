package br.com.milenio.vendingmachine.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.HistoricoMaquina;
import br.com.milenio.vendingmachine.repository.HistoricoMaquinaRepository;

@Stateless
public class HistoricoMaquinaServiceBean implements HistoricoMaquinaService {
	
	@EJB
	HistoricoMaquinaRepository historicoMaquinaRepository;
	
	public void cadastrar(HistoricoMaquina historicoMaquina) {
		historicoMaquina.setData(new Date());
		
	    historicoMaquinaRepository.persist(historicoMaquina);
	}
}
