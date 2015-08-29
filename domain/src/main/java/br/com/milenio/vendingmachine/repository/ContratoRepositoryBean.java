package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Contrato;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Stateless(name = "ContratoRepository")
public class ContratoRepositoryBean extends AbstractVendingMachineRepositoryBean<Contrato, Long> 
	implements ContratoRepository {

	public ContratoRepositoryBean() {
		super(Contrato.class);
	}

	@Override
	public Contrato findByCodigo(String codigo) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c FROM Contrato c WHERE ");
		sb.append(" c.codigo = :codigo ");

		TypedQuery<Contrato> query = getEntityManager().createQuery(sb.toString(), Contrato.class);

		query.setParameter("codigo", codigo);

		List<Contrato> contratoCadastrado = (List<Contrato>) query.getResultList();
		
		if(!contratoCadastrado.isEmpty()) {
			return contratoCadastrado.get(0);
		}
		
		return null;
	}

	@Override
	public List<Contrato> buscarUsuariosComFiltro(String codigo, String modalidade, Boolean indDisponivel) {
		UaiCriteria<Contrato> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Contrato.class);
		
		if(codigo != null && !codigo.isEmpty()) {
			uaiCriteria.andEquals("codigo", codigo);
		}
		
		if(modalidade != null && !modalidade.isEmpty())
			uaiCriteria.andEquals("modalidade", modalidade);
		
		if(indDisponivel != null) {
			uaiCriteria.andEquals("indDisponivel", indDisponivel);
		}
		
		List<Contrato> contratos = (List<Contrato>) uaiCriteria.getResultList();
		
		return contratos;
	}
}
