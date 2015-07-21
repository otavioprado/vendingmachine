package br.com.milenio.vendingmachine.managedbean;

import java.util.Date;
import java.util.List;

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
	
	@Inject
	private UsuarioService usuarioService;
	
	private UsuarioSistema usuario = new UsuarioSistema();
	private Long perfilId;

	private List<UsuarioSistema> listUsuarios;
	private String nome;
	private boolean status;

	/**
	 * Método responsável por realizar a chamada ao serviço de cadastro de usuários
	 * e informar a view do resultado, exibindo as mensagens de sucesso/erro.
	 * 
	 * @return
	 */
	public String cadastrarUsuario() {
		logger.debug("Tentando realizar o cadastro do usuário " + usuario.getNome());
		
		// Registra o usuario com a data atual
		usuario.setDataCadastro(new Date());
		
		// Informa que o cadastro do usuario deve ter a senha alterada
		usuario.setIndObrigaTrocaSenha(true);
		
		try{
			usuarioService.cadastrarUsuario(usuario, perfilId);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário " + usuario.getLogin() + " cadastrado com sucesso.", null));
			logger.info("Usuário " + usuario.getNome() + " cadastrado no sistema com sucesso.");
			
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
		
		usuario = new UsuarioSistema();
		perfilId = null;
		return "";
	}
	
	public String pesquisarUsuario() {
		listUsuarios = usuarioService.listarTodos();
		
		return "";
	}
	
	public String editarUsuario() {
		return null;
	}
	
	public String excluirUsuario() {
		return null;
	}

	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}
	
	public Long getPerfilId() {
		return perfilId;
	}

	public void setPerfilId(Long perfilId) {
		this.perfilId = perfilId;
	}
	
	public List<UsuarioSistema> getUsuariosSistema() {
		return usuarioService.listarTodos();
	}
	
	public List<UsuarioSistema> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<UsuarioSistema> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
