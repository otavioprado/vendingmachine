package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Manutencao;

@Local
public interface ManutencaoService {
	public void cadastrar(Manutencao manutencao);
}
