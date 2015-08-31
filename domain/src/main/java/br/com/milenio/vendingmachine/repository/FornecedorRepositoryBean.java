package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Fornecedor;

@Stateless(name = "FornecedorRepository")
public class FornecedorRepositoryBean extends AbstractVendingMachineRepositoryBean<Fornecedor, Long> 
	implements FornecedorRepository {

	public FornecedorRepositoryBean() {
		super(Fornecedor.class);
	}

	@Override
	public Fornecedor findFornecedorByCodigoEmailCpfCnpj(Fornecedor fornecedor) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT f FROM Fornecedor f WHERE ");
		sb.append(" f.codigo = :codigo OR");
		sb.append(" f.email = :email OR");
		sb.append(" f.cpfCnpj = :cpfCnpj ");

		TypedQuery<Fornecedor> query = getEntityManager().createQuery(sb.toString(), Fornecedor.class);

		query.setParameter("codigo", fornecedor.getCodigo());
		query.setParameter("email", fornecedor.getEmail());
		query.setParameter("cpfCnpj", fornecedor.getCpfCnpj());

		List<Fornecedor> fornecedorCadastrado = (List<Fornecedor>) query.getResultList();
		
		if(!fornecedorCadastrado.isEmpty()) {
			return fornecedorCadastrado.get(0);
		}
		
		return null;
	}
	
	@Override
	public List<Fornecedor> buscarFornecedoresComFiltro(Fornecedor fornecedor) {
		String codigo = fornecedor.getCodigo();
		String fisicaJuridica = fornecedor.getFisicaJuridica();
		String cpfCnpj = fornecedor.getCpfCnpj();
		String nomeFantasia = fornecedor.getNomeFantasia();
		String email = fornecedor.getEmail();
		
		UaiCriteria<Fornecedor> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Fornecedor.class);
		
		if(codigo != null && !codigo.isEmpty()) {
			uaiCriteria.andEquals("codigo", codigo);
		}
		
		if(fisicaJuridica != null && !fisicaJuridica.isEmpty()) {
			uaiCriteria.andEquals("fisicaJuridica", fisicaJuridica);
		}
		
		if(cpfCnpj != null && !cpfCnpj.isEmpty()) {
			uaiCriteria.andEquals("cpfCnpj", cpfCnpj);
		}
		
		if(nomeFantasia != null && !nomeFantasia.isEmpty()) {
			uaiCriteria.andEquals("nomeFantasia", nomeFantasia);
		}
		
		if(email != null && !email.isEmpty()) {
			uaiCriteria.andEquals("email", email);
		}
		
		List<Fornecedor> fornecedores = (List<Fornecedor>) uaiCriteria.getResultList();
		
		return fornecedores;
	}

	@Override
	public Fornecedor findByCodigo(String codigo) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT f FROM Fornecedor f WHERE ");
		sb.append(" f.codigo = :codigo");

		TypedQuery<Fornecedor> query = getEntityManager().createQuery(sb.toString(), Fornecedor.class);

		query.setParameter("codigo", codigo);

		List<Fornecedor> fornecedorCadastrado = (List<Fornecedor>) query.getResultList();
		
		if(!fornecedorCadastrado.isEmpty()) {
			return fornecedorCadastrado.get(0);
		}
		
		return null;
	}

	@Override
	public Fornecedor findFornecedorByEmail(String email) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT f FROM Fornecedor f WHERE ");
		sb.append(" f.email = :email");

		TypedQuery<Fornecedor> query = getEntityManager().createQuery(sb.toString(), Fornecedor.class);

		query.setParameter("email", email);

		List<Fornecedor> fornecedorCadastrado = (List<Fornecedor>) query.getResultList();
		
		if(!fornecedorCadastrado.isEmpty()) {
			return fornecedorCadastrado.get(0);
		}
		
		return null;
	}

	@Override
	public Fornecedor findFornecedorByCpfCnpj(String cpfCnpj) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT f FROM Fornecedor f WHERE ");
		sb.append(" f.cpfCnpj = :cpfCnpj");

		TypedQuery<Fornecedor> query = getEntityManager().createQuery(sb.toString(), Fornecedor.class);

		query.setParameter("cpfCnpj", cpfCnpj);

		List<Fornecedor> fornecedorCadastrado = (List<Fornecedor>) query.getResultList();
		
		if(!fornecedorCadastrado.isEmpty()) {
			return fornecedorCadastrado.get(0);
		}
		
		return null;
	}
	
	
}
