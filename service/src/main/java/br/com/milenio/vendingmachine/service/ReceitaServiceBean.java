package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.Receita;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;
import br.com.milenio.vendingmachine.repository.ReceitaRepository;
import br.com.milenio.vendingmachine.util.Constants;

@Stateless
public class ReceitaServiceBean implements ReceitaService {

	@EJB
	ReceitaRepository receitaRepository;
	
	@EJB
	MaquinaRepository maquinaRepository;
	
	@Override
	public void cadastrar(Receita receita) throws InconsistenciaException {
		String codigoMaquina = receita.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O código da máquina é inválido");
		} else {
			Maquina maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O código da máquina é inválido");
			} else if (Constants.INATIVADA.equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
				throw new InconsistenciaException("Máquinas inativadas não podem ter novas movimentações financeiras cadastradas.");
			}
		}
		
		receitaRepository.persist(receita);
	}

	@Override
	public List<Receita> buscarComFiltro(Receita receita, Date dataFim) throws CadastroInexistenteException {
		List<Receita> receitas;
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if((receita.getMaquina().getCodigo() == null || receita.getMaquina().getCodigo().isEmpty()) && 
				receita.getValor() == null && receita.getData() == null && dataFim == null && receita.getNaturezaFinanceira().getId() == null) {
			receitas = receitaRepository.getAll();
			
			if(receitas.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhuma receita cadastrada no sistema");
			}
			
			return receitas;
		} else {
			receitas = receitaRepository.buscarComFiltro(receita, dataFim);
			
			if(receitas.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhuma receita cadastrada no sistema para o filtro informado.");
			}
			
			return receitas;
		}
	}

	@Override
	public Receita findById(Long id) {
		return receitaRepository.findById(id);
	}

	@Override
	public Receita excluir(Long id) throws InconsistenciaException {
		Receita receita = receitaRepository.findById(id);
		
		if(receita == null) {
			throw new InconsistenciaException("Não existe nenhuma receita para o ID informado.");
		}
		
		receitaRepository.remove(receita);
		
		return receita;
	}

	@Override
	public void editar(Receita receita) throws InconsistenciaException {
		String codigoMaquina = receita.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O código da máquina é inválido");
		} else {
			Maquina maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O código da máquina é inválido");
			} else if (Constants.INATIVADA.equalsIgnoreCase(maquina.getMaquinaStatus().getDescricao())) {
				// Busca os dados atual da receita no banco de dados
				Receita receitaAtual = receitaRepository.findById(receita.getId());
				
				// Se a máquina está inativada, valida se a máquina da despesa sendo editada é a mesma do cadastro,
				// se for deixa passar, caso contrário joga uma exception
				if(!receitaAtual.getMaquina().getId().equals(receita.getMaquina().getId())) {
					throw new InconsistenciaException("Máquinas inativadas não podem ter novas movimentações financeiras vinculadas pela edição.");
				}
			}
		}
		
		receitaRepository.merge(receita);
	}
}
