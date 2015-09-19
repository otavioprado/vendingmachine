package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.Despesa;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.DespesaRepository;
import br.com.milenio.vendingmachine.repository.MaquinaRepository;

@Stateless
public class DespesaServiceBean implements DespesaService {

	@EJB
	DespesaRepository despesaRepository;
	
	@EJB
	MaquinaRepository maquinaRepository;
	
	@Override
	public void cadastrar(Despesa despesa) throws InconsistenciaException {
		String codigoMaquina = despesa.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O código da máquina é inválido");
		} else {
			Maquina maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O código da máquina é inválido");
			}
		}
		
		despesaRepository.persist(despesa);
	}

	@Override
	public List<Despesa> buscarComFiltro(Despesa despesa, Date dataFim) throws CadastroInexistenteException {
		List<Despesa> despesas;
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if((despesa.getMaquina().getCodigo() == null || despesa.getMaquina().getCodigo().isEmpty()) && 
				despesa.getValor() == null && despesa.getData() == null && dataFim == null && despesa.getNaturezaFinanceira().getId() == null) {
			despesas = despesaRepository.getAll();
			
			if(despesas.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhuma despesa cadastrada no sistema");
			}
			
			return despesas;
		} else {
			despesas = despesaRepository.buscarComFiltro(despesa, dataFim);
			
			if(despesas.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhuma despesa cadastrada no sistema para o filtro informado.");
			}
			
			return despesas;
		}
	}

	@Override
	public Despesa findById(Long id) {
		return despesaRepository.findById(id);
	}

	@Override
	public Despesa excluir(Long id) throws InconsistenciaException {
		Despesa despesa = despesaRepository.findById(id);
		
		if(despesa == null) {
			throw new InconsistenciaException("Não existe nenhuma despesa para o ID informado.");
		}
		
		despesaRepository.remove(despesa);
		
		return despesa;
	}

	@Override
	public void editar(Despesa despesa) throws InconsistenciaException {
		String codigoMaquina = despesa.getMaquina().getCodigo();
		if(codigoMaquina == null || codigoMaquina.isEmpty()) {
			throw new InconsistenciaException("O código da máquina é inválido");
		} else {
			Maquina maquina = maquinaRepository.findByCodigo(codigoMaquina);
			
			if(maquina == null) {
				throw new InconsistenciaException("O código da máquina é inválido");
			}
		}
		
		despesaRepository.merge(despesa);
	}
}
