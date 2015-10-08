package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.HistoricoMaquina;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.repository.HistoricoMaquinaRepository;

@Stateless
public class HistoricoMaquinaServiceBean implements HistoricoMaquinaService {
	
	@EJB
	HistoricoMaquinaRepository historicoMaquinaRepository;
	
	public void cadastrar(HistoricoMaquina historicoMaquina) {
		historicoMaquina.setData(new Date());
		
	    historicoMaquinaRepository.persist(historicoMaquina);
	}

	@Override
	public List<HistoricoMaquina> buscarComFiltro(HistoricoMaquina historicoConsParam) throws CadastroInexistenteException {
		List<HistoricoMaquina> lstHistoricoMaquina;
		
		String codMaquina = historicoConsParam.getMaquina().getCodigo();
		String status = historicoConsParam.getStatus();
		String login = historicoConsParam.getUsuarioSistema().getLogin();
		Date data = historicoConsParam.getData();
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if((codMaquina == null || codMaquina.isEmpty()) && (status == null || status.isEmpty()) && (login == null || login.isEmpty()) && data == null) {
			lstHistoricoMaquina = historicoMaquinaRepository.getAll();
			
			if(lstHistoricoMaquina.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhum histórico existente no sistema.");
			}
			
			return lstHistoricoMaquina;
		} else {
			lstHistoricoMaquina = historicoMaquinaRepository.buscarComFiltro(historicoConsParam);
			
			if(lstHistoricoMaquina.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhum histórico de máquina para o filtro informado.");
			}
			
			return lstHistoricoMaquina;
		}
	}
}
