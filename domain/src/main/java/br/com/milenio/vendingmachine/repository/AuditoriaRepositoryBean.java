package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Stateless(name = "AuditoriaRepository")
public class AuditoriaRepositoryBean extends AbstractVendingMachineRepositoryBean<Auditoria, Long> 
	implements AuditoriaRepository {

	public AuditoriaRepositoryBean() {
		super(Auditoria.class);
	}
	
	public List<Auditoria> buscarAcoesRealizadas(UsuarioSistema usuario, Date dataAcao, String ip) {
		UaiCriteria<Auditoria> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Auditoria.class);
		
		if(usuario.getLogin() != null && !usuario.getLogin().isEmpty()) {
			uaiCriteria.innerJoin("usuario");
			uaiCriteria.andEquals("usuario.login", usuario.getLogin());
			uaiCriteria.andStringLike("usuario.login", "%" + usuario.getLogin() + "%");
		}
		
		if(dataAcao != null) {
			uaiCriteria.andEquals("dataAcao", dataAcao);
		}
		
		if(ip != null && !ip.isEmpty()) {
			uaiCriteria.andEquals("ip", ip);
		}
		
		if(usuario.getPerfil() != null) {
			uaiCriteria.innerJoin("usuario.perfil");
			uaiCriteria.andEquals("usuario.perfil.id", usuario.getPerfil().getId());
		}
		
		List<Auditoria> lstAuditoria = (List<Auditoria>) uaiCriteria.getResultList();
		
		return lstAuditoria;
	}
}
