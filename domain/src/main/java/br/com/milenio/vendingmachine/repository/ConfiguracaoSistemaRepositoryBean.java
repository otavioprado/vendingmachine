package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.ConfiguracaoSistema;

@Stateless(name = "ConfiguracaoSistemaRepository")
public class ConfiguracaoSistemaRepositoryBean extends AbstractVendingMachineRepositoryBean<ConfiguracaoSistema, Long> 
	implements ConfiguracaoSistemaRepository {

	public ConfiguracaoSistemaRepositoryBean() {
		super(ConfiguracaoSistema.class);
	}
	
	@Override
	public String getValorConfiguracaoPeloNome(String nome) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT config FROM ConfiguracaoSistema config WHERE ");
		sb.append(" config.nome = :nome");

		TypedQuery<ConfiguracaoSistema> query = getEntityManager().createQuery(sb.toString(), ConfiguracaoSistema.class);

		query.setParameter("nome", nome);

		List<ConfiguracaoSistema> configs = (List<ConfiguracaoSistema>) query.getResultList();
		
		if(!configs.isEmpty()) {
			ConfiguracaoSistema first = (ConfiguracaoSistema) configs.get(0);
			
			return first.getValor();
		}
		
		return null;
	}
	
}
