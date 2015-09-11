package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public void editar(Fornecedor fornecedorEditado) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Carrega um objeto com os dados atuais do fornecedor sendo editado
		Fornecedor fornecedorAtual = fornecedorRepository.findById(fornecedorEditado.getId());
		
		Fornecedor resultado = null;
		// Se houve mudança no e-mail ou no CPF/CNPJ, é necessário validar se o novo e-mail ou CPF/CNPJ já não existe no sistema
		if(!fornecedorAtual.getEmail().equalsIgnoreCase(fornecedorEditado.getEmail())) {
			// Se entrou aqui, então houve alteração no e-mail do fornecedor
			// Verifica se já existe um fornecedor com o mesmo e-mail cadastrado no banco de dados do sistema
			resultado = fornecedorRepository.findFornecedorByEmail(fornecedorEditado.getEmail());
			
			if(resultado != null && resultado.getEmail().equalsIgnoreCase(fornecedorEditado.getEmail())) {
				LOGGER.info("Já existe um fornecedor com e-mail " + fornecedorEditado.getEmail() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("Já existe um fornecedor com o e-mail " + fornecedorEditado.getEmail() + " cadastrado no sistema.");
			}
		}
		
		if(!fornecedorAtual.getCpfCnpj().equalsIgnoreCase(fornecedorEditado.getCpfCnpj())) {
			// Se entrou aqui, então houve alteração no CPF/CNPJ do fornecedor
			// Verifica se já existe um fornecedor com o mesmo CPF/CNPJ cadastrado no banco de dados do sistema
			resultado = fornecedorRepository.findFornecedorByCpfCnpj(fornecedorEditado.getCpfCnpj());
			
			if(resultado != null && resultado.getCpfCnpj().equalsIgnoreCase(fornecedorEditado.getCpfCnpj())) {
				LOGGER.info("Já existe um fornecedor com CPF/CNPJ " + fornecedorEditado.getCpfCnpj() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("Já existe um fornecedor com o CPF/CNPJ " + fornecedorEditado.getCpfCnpj() + " cadastrado no sistema.");
			}
		}
		
		enderecoService.editar(fornecedorEditado.getEndereco());
		fornecedorRepository.merge(fornecedorEditado);
	}

	@Override
	public Fornecedor excluir(Long id) throws InconsistenciaException {
		Fornecedor fornecedor = fornecedorRepository.findById(id);

		fornecedorRepository.remove(fornecedor);

		return fornecedor;
	}

	@Override
	public Fornecedor findByCodigo(String codigo) {
		return fornecedorRepository.findByCodigo(codigo);
	}
}
