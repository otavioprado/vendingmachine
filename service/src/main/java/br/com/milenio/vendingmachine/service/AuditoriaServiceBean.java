package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.repository.AuditoriaRepository;
import br.com.milenio.vendingmachine.repository.PerfilRepository;

@Stateless
public class AuditoriaServiceBean implements AuditoriaService {
	
	@EJB
	private AuditoriaRepository auditoriaRepository;
	
	@EJB
	PerfilRepository perfilRepository;
	
	public List<Auditoria> buscar(UsuarioSistema usuario, Date dataAcao, String ip, Long perfilId) throws CadastroInexistenteException {
		
		if(perfilId != null) {
			Perfil perfil = perfilRepository.findById(perfilId);
			usuario.setPerfil(perfil);
		}
		
		List<Auditoria> lstAuditoria;
		
		// Se nenhum filtro tiver sido informado, busca todos os registros
		if(dataAcao == null && (ip == null || ip.isEmpty()) && 
				(usuario.getLogin() == null || usuario.getLogin().isEmpty()) &&
				(usuario.getPerfil() == null)) {
			lstAuditoria = auditoriaRepository.getAll();
			
			if(lstAuditoria == null || lstAuditoria.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhuma ação auditada no sistema");
			}
		}
		
		lstAuditoria = auditoriaRepository.buscarAcoesRealizadas(usuario, dataAcao, ip);
		
		if(lstAuditoria == null || lstAuditoria.isEmpty()) {
			throw new CadastroInexistenteException("Não existe nenhum registro de auditoria para o filtro informado.");
		}
		
		return lstAuditoria;
	}
	
	public void cadastrarNovaAcao(Auditoria auditoria) {
	    auditoria.setHorarioAcao(auditoria.getDataAcao());
		
		auditoriaRepository.persist(auditoria);
	}
}
