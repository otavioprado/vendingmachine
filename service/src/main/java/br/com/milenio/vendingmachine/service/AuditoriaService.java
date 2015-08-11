package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Local
public interface AuditoriaService {
	public List<Auditoria> buscar(UsuarioSistema usuario, Date dataAcao);
	
	public void cadastrarNovaAcao(Auditoria auditoria);
}
