package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

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

	@Override
	public List<Produto> buscarComFiltro(Produto produto) {
		String codigo = produto.getCodigo();
		String descricao = produto.getDescricao();
		Boolean indPerecivel = produto.getIndPerecivel();
		String nomeFantasia = produto.getFornecedor().getNomeFantasia();
		
		UaiCriteria<Produto> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Produto.class);
		
		if(codigo != null && !codigo.isEmpty()) {
			uaiCriteria.andEquals("codigo", codigo);
		}
		
		if(descricao != null && !descricao.isEmpty()) {
			uaiCriteria.andEquals("descricao", descricao);
		}
		
		if(indPerecivel != null) {
			uaiCriteria.andEquals("indPerecivel", indPerecivel);
		}
		
		if(nomeFantasia != null && !nomeFantasia.isEmpty()) {
			uaiCriteria.innerJoin("fornecedor");
			uaiCriteria.andEquals("fornecedor.nomeFantasia", nomeFantasia);
		}
		
		List<Produto> produtos = (List<Produto>) uaiCriteria.getResultList();
		
		return produtos;
	}
}
