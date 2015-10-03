package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Maquina;

@Local
public interface MaquinaRepository extends Repository<Maquina, Long> {
	public Maquina findByCodigo(String codigo);

	public List<Maquina> buscarComFiltro(Maquina maquina);

	public List<Maquina> buscarComFiltroComVariosStatus(Maquina maquina, List<String> listMaquinaStatus);
}
