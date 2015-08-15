package br.com.milenio.vendingmachine.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.repository.AuditoriaRepository;
import br.com.milenio.vendingmachine.repository.PerfilRepository;

@Stateless
public class AuditoriaServiceBean implements AuditoriaService {
	
	@EJB
	private AuditoriaRepository auditoriaRepository;
	
	@EJB
	PerfilRepository perfilRepository;
	
	public List<Auditoria> buscar(UsuarioSistema usuario, Date dataAcao, String ip, Long perfilId) {
		
		if(perfilId != null) {
			Perfil perfil = perfilRepository.findById(perfilId);
			usuario.setPerfil(perfil);
		}
		
		// Se nenhum filtro tiver sido informado, busca todos os registros
		if(dataAcao == null && (ip == null || ip.isEmpty()) && 
				(usuario.getLogin() == null || usuario.getLogin().isEmpty()) &&
				(usuario.getPerfil() == null)) {
			return auditoriaRepository.getAll();
		}
		
		return auditoriaRepository.buscarAcoesRealizadas(usuario, dataAcao, ip);
	}
	
	public void cadastrarNovaAcao(Auditoria auditoria) {
	    auditoria.setHorarioAcao(auditoria.getDataAcao());
		
		auditoriaRepository.persist(auditoria);
	}
}
