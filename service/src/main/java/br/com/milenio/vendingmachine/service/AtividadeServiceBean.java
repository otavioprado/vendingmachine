package br.com.milenio.vendingmachine.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.repository.AtividadeRepository;
import br.com.milenio.vendingmachine.repository.UsuarioSistemaRepository;

@Stateless
public class AtividadeServiceBean implements AtividadeService {

	@EJB
	private AtividadeRepository atividadeRepository;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private UsuarioSistemaRepository usuarioSistemaRepository;
	
	private Logger logger = LogManager.getLogger(AtividadeServiceBean.class);

	@Override
	public void cadastrarAtividade(Atividade atividade) throws CadastroInexistenteException {
		usuarioService.buscarUsuariosComFiltro(atividade.getUsuario().getLogin(), null, null);
		
		atividadeRepository.persist(atividade);
	}


	
}
