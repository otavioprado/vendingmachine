package br.com.milenio.vendingmachine.managedbean;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.milenio.vendingmachine.domain.model.ConfiguracaoSistema;
import br.com.milenio.vendingmachine.repository.ConfiguracaoSistemaRepository;

@Named
@RequestScoped
public class ConfiguracaoMB {

	private static final String QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA = "QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA";

	@Inject
	private FacesContext ctx;
	
	@EJB
	ConfiguracaoSistemaRepository configuracaoSistemaRepository;
	
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
	}
	
	public Long getQtdMaxTentAcessSenhaInvalida() {
		return qtdMaxTentAcessSenhaInvalida;
	}

	public void setQtdMaxTentAcessSenhaInvalida(Long qtdMaxTentAcessSenhaInvalida) {
		this.qtdMaxTentAcessSenhaInvalida = qtdMaxTentAcessSenhaInvalida;
	}
}
