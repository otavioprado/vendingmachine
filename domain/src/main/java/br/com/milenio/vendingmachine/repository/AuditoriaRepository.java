package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Local
public interface AuditoriaRepository extends Repository<Auditoria, Long> {

	public List<Auditoria> buscarAcoesRealizadas(UsuarioSistema usuario, Date dataAcao, String ip);
}
