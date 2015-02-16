package br.com.milenio.vendingmachine.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Usuario;

// Solução: http://stackoverflow.com/questions/25644007/request-io-undertow-servlet-spec-httpservletrequestimpl-was-not-original-or-a-wr

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
	
	private List<Usuario> usuariosSistemas = new ArrayList<Usuario>();

	public List<Usuario> getUsuariosSistemas() {
		Usuario user = new Usuario();
		user.setNome("Otávio Prado");
		user.setSenhaAplicacao("123");
		user.setEmail("otavio_lipe@hotmail.com");
		
		usuariosSistemas.add(user);
		
		return usuariosSistemas;
	}

	public void setUsuariosSistemas(List<Usuario> usuariosSistemas) {
		this.usuariosSistemas = usuariosSistemas;
	}

	private String usuario;
	private String senha;

	public void efetivarLogin() throws ServletException, IOException {

		logger.debug("Warn test 1994");

		logger.info("Info test 1994");

		logger.warn("Warn test 1994");

		logger.error("Info test 1994");

		logger.fatal("Warn test 1994");

		RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
		dispatcher.forward(request, response);
		ctx.responseComplete();
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
