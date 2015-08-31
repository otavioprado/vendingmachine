package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Produto;

@Stateless(name = "ProdutoRepository")
public class ProdutoRepositoryBean extends AbstractVendingMachineRepositoryBean<Produto, Long> implements ProdutoRepository {
	
	public ProdutoRepositoryBean() {
		super(Produto.class);
	}

	@Override
	public Produto findByCodigo(String codigo) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p FROM Produto p WHERE ");
		sb.append(" p.codigo = :codigo ");

		TypedQuery<Produto> query = getEntityManager().createQuery(sb.toString(), Produto.class);

		query.setParameter("codigo", codigo);

		List<Produto> produtoCadastrado = (List<Produto>) query.getResultList();
		
		if(!produtoCadastrado.isEmpty()) {
			return produtoCadastrado.get(0);
		}
		
		return null;
	}
}
