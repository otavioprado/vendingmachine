package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;
import br.com.milenio.vendingmachine.repository.PerfilRepository;
import br.com.milenio.vendingmachine.service.UsuarioService;

@Named
@RequestScoped
public class PermissoesMB implements Serializable {

	private static final long serialVersionUID = 3381346066960699037L;

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
	
	@EJB
	private PerfilRepository perfilRepository;

	private Set<Permissao> permissoes;
	
	private Set<Permissao> permissoesSelecionadas;
	
	private Long idPerfilSelecionado;
	
	public void carregarPermissoesPerfil() {
		Perfil perfilSelecionado = perfilRepository.findById(idPerfilSelecionado);
		
		permissoes = perfilRepository.getPermissoesPerfil(perfilSelecionado);
	}
	
	public Set<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
	
	public Long getIdPerfilSelecionado() {
		return idPerfilSelecionado;
	}

	public void setIdPerfilSelecionado(Long idPerfilSelecionado) {
		this.idPerfilSelecionado = idPerfilSelecionado;
	}
	
	public Set<Permissao> getPermissoesSelecionadas() {
		return permissoesSelecionadas;
	}

	public void setPermissoesSelecionadas(Set<Permissao> permissoesSelecionadas) {
		this.permissoesSelecionadas = permissoesSelecionadas;
	}
}
