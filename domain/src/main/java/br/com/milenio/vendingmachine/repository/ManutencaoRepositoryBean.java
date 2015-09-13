package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Manutencao;
import br.com.milenio.vendingmachine.domain.model.Maquina;

@Stateless(name = "ManutencaoRepository")
public class ManutencaoRepositoryBean extends AbstractVendingMachineRepositoryBean<Manutencao, Long> 
	implements ManutencaoRepository {

	public ManutencaoRepositoryBean() {
		super(Manutencao.class);
	}

	@Override
	public List<Manutencao> buscarComFiltro(Manutencao manutencao) {
		UaiCriteria<Manutencao> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Manutencao.class);
		
		String codMaquina = manutencao.getMaquina().getCodigo();
		String codFornecedor = manutencao.getFornecedor().getCodigo();
		String motivo = manutencao.getMotivo();
		Date dataCadastro = manutencao.getDataCadastro();
		
		if(codMaquina != null && !codMaquina.isEmpty()) {
			uaiCriteria.innerJoin("maquina");
			uaiCriteria.andEquals("maquina.codigo", codMaquina);
		}
		
		if(motivo != null && !motivo.isEmpty()) {
			uaiCriteria.andEquals("motivo", motivo);
		}
		
		if(codFornecedor != null && !codFornecedor.isEmpty()) {
			uaiCriteria.innerJoin("fornecedor");
			uaiCriteria.andEquals("fornecedor.codigo", codFornecedor);
		}
		
		if(dataCadastro != null) {
			uaiCriteria.andEquals("dataCadastro", dataCadastro);
		}
		
		List<Manutencao> manutencoes = (List<Manutencao>) uaiCriteria.getResultList();
		
		return manutencoes;
	}
}
