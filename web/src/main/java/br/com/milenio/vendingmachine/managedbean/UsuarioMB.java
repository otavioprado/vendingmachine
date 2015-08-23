package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
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
	private HttpServletRequest request;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private UsuarioService usuarioService;
	
	private UsuarioSistema usuario = new UsuarioSistema();

	private List<UsuarioSistema> listUsuarios;
	private String login;

	private Boolean status;
	private Long perfilId;
	private String motivoBloqueio;

	private Long id;

	/**
	 * M�todo respons�vel por realizar a chamada ao servi�oo de cadastro de usu�rios
	 * e informar a view do resultado, exibindo as mensagens de sucesso/erro.
	 * 
	 * @return
	 */
	public String cadastrarUsuario() {
		logger.debug("Tentando realizar o cadastro do usu�rio " + usuario.getNome());
		
		String login = usuario.getLogin() != null ? usuario.getLogin().trim() : "";
		String nome = usuario.getNome() != null ? usuario.getNome().trim() : "";
		
		if(login.isEmpty() || nome.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos login e nome n�o podem conter apenas espa�os em branco.", null));
			return "";
		}
		
		// Registra o usuario com a data atual
		usuario.setDataCadastro(new Date());
		
		// Informa que o cadastro do usuario esta ativo
		usuario.setIndAtivo(true);
		usuario.setQtdTentativasAcessoInvalido(0);
		
		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(usuario.getEmail())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail informado n�o � v�lido.", null));
			return "";
		}
		
		try{
			usuarioService.cadastrarUsuario(usuario, perfilId);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usu�rio " + usuario.getLogin() + " cadastrado com sucesso.", null));
			logger.info("Usu�rio " + usuario.getNome() + " foi cadastrado no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de usu�rio
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou o usu�rio " + usuario.getLogin());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
			
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		usuario = new UsuarioSistema();
		perfilId = null;
		
		return "";
	}
	
	public void editarUsuario() {
		logger.debug("Tentando realizar altera��es no cadastro do usu�rio " + usuario.getNome());
		
		String login = usuario.getLogin() != null ? usuario.getLogin().trim() : "";
		String nome = usuario.getNome() != null ? usuario.getNome().trim() : "";
		
		if(login.isEmpty() || nome.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos login e nome n�o podem conter apenas espa�os em branco.", null));
			return;
		}

		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(usuario.getEmail())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail informado n�o � v�lido.", null));
			return;
		}
		
		try {
			usuarioService.editarUsuario(usuario);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "As altera��es no cadastro do usu�rio " + usuario.getLogin() + " foram salvas com sucesso.", null));
			logger.info("As altera��es no cadastro do usu�rio " + usuario.getNome() + " foram salvas com sucesso.");
			
			// Processo de auditoria
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Edi��o");
			auditoria.setDescricao("Editou o usu�rio " + usuario.getLogin());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	public void consultarUsuario() {
		try {
			listUsuarios = usuarioService.buscarUsuariosComFiltro(login, status, perfilId);
		} catch (CadastroInexistenteException e) {
			if(listUsuarios != null && !listUsuarios.isEmpty()) {
				listUsuarios.clear();
			}
			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}

	public void bloquearUsuario() {
		
		motivoBloqueio = motivoBloqueio.trim();
		
		if(motivoBloqueio != null && motivoBloqueio.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo motivo bloqueio n�o pode conter apenas espa�os em branco.", null));
			return;
		}
		
		
		if (motivoBloqueio != null && motivoBloqueio.length() < 10) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo motivo bloqueio deve conter pelo menos 10 caracteres.", null));
			return;
		}
		
		if(usuarioService.bloquearUsuario(id, motivoBloqueio)) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usu�rio " + usuario.getLogin() + " bloqueado com sucesso.", null));
			// Ap�s bloqueio, carrega a lista de usu�rios para atualizar na view de consulta
			consultarUsuario();
			
			// Processo de auditoria
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Bloqueio");
			auditoria.setDescricao("Bloqueou o usu�rio " + usuario.getLogin());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		}
		
		motivoBloqueio = null;
	}
	
	public void desbloquearUsuario() {
		if(usuarioService.desbloquearUsuario(id)) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usu�rio " + usuario.getLogin() + " desbloqueado com sucesso.", null));
			// Ap�s desbloqueio, carrega a lista de usu�rios para atualizar na view de consulta
			consultarUsuario();
			
			// Processo de auditoria
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Desbloqueio");
			auditoria.setDescricao("Desbloqueou o usu�rio " + usuario.getLogin());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
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
