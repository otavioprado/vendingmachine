package br.com.milenio.vendingmachine.repository;

import java.util.Date;
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
	public List<Alocacao> findAlocacoesByCliente(Long clienteId) {
		UaiCriteria<Alocacao> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Alocacao.class);

		uaiCriteria.innerJoin("cliente");
		uaiCriteria.andEquals("cliente.id", clienteId);
		
		// Se tiver valor na data de desalocação, então a máquina não está mais alocada no cliente
		uaiCriteria.andIsNull("dataDesalocacao");
		
		List<Alocacao> alocacoes = (List<Alocacao>) uaiCriteria.getResultList();
		
		return alocacoes;
	}

	@Override
	public List<Alocacao> buscarComFiltro(Alocacao alocacao) {
		UaiCriteria<Alocacao> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Alocacao.class);
		
		String codCliente = alocacao.getCliente().getCodigo();
		String codContrato = alocacao.getContrato().getCodigo();
		String codMaquina = alocacao.getMaquina().getCodigo();
		
		if(codCliente != null && !codCliente.isEmpty()) {
			uaiCriteria.innerJoin("cliente");
			uaiCriteria.andEquals("cliente.codigo", codCliente);
		}
		
		if(codContrato != null && !codContrato.isEmpty()) {
			uaiCriteria.innerJoin("contrato");
			uaiCriteria.andEquals("contrato.codigo", codContrato);
		}
		
		if(codMaquina != null && !codMaquina.isEmpty()) {
			uaiCriteria.innerJoin("maquina");
			uaiCriteria.andEquals("maquina.codigo", codMaquina);
		}
		
		Date dataAlocacao = alocacao.getDataAlocacao();
		Date dataDesalocacao = alocacao.getDataDesalocacao();
		
		if(dataAlocacao != null && dataDesalocacao == null) {
			uaiCriteria.andGreaterOrEqualTo("dataAlocacao", dataAlocacao);
		} else if(dataDesalocacao != null && dataAlocacao == null) {
			uaiCriteria.andLessOrEqualTo("dataDesalocacao", dataDesalocacao);
		} else if(dataAlocacao != null && dataDesalocacao != null) {
			uaiCriteria.andBetween("dataAlocacao", dataAlocacao, dataDesalocacao);
		}
		
		List<Alocacao> alocacoes = (List<Alocacao>) uaiCriteria.getResultList();
		
		return alocacoes;
	}

	@Override
	public List<Alocacao> findAlocacoesAtivasByCliente(Long clienteId) {
		UaiCriteria<Alocacao> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Alocacao.class);

		uaiCriteria.innerJoin("cliente");
		uaiCriteria.andEquals("cliente.id", clienteId);
		
		// Se tiver valor na data de desalocação, então a máquina não está mais alocada no cliente
		uaiCriteria.andIsNull("dataDesalocacao");
		
		// Alocações efetivadas nunca terão dataAlocacao null
		uaiCriteria.andIsNotNull("dataAlocacao");
		
		List<Alocacao> alocacoes = (List<Alocacao>) uaiCriteria.getResultList();
		
		return alocacoes;
	}

	@Override
	public List<Alocacao> findAlocacoesPendentesAlocacao() {
		UaiCriteria<Alocacao> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Alocacao.class);
		
		// Se tiver valor na data de desalocação, então a máquina não está mais alocada no cliente
		uaiCriteria.andIsNotNull("dataCadastroAlocacao");
		uaiCriteria.andIsNull("dataAlocacao");
		
		List<Alocacao> alocacoes = (List<Alocacao>) uaiCriteria.getResultList();
		
		return alocacoes;
	}
}
