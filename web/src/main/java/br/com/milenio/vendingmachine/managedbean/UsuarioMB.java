package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.service.UsuarioService;

@Named
@ViewScoped
public class UsuarioMB implements Serializable {
	
	private static final long serialVersionUID = -8922001136406729460L;

	@Inject
	private Logger logger;
	
	@Inject
	private FacesContext ctx;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private ExternalContext ec;
	
	@Inject
	private HttpSession session;
	
	private UsuarioSistema usuario = new UsuarioSistema();

	private List<UsuarioSistema> listUsuarios;
	private String login;

	private Boolean status;
	private Long perfilId;
	private String motivoBloqueio;

	private Long id;

	/**
	 * Método responsável por realizar a chamada ao serviçoo de cadastro de usuários
	 * e informar a view do resultado, exibindo as mensagens de sucesso/erro.
	 * 
	 * @return
	 */
	public String cadastrarUsuario() {
		logger.debug("Tentando realizar o cadastro do usuï¿½rio " + usuario.getNome());
		
		// Registra o usuario com a data atual
		usuario.setDataCadastro(new Date());
		
		// Informa que o cadastro do usuario esta ativo
		usuario.setIndAtivo(true);
		usuario.setQtdTentativasAcessoInvalido(0);
		
		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(usuario.getEmail())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail informado não é válido.", null));
			return "";
		}
		
		try{
			usuarioService.cadastrarUsuario(usuario, perfilId);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário " + usuario.getLogin() + " cadastrado com sucesso.", null));
			logger.info("Usuário " + usuario.getNome() + " foi cadastrado no sistema com sucesso.");
			
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
		
		usuario = new UsuarioSistema();
		perfilId = null;
		return "";
	}
	
	public void editarUsuario() {
		logger.debug("Tentando realizar alterações no cadastro do usuário " + usuario.getNome());

		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(usuario.getEmail())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail informado não é válido.", null));
			return;
		}
		
		try {
			usuarioService.editarUsuario(usuario);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "As alterações no cadastro do usuário " + usuario.getLogin() + " foram salvas com sucesso.", null));
			logger.info("As alterações no cadastro do usuário " + usuario.getNome() + " foram salvas com sucesso.");
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	public void consultarUsuario() {
		try {
			listUsuarios = usuarioService.buscarUsuariosComFiltro(login, status, perfilId);
		} catch (CadastroInexistenteException e) {
			listUsuarios.clear();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}
	
	public void bloquearUsuario() {
		
		if (motivoBloqueio != null && motivoBloqueio.length() < 10) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo motivo bloqueio deve conter pelo menos 10 caracteres.", null));
			return;
		}
		
		if(usuarioService.bloquearUsuario(id, motivoBloqueio)) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário " + usuario.getLogin() + " bloqueado com sucesso.", null));
			// Após bloqueio, carrega a lista de usuários para atualizar na view de consulta
			consultarUsuario();
		}
	}
	
	public void desbloquearUsuario() {
		if(usuarioService.desbloquearUsuario(id)) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário " + usuario.getLogin() + " desbloqueado com sucesso.", null));
			// Após desbloqueio, carrega a lista de usuários para atualizar na view de consulta
			consultarUsuario();
		}
	}
	
	public void carregarDadosUsuarioParaEdicao() {
		usuario = usuarioService.findById(id);
	}
	
	public void limparLista() {
		listUsuarios = null;
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
	
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMotivoBloqueio() {
		return motivoBloqueio;
	}

	public void setMotivoBloqueio(String motivoBloqueio) {
		this.motivoBloqueio = motivoBloqueio;
	}
}
