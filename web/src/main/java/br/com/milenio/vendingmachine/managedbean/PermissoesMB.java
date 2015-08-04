package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;
import br.com.milenio.vendingmachine.repository.PerfilRepository;
import br.com.milenio.vendingmachine.repository.PermissaoRepository;
import br.com.milenio.vendingmachine.security.spring.Teste;

@Named
@ViewScoped
public class PermissoesMB implements Serializable {

	private static final long serialVersionUID = 3381346066960699037L;

	@EJB
	private PerfilRepository perfilRepository;
	
	@EJB
	private PermissaoRepository permissaoRepository;
	
	@Inject
	private FacesContext ctx;

	private List<Permissao> permissoes;
	
	private List<Permissao> permissoesSelecionadas;
	
	private Long idPerfilSelecionado;
	
	private Perfil perfilSelecionado;
	
	public void carregarPermissoesPerfil() {
		if(idPerfilSelecionado == null ||idPerfilSelecionado == 0) {
			permissoes.clear();
			permissoesSelecionadas.clear();
			return;
		}
		
		// Carrega todas as permissões
		permissoes = permissaoRepository.getAll();
		
		// Busca as permissões do perfil selecionado
		perfilSelecionado = perfilRepository.findById(idPerfilSelecionado);
		permissoesSelecionadas = perfilRepository.getPermissoesPerfil(perfilSelecionado);
	}
	
	public void solicitarAlteracoesDasPermissoesDoPerfil() {
		perfilSelecionado.setPermissoes(permissoesSelecionadas);
		perfilRepository.merge(perfilSelecionado);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Permissões do perfil " + perfilSelecionado.getNome() + " alteradas com sucesso.", null));
		
		
		Teste teste = new Teste();
		teste.invalidar();
	}
	
	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
	
	public Long getIdPerfilSelecionado() {
		return idPerfilSelecionado;
	}

	public void setIdPerfilSelecionado(Long idPerfilSelecionado) {
		this.idPerfilSelecionado = idPerfilSelecionado;
	}
	
	public List<Permissao> getPermissoesSelecionadas() {
		return permissoesSelecionadas;
	}

	public void setPermissoesSelecionadas(List<Permissao> permissoesSelecionadas) {
		this.permissoesSelecionadas = permissoesSelecionadas;
	}
}
