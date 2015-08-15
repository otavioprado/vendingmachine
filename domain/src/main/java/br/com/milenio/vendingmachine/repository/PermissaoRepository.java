package br.com.milenio.vendingmachine.repository;

import java.util.List;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Permissao;

public interface PermissaoRepository extends Repository<Permissao, Long> {

	List<Permissao> getPermissoesVisiveis();
}
