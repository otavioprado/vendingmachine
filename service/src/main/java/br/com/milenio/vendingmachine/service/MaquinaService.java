package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface MaquinaService {
	public void cadastrar(Maquina maquina) throws ConteudoJaExistenteNoBancoDeDadosException, InconsistenciaException;

	public List<Maquina> buscarComFiltro(Maquina maquina) throws CadastroInexistenteException;

	public Maquina inativar(Long id) throws InconsistenciaException;

	public Maquina findById(Long id);

	public void editar(Maquina maquina) throws ConteudoJaExistenteNoBancoDeDadosException, InconsistenciaException;

	public Maquina findByCodigo(String codigo);

	public List<Maquina> buscarComFiltroComVariosStatus(Maquina maquina, List<String> listMaquinaStatus) throws CadastroInexistenteException;

	public Maquina ativar(Long id) throws InconsistenciaException;
}
