package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.HistoricoMaquina;

@Local
public interface HistoricoMaquinaService {
	public void cadastrar(HistoricoMaquina historicoMaquina);
}
