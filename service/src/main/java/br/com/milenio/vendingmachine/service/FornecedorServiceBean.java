package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Contrato;
import br.com.milenio.vendingmachine.domain.model.Fornecedor;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.FornecedorRepository;

@Stateless
public class FornecedorServiceBean implements FornecedorService {
	
	@EJB
	FornecedorRepository fornecedorRepository;
	
	@EJB
	EnderecoService enderecoService;
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioServiceBean.class);
	
	@Override
	public void cadastrar(Fornecedor fornecedor) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Verifica se já existe um fornecedor com o mesmo CÓDIGO, EMAIL ou CPF/CNPJ cadastrado no banco de dados do sistema
		Fornecedor fornecedorCadastrado = fornecedorRepository.findFornecedorByCodigoEmailCpfCnpj(fornecedor);
		
		if(fornecedorCadastrado != null) {
			String codigo = fornecedorCadastrado.getCodigo();
			String email = fornecedorCadastrado.getEmail();
			String cpfCnpj = fornecedorCadastrado.getCpfCnpj();
			String fisicaJuridica = fornecedorCadastrado.getFisicaJuridica();
			String msg = null;
			
			if(codigo.equalsIgnoreCase(fornecedor.getCodigo())) {
				msg = "Já existe um fornecedor com o código " + fornecedor.getCodigo() + " cadastrado no sistema.";
			} else if (email.equals(fornecedor.getEmail())) {
				msg = "Já existe um fornecedor com o e-mail " + fornecedor.getEmail() + " cadastrado no sistema.";
			} else if (cpfCnpj.equals(fornecedor.getCpfCnpj())) {
				if("PF".equals(fisicaJuridica)) {
					msg = "Já existe um fornecedor com o CPF " + fornecedor.getCpfCnpj() + " cadastrado no sistema.";
				} else if ("PJ".equals(fisicaJuridica)) {
					msg = "Já existe um fornecedor com o CNPJ " + fornecedor.getCpfCnpj() + " cadastrado no sistema.";
				}
			}
			
			LOGGER.info(msg);
			throw new ConteudoJaExistenteNoBancoDeDadosException(msg);
		}
		
		enderecoService.cadastrar(fornecedor.getEndereco());
		fornecedor.setId(null);
		fornecedorRepository.persist(fornecedor);
	}

	@Override
	public List<Fornecedor> buscarFornecedoresComFiltro(Fornecedor fornecedor) throws CadastroInexistenteException {
		
		List<Fornecedor> fornecedores;
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if(parametrosVazios(fornecedor)) {
			fornecedores = fornecedorRepository.getAll();
			
			if(fornecedores.isEmpty()) {
				throw new CadastroInexistenteException("Não existem fornecedores cadastrados no sistema");
			}
			
			return fornecedores;
		} else {
			fornecedores = fornecedorRepository.buscarFornecedoresComFiltro(fornecedor);
			
			if(fornecedores.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhum cadastro de fornecedor para o filtro informado.");
			}
			
			return fornecedores;
		}
	}

	private boolean parametrosVazios(Fornecedor fornecedor) {
		String codigo = fornecedor.getCodigo();
		String fisicaJuridica = fornecedor.getFisicaJuridica();
		String cpfCnpj = fornecedor.getCpfCnpj();
		String nomeFantasia = fornecedor.getNomeFantasia();
		String email = fornecedor.getEmail();

		if((codigo == null || codigo.isEmpty()) && (fisicaJuridica == null || fisicaJuridica.isEmpty()) && 
				(cpfCnpj == null || cpfCnpj.isEmpty()) && (nomeFantasia == null || nomeFantasia.isEmpty()) && (email == null || email.isEmpty())) {
			return true;
		}
		
		return false;
	}

	@Override
	public Fornecedor findById(Long id) {
		Fornecedor fornecedor = fornecedorRepository.findById(id);
		return fornecedor;
	}

	@Override
	public void editar(Fornecedor fornecedor) {
		enderecoService.editar(fornecedor.getEndereco());
		fornecedorRepository.merge(fornecedor);
	}

	@Override
	public Fornecedor excluir(Long id) throws InconsistenciaException {

		Fornecedor fornecedor = fornecedorRepository.findById(id);
		
		/*if(fornecedor.getIndDisponivel() == false) {
			throw new InconsistenciaException("Apenas fornecedores que não tenham sido atribuídos a nenhum cliente podem ser excluídos.");
		}*/
		
		fornecedorRepository.remove(fornecedor);
		
		return fornecedor;
	}


}
