package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.repository.AuditoriaRepository;

@Stateless
public class AuditoriaServiceBean implements AuditoriaService {
	
	@EJB
	private AuditoriaRepository auditoriaRepository;
	
	public List<Auditoria> buscar(UsuarioSistema usuario, Date dataAcao) {
		if(dataAcao == null) {
			return auditoriaRepository.getAll();
		}
		
		return auditoriaRepository.buscarAcoesRealizadas(usuario.getLogin(), dataAcao);
	}
	
	public void cadastrarNovaAcao(Auditoria auditoria) {
		auditoriaRepository.persist(auditoria);
	}
}
