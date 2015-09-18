package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Receita;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface ReceitaService {
	public void cadastrar(Receita receita) throws InconsistenciaException;

	public List<Receita> buscarComFiltro(Receita receita, Date dataFim) throws CadastroInexistenteException;

	public Receita findById(Long id);

	public Receita excluir(Long id) throws InconsistenciaException;

	public void editar(Receita receita) throws InconsistenciaException;
}
