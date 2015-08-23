package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Endereco;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface EnderecoService {
	public Endereco consultarCepWebService(String cep) throws InconsistenciaException;
	
	public void cadastrar(Endereco endereco);
}
