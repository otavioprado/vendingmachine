package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Maquina;

@Stateless(name = "MaquinaRepository")
public class MaquinaRepositoryBean extends AbstractVendingMachineRepositoryBean<Maquina, Long> 
	implements MaquinaRepository {

	public MaquinaRepositoryBean() {
		super(Maquina.class);
	}

	@Override
	public Maquina findByCodigo(String codigo) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT m FROM Maquina m WHERE ");
		sb.append(" m.codigo = :codigo ");

		TypedQuery<Maquina> query = getEntityManager().createQuery(sb.toString(), Maquina.class);

		query.setParameter("codigo", codigo);

		List<Maquina> maquinas = (List<Maquina>) query.getResultList();
		
		if(!maquinas.isEmpty()) {
			return maquinas.get(0);
		}
		
		return null;
	}

	@Override
	public List<Maquina> buscarComFiltro(Maquina maquina) {
		UaiCriteria<Maquina> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Maquina.class);
		
		String codigo = maquina.getCodigo();
		String modelo = maquina.getModelo();
		String statusDescricao = maquina.getMaquinaStatus().getDescricao();
		Date data = maquina.getDataAquisicao();
		
		if(codigo != null && !codigo.isEmpty()) {
			uaiCriteria.andEquals("codigo", codigo);
		}
		
		if(modelo != null && !modelo.isEmpty()) {
			uaiCriteria.andEquals("modelo", modelo);
		}
		
		if(statusDescricao != null && !statusDescricao.isEmpty()) {
			uaiCriteria.innerJoin("maquinaStatus");
			uaiCriteria.andEquals("maquinaStatus.descricao", statusDescricao);
		}
		
		if(data != null) {
			uaiCriteria.andEquals("dataAquisicao", data);
		}
		
		List<Maquina> maquinas = (List<Maquina>) uaiCriteria.getResultList();
		
		return maquinas;
	}
}
