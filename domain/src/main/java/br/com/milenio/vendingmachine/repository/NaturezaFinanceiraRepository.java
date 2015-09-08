package br.com.milenio.vendingmachine.repository;

import java.util.List;
import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.NaturezaFinanceira;

@Local
public interface NaturezaFinanceiraRepository extends Repository<NaturezaFinanceira, Long> {
	
	public NaturezaFinanceira findNaturezaFinanceiraByCodigoDescricao(NaturezaFinanceira naturezaFinanceira);
	
	public List<NaturezaFinanceira> buscarNaturezasComFiltro(NaturezaFinanceira naturezaFinanceira);
}
