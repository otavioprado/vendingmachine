package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
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

	@Override
	public void cadastrarAtividade(Atividade atividade) throws CadastroInexistenteException {
		UsuarioSistema usuario = usuarioSistemaRepository.findUsuarioByLogin(atividade.getUsuario().getLogin());
		
		if(usuario == null) {
			throw new CadastroInexistenteException("Não existe nenhum usuário cadastrado no sistema para o login " + atividade.getUsuario().getLogin() + " informado.");
		}
		
		atividade.setUsuario(usuario);
		atividadeRepository.persist(atividade);
	}

	@Override
	public List<Atividade> buscarAtividadesAgendadas(String login) {
		return atividadeRepository.findAtividadesUsuarioByLogin(login);
	}
}
