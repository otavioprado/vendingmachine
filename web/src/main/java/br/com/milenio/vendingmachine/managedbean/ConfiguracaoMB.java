package br.com.milenio.vendingmachine.managedbean;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.ConfiguracaoSistema;
import br.com.milenio.vendingmachine.repository.ConfiguracaoSistemaRepository;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;

@Named
@RequestScoped
public class ConfiguracaoMB {

	private static final String QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA = "QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA";

	@Inject
	private FacesContext ctx;
	
	@EJB
	ConfiguracaoSistemaRepository configuracaoSistemaRepository;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private HttpServletRequest request;
	
	private Long qtdMaxTentAcessSenhaInvalida;
	
	public void carregarConfiguracoes() {
		List<ConfiguracaoSistema> listConfig = configuracaoSistemaRepository.getAll();
		
		for(ConfiguracaoSistema config : listConfig) {
			if(QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA.equals(config.getNome())) {
				qtdMaxTentAcessSenhaInvalida = Long.parseLong(config.getValor());
			}
		}
	}
	
	public void solicitarAlteracaoConfiguracoes() {
		ConfiguracaoSistema configSis = configuracaoSistemaRepository.getConfiguracaoPeloNome(QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA);
		configSis.setValor(qtdMaxTentAcessSenhaInvalida.toString());
		configuracaoSistemaRepository.merge(configSis);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Configurações do sistema alteradas com sucesso!", null));
		
		// Processo de auditoria
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Edição");
		auditoria.setDescricao("Editou as configurações do sistema");
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
	}
	
	public Long getQtdMaxTentAcessSenhaInvalida() {
		return qtdMaxTentAcessSenhaInvalida;
	}

	public void setQtdMaxTentAcessSenhaInvalida(Long qtdMaxTentAcessSenhaInvalida) {
		this.qtdMaxTentAcessSenhaInvalida = qtdMaxTentAcessSenhaInvalida;
	}
}
