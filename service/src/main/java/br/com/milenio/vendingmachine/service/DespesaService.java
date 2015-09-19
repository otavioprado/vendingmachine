package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Despesa;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface DespesaService {
	public void cadastrar(Despesa despesa) throws InconsistenciaException;

	public List<Despesa> buscarComFiltro(Despesa despesa, Date dataFim) throws CadastroInexistenteException;

	public Despesa findById(Long id);

	public Despesa excluir(Long id) throws InconsistenciaException;

	public void editar(Despesa despesa) throws InconsistenciaException;
}
