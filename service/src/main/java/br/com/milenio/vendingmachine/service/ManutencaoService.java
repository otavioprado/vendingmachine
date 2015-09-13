package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Manutencao;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface ManutencaoService {
	public void cadastrar(Manutencao manutencao) throws InconsistenciaException;

	public List<Manutencao> buscarComFiltro(Manutencao manutencao) throws CadastroInexistenteException;

	public Manutencao findById(Long id);

	public void editar(Manutencao manutencao) throws InconsistenciaException;

	public Manutencao excluir(Long id) throws InconsistenciaException;

	public void efetivarRetorno(Manutencao manutencao);
}
