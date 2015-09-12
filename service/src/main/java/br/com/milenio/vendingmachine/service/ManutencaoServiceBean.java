package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Manutencao;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.ManutencaoRepository;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;

@Stateless
public class ManutencaoServiceBean implements ManutencaoService {
	
	@EJB
	ManutencaoRepository manutencaoRepository;
	
	@EJB
	MaquinaRepository maquinaRepository;
	
	@EJB
	private MaquinaStatusRepository maquinaStatusRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioServiceBean.class);
	
	@Override
	public void cadastrar(Manutencao manutencao) throws InconsistenciaException {
		manutencao.setDataCadastro(new Date());
		
		if(!"EM ESTOQUE".equalsIgnoreCase(manutencao.getMaquina().getMaquinaStatus().getDescricao())){
			throw new InconsistenciaException("Manutenções só podem ser cadastradas para máquinas que estejam em estoque.");
		}
		
		// Altera o status da máquina para EM MANUTENÇÃO
		MaquinaStatus maquinaStatus = maquinaStatusRepository.findByDescricao("EM MANUTENÇÃO");
		manutencao.getMaquina().setMaquinaStatus(maquinaStatus);
		maquinaRepository.merge(manutencao.getMaquina());
		
		// Salva a manutenção
		manutencaoRepository.persist(manutencao);
	}

	@Override
	public List<Manutencao> buscarComFiltro(Manutencao manutencao) throws CadastroInexistenteException {
		List<Manutencao> manutencoes;
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if((manutencao.getMaquina().getCodigo() == null || manutencao.getMaquina().getCodigo().isEmpty()) &&
				(manutencao.getFornecedor().getCodigo() == null || manutencao.getFornecedor().getCodigo().isEmpty()) &&
				(manutencao.getDataCadastro() == null) && (manutencao.getMotivo() == null || manutencao.getMotivo().isEmpty())) {
			manutencoes = manutencaoRepository.getAll();
			
			if(manutencoes.isEmpty()) {
				throw new CadastroInexistenteException("Não existem manutenções cadastrados no sistema");
			}
			
			return manutencoes;
		} else {
			manutencoes = manutencaoRepository.buscarComFiltro(manutencao);
			
			if(manutencoes.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhum cadastro de manutenção para o filtro informado.");
			}
			
			return manutencoes;
		}
	}
}
