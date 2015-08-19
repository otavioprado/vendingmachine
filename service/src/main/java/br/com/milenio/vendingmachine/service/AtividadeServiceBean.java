package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.repository.AtividadeRepository;
import br.com.milenio.vendingmachine.repository.PerfilRepository;
import br.com.milenio.vendingmachine.repository.UsuarioSistemaRepository;

@Stateless
public class AtividadeServiceBean implements AtividadeService {

	@EJB
	private AtividadeRepository atividadeRepository;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private PerfilRepository perfilRepository;
	
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
	public List<Atividade> buscarAtividadesAgendadas(String login, Date data) throws CadastroInexistenteException {
		List<Atividade> atividades = atividadeRepository.findAtividadesUsuarioByLoginDate(login, data);
		
		if(atividades == null || atividades.isEmpty()) {
			throw new CadastroInexistenteException("Não existe nenhum agendamento cadastrado para hoje.");
		}
		
		return atividades;
	}

	@Override
	public List<Atividade> buscarAtividadesComFiltro(String login, Long perfilId, Date data) throws CadastroInexistenteException {
		Perfil perfil = null;
		List<Atividade> lstAtividade = null;
		
		if(perfilId != null) {
			perfil = perfilRepository.findById(perfilId);
		}
		
		if(login == null || login.isEmpty() && perfilId == null && data == null) {
			lstAtividade = atividadeRepository.getAll();
			
			if(lstAtividade == null || lstAtividade.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhuma atividade agendada no sistema.");
			}
		}
		
		lstAtividade = atividadeRepository.buscarAtividadesComFiltro(login, perfil, data);
		
		if(lstAtividade == null || lstAtividade.isEmpty()) {
			throw new CadastroInexistenteException("Não existe nenhuma atividade agendada no sistema para o filtro informado.");
		}
		
		return lstAtividade;
	}

	@Override
	public Atividade excluirAtividade(Long idAtividade) {
		Atividade atividade = atividadeRepository.findById(idAtividade);
		
		atividadeRepository.remove(atividade);
		
		return atividade;
	}

	@Override
	public Atividade findById(Long idAtividade) {
		return atividadeRepository.findById(idAtividade);
	}

	@Override
	public void editarUsuario(Atividade atividade) {
		atividadeRepository.merge(atividade);
	}
}
