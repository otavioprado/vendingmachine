package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.UsuarioBloqueadoNoSistemaException;
import br.com.milenio.vendingmachine.exceptions.UsuarioInexistenteNoSistemaException;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.UsuarioService;

@Named
@RequestScoped
public class LoginMB {

	@Inject
	private FacesContext ctx;

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;

	@Inject
	private Logger logger;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	private Map<String,Object> perfil = new LinkedHashMap<String,Object>();

	private String usuario;
	private String senha;

	public void efetuarLogin() throws ServletException, IOException {
		UsuarioSistema usuarioValidado = null;
		try{ 
			usuarioValidado = usuarioService.validarUsuarioAtivoPeloLoginSenha(usuario, senha);
		} catch(UsuarioBloqueadoNoSistemaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Este usuário está bloqueado no sistema. Motivo: " + e.getMessage(), null));
			return;
		} catch (UsuarioInexistenteNoSistemaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return;
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
		dispatcher.forward(request, response);
		
		ctx.responseComplete();
		
		// Processo de auditoria de login
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Login");
		auditoria.setDescricao("Entrou no sistema");
		auditoria.setUsuario(usuarioValidado);
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
	}
	
	public void preRender() {
		if("true".equals(request.getParameter("invalid"))) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválidos!", null));
		}
	}

	public String cadastrarUsuario() {
		return null;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
