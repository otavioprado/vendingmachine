package br.com.milenio.vendingmachine.managedbean;

import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.service.UsuarioService;

@Named
@RequestScoped
public class UsuarioMB {
	
	@Inject
	private Logger logger;
	
	@Inject
	private FacesContext ctx;
	
	private UsuarioSistema usuario = new UsuarioSistema();
	
	@Inject
	private UsuarioService usuarioService;
	
	public String cadastrarUsuario() {
		// Registra o usuário com a data atual
		usuario.setDataCadastro(new Date());
		
		// Informa que o cadastro do usuário deve ter a senha alterada
		usuario.setIndObrigaTrocaSenha(true);
		
		if(usuarioService.cadastrarUsuario(usuario)) {
			// Sucesso
		}
		// Problema
		
		// Exibe mensagem de cadastro realizado com sucesso
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário " + usuario.getLogin() + " cadastrado com sucesso.", null));
		
		usuario = new UsuarioSistema();
		return "";
	}

	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}
}
