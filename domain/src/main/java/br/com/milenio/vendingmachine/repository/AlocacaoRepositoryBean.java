package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Alocacao;

@Stateless(name = "AlocacaoRepository")
public class AlocacaoRepositoryBean extends AbstractVendingMachineRepositoryBean<Alocacao, Long> 
	implements AlocacaoRepository {

	public AlocacaoRepositoryBean() {
		super(Alocacao.class);
	}

	@Override
	public List<Alocacao> findByCliente(Long clienteId) {
		UaiCriteria<Alocacao> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Alocacao.class);

		uaiCriteria.innerJoin("cliente");
		uaiCriteria.andEquals("cliente.id", clienteId);
		
		List<Alocacao> alocacoes = (List<Alocacao>) uaiCriteria.getResultList();
		
		return alocacoes;
	}
}
