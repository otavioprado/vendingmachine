package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;
import br.com.milenio.vendingmachine.repository.PerfilRepository;
import br.com.milenio.vendingmachine.repository.PermissaoRepository;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.security.spring.PermissoesUtils;
import br.com.milenio.vendingmachine.service.AuditoriaService;

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
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private HttpServletRequest request;

	private List<Permissao> permissoes;
	
	private List<Permissao> permissoesSelecionadas;
	
	private Long idPerfilSelecionado;
	
	private Perfil perfilSelecionado;
	
	public void carregarPermissoesPerfil() {
		if(idPerfilSelecionado == null || idPerfilSelecionado == 0) {
			permissoes.clear();
			permissoesSelecionadas.clear();
			return;
		}
		
		// Carrega todas as permissões
		permissoes = permissaoRepository.getPermissoesVisiveis();
		
		// Busca as permissões do perfil selecionado
		perfilSelecionado = perfilRepository.findById(idPerfilSelecionado);
		permissoesSelecionadas = perfilRepository.getPermissoesPerfil(perfilSelecionado);
	}
	
	public void solicitarAlteracoesDasPermissoesDoPerfil() throws ServletException, IOException {
		if(idPerfilSelecionado == null || idPerfilSelecionado == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecione um perfil antes de realizar essa operação.", null));
			return;
		}
		
		if(!validarDependenciaEntrePermissoes(permissoesSelecionadas)) {
			// Busca novamente as permissões do perfil selecionado
			permissoesSelecionadas = perfilRepository.getPermissoesPerfil(perfilSelecionado);
			return;
		}
		
		if("ADMINISTRADOR".equals(perfilSelecionado.getNome())) {
			permissoesSelecionadas.addAll(permissaoRepository.getPermissoesAdministrativasRestritas());
		}
		
		Permissao permissaoLogin = permissaoRepository.findById(1L);
		permissoesSelecionadas.add(permissaoLogin);
		
		perfilSelecionado.setPermissoes(permissoesSelecionadas);
		perfilRepository.merge(perfilSelecionado);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Permissões do perfil " + perfilSelecionado.getNome() + " alteradas com sucesso.", null));
		
		
		PermissoesUtils permissoesUtils = new PermissoesUtils();
		permissoesUtils.atualizarPermissoesDosUsuariosLogadosComPerfil(perfilSelecionado);
		
		// Processo de auditoria
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Edição");
		auditoria.setDescricao("Editou as permissões do perfil " + perfilSelecionado.getNome());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
	}
	
	public boolean validarDependenciaEntrePermissoes(List<Permissao> permissoesSelecionadas) {
		for(Permissao permissao : permissoesSelecionadas) {
			if(permissao.getDependencia() != null) {
				Permissao dependencia = permissao.getDependencia();
				
				if(!permissoesSelecionadas.contains(dependencia)){
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Para atribuir a permissão " + permissao.getDescricao() + " é necessário selecionar a permissão " + dependencia.getDescricao() + ".", null));
					return false;
				}
			}
		}
		
		return true;
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
