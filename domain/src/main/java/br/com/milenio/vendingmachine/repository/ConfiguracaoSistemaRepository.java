package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.ConfiguracaoSistema;

@Local
public interface ConfiguracaoSistemaRepository extends Repository<ConfiguracaoSistema, Long> {
	public String getValorConfiguracaoPeloNome(String nome);
	
	public ConfiguracaoSistema getConfiguracaoPeloNome(String nome);
}
