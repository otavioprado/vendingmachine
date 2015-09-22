package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Cliente;

@Stateless(name = "ClienteRepository")
public class ClienteRepositoryBean extends AbstractVendingMachineRepositoryBean<Cliente, Long> 
	implements ClienteRepository {

	public ClienteRepositoryBean() {
		super(Cliente.class);
	}

	@Override
	public Cliente findClienteByCodigoEmailCpfCnpj(Cliente cliente) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c FROM Cliente c WHERE ");
		sb.append(" c.codigo = :codigo OR");
		sb.append(" c.email = :email OR");
		sb.append(" c.cpfCnpj = :cpfCnpj ");

		TypedQuery<Cliente> query = getEntityManager().createQuery(sb.toString(), Cliente.class);

		query.setParameter("codigo", cliente.getCodigo());
		query.setParameter("email", cliente.getEmail());
		query.setParameter("cpfCnpj", cliente.getCpfCnpj());

		List<Cliente> clienteCadastrado = (List<Cliente>) query.getResultList();
		
		if(!clienteCadastrado.isEmpty()) {
			return clienteCadastrado.get(0);
		}
		
		return null;
	}
	
	public List<Cliente> buscarClientesComFiltro(Cliente cliente) {
		String codigo = cliente.getCodigo();
		String fisicaJuridica = cliente.getFisicaJuridica();
		String cpfCnpj = cliente.getCpfCnpj();
		String nomeFantasia = cliente.getNomeFantasia();
		Boolean indAtivo = cliente.getIndAtivo();
		String email = cliente.getEmail();
		
		UaiCriteria<Cliente> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Cliente.class);
		
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
		
		if(indAtivo != null) {
			uaiCriteria.andEquals("indAtivo", indAtivo);
		}
		
		if(email != null && !email.isEmpty()) {
			uaiCriteria.andEquals("email", email);
		}
		
		List<Cliente> usuarios = (List<Cliente>) uaiCriteria.getResultList();
		
		return usuarios;
	}
	
	@Override
	public Cliente findClienteByEmail(String email) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c FROM Cliente c WHERE ");
		sb.append(" c.email = :email");

		TypedQuery<Cliente> query = getEntityManager().createQuery(sb.toString(), Cliente.class);

		query.setParameter("email", email);

		List<Cliente> clienteCadastrado = (List<Cliente>) query.getResultList();
		
		if(!clienteCadastrado.isEmpty()) {
			return clienteCadastrado.get(0);
		}
		
		return null;
	}
	
	@Override
	public Cliente findClienteByCpfCnpj(String cpfCnpj) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c FROM Cliente c WHERE ");
		sb.append(" c.cpfCnpj = :cpfCnpj");

		TypedQuery<Cliente> query = getEntityManager().createQuery(sb.toString(), Cliente.class);

		query.setParameter("cpfCnpj", cpfCnpj);

		List<Cliente> clienteCadastrado = (List<Cliente>) query.getResultList();
		
		if(!clienteCadastrado.isEmpty()) {
			return clienteCadastrado.get(0);
		}
		
		return null;
	}

	@Override
	public Cliente findByCodigo(String codigo) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c FROM Cliente c WHERE ");
		sb.append(" c.codigo = :codigo");

		TypedQuery<Cliente> query = getEntityManager().createQuery(sb.toString(), Cliente.class);

		query.setParameter("codigo", codigo);

		List<Cliente> clienteCadastrado = (List<Cliente>) query.getResultList();
		
		if(!clienteCadastrado.isEmpty()) {
			return clienteCadastrado.get(0);
		}
		
		return null;
	}
}
