package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

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
}
