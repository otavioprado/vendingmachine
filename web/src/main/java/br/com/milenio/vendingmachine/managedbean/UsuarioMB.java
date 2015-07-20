package br.com.milenio.vendingmachine.managedbean;

import java.nio.charset.Charset;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
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
		logger.debug("Tentando realizar o cadastro do usu�rio " + usuario.getNome());
		
		// Registra o usuario com a data atual
		usuario.setDataCadastro(new Date());
		
		// Informa que o cadastro do usuario deve ter a senha alterada
		usuario.setIndObrigaTrocaSenha(true);
		
		try{
			usuarioService.cadastrarUsuario(usuario);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usu�rio " + usuario.getLogin() + " cadastrado com sucesso.", null));
			logger.info("Usu�rio " + usuario.getNome() + " cadastrado no sistema com sucesso.");
			
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "J� �a T� � � � � n�o ? !", null));
		}
		
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
