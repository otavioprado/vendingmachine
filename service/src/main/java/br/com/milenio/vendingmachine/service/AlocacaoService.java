package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface AlocacaoService {
	public void cadastrar(Alocacao alocacao) throws InconsistenciaException;

	public List<Alocacao> findAlocacoesByCliente(Long clienteId);

	public Alocacao findById(Long id);

	public void solicitarDesalocacao(Alocacao alocacao) throws InconsistenciaException;

	public Alocacao excluir(Long id) throws InconsistenciaException;

	public List<Alocacao> buscarComFiltro(Alocacao alocacao) throws CadastroInexistenteException;

	public void alocar(Alocacao alocacao) throws InconsistenciaException;
	
	public void desalocar(Alocacao alocacao) throws InconsistenciaException;
	
	public List<Alocacao> findAlocacoesAtivasByCliente(Long clienteId);

	public List<Alocacao> findAlocacoesPendentesAlocacao();

	public List<Alocacao> findAlocacoesAtivas();
}
