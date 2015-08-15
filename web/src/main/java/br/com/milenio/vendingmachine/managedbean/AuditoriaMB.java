package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.service.AuditoriaService;

@Named
@ViewScoped
public class AuditoriaMB implements Serializable {
	private static final long serialVersionUID = 4933510600213893296L;
	
	private UsuarioSistema usuario = new UsuarioSistema();
	private Date dataAcao;
	private Long perfilId;
	private String ip;
	private List<Auditoria> lstAuditoria = new ArrayList<Auditoria>();
	
	@EJB
	AuditoriaService auditoriaService;
	
	public List<Auditoria> getLstAuditoria() {
		return lstAuditoria;
	}
	
	public void consultarAcoes() {
		lstAuditoria = auditoriaService.buscar(usuario, dataAcao, ip, perfilId);
		
		 usuario = new UsuarioSistema();
		 perfilId = null;
		 dataAcao = null;
		 ip = null;
	}

	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}

	public Date getDataAcao() {
		return dataAcao;
	}

	public void setDataAcao(Date dataAcao) {
		this.dataAcao = dataAcao;
	}

	public Long getPerfilId() {
		return perfilId;
	}

	public void setPerfilId(Long perfilId) {
		this.perfilId = perfilId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
