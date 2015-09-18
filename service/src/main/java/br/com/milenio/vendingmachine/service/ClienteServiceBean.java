package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.repository.ClienteRepository;

@Stateless
public class ClienteServiceBean implements ClienteService {
	
	@EJB
	ClienteRepository clienteRepository;
	
	@EJB
	EnderecoService enderecoService;
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioServiceBean.class);
	
	public void cadastrar(Cliente cliente) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Verifica se já existe um cliente com o mesmo CÓDIGO, EMAIL ou CPF/CNPJ cadastrado no banco de dados do sistema
		Cliente clienteCadastrado = clienteRepository.findClienteByCodigoEmailCpfCnpj(cliente);
		
		if(clienteCadastrado != null) {
			String codigo = clienteCadastrado.getCodigo();
			String email = clienteCadastrado.getEmail();
			String cpfCnpj = clienteCadastrado.getCpfCnpj();
			String fisicaJuridica = clienteCadastrado.getFisicaJuridica();
			String msg = null;
			
			if(codigo.equalsIgnoreCase(cliente.getCodigo())) {
				msg = "Já existe um cliente com o código " + cliente.getCodigo() + " cadastrado no sistema.";
			} else if (email.equals(cliente.getEmail())) {
				msg = "Já existe um cliente com o e-mail " + cliente.getEmail() + " cadastrado no sistema.";
			} else if (cpfCnpj.equals(cliente.getCpfCnpj())) {
				if("PF".equals(fisicaJuridica)) {
					msg = "Já existe um cliente com o CPF " + cliente.getCpfCnpj() + " cadastrado no sistema.";
				} else if ("PJ".equals(fisicaJuridica)) {
					msg = "Já existe um cliente com o CNPJ " + cliente.getCpfCnpj() + " cadastrado no sistema.";
				}
			}
			
			LOGGER.info(msg);
			throw new ConteudoJaExistenteNoBancoDeDadosException(msg);
		}
		
		enderecoService.cadastrar(cliente.getEndereco());
		cliente.setId(null);
		cliente.setIndAtivo(true);
		clienteRepository.persist(cliente);
	}

	@Override
	public List<Cliente> buscarClientesComFiltro(Cliente cliente) throws CadastroInexistenteException {
		List<Cliente> clientes;
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if(parametrosVazios(cliente)) {
			clientes = clienteRepository.getAll();
			
			if(clientes.isEmpty()) {
				throw new CadastroInexistenteException("Não existem clientes cadastrados no sistema");
			}
			return clientes;
		} else {
			clientes = clienteRepository.buscarClientesComFiltro(cliente);
			
			if(clientes.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhum cadastro de cliente para o filtro informado.");
			}
			return clientes;
		}
	}

	private boolean parametrosVazios(Cliente cliente) {
		String codigo = cliente.getCodigo();
		String fisicaJuridica = cliente.getFisicaJuridica();
		String cpfCnpj = cliente.getCpfCnpj();
		String nomeFantasia = cliente.getNomeFantasia();
		Boolean indAtivo = cliente.getIndAtivo();
		String email = cliente.getEmail();

		if((codigo == null || codigo.isEmpty()) && (fisicaJuridica == null || fisicaJuridica.isEmpty()) && 
				(cpfCnpj == null || cpfCnpj.isEmpty()) && (nomeFantasia == null || nomeFantasia.isEmpty()) 
				&& (indAtivo == null) && (email == null || email.isEmpty())) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean bloquearUsuario(Long id, String motivoBloqueio) {
		Cliente cliente = clienteRepository.findById(id);
		cliente.setIndAtivo(false);
		cliente.setMotivoBloqueio(motivoBloqueio);
		clienteRepository.persist(cliente);
		
		return true;
	}

	@Override
	public boolean desbloquearUsuario(Long id) {
		Cliente cliente = clienteRepository.findById(id);
		cliente.setIndAtivo(true);
		cliente.setMotivoBloqueio(null);
		clienteRepository.persist(cliente);
		
		return true;
	}

	@Override
	public Cliente findById(Long id) {
		Cliente cliente = clienteRepository.findById(id);
		return cliente;
	}

	@Override
	public void editar(Cliente clienteEditado) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Carrega um objeto com os dados atuais do cliente sendo editado
		Cliente clienteAtual = clienteRepository.findById(clienteEditado.getId());
		
		Cliente resultado = null;
		// Se houve mudança no e-mail ou no CPF/CNPJ, é necessário validar se o novo e-mail ou CPF/CNPJ já não existe no sistema
		if(!clienteAtual.getEmail().equalsIgnoreCase(clienteEditado.getEmail())) {
			// Se entrou aqui, então houve alteração no e-mail do cliente
			// Verifica se já existe um cliente com o mesmo e-mail cadastrado no banco de dados do sistema
			resultado = clienteRepository.findClienteByEmail(clienteEditado.getEmail());
			
			if(resultado != null && resultado.getEmail().equalsIgnoreCase(clienteEditado.getEmail())) {
				LOGGER.info("Já existe um cliente com e-mail " + clienteEditado.getEmail() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("Já existe um cliente com o e-mail " + clienteEditado.getEmail() + " cadastrado no sistema.");
			}
		}
		
		if(!clienteAtual.getCpfCnpj().equalsIgnoreCase(clienteEditado.getCpfCnpj())) {
			// Se entrou aqui, então houve alteração no CPF/CNPJ do cliente
			// Verifica se já existe um cliente com o mesmo CPF/CNPJ cadastrado no banco de dados do sistema
			resultado = clienteRepository.findClienteByCpfCnpj(clienteEditado.getCpfCnpj());
			
			if(resultado != null && resultado.getCpfCnpj().equalsIgnoreCase(clienteEditado.getCpfCnpj())) {
				LOGGER.info("Já existe um cliente com CPF/CNPJ " + clienteEditado.getCpfCnpj() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("Já existe um cliente com o CPF/CNPJ " + clienteEditado.getCpfCnpj() + " cadastrado no sistema.");
			}
		}
		
		enderecoService.editar(clienteEditado.getEndereco());
		clienteRepository.merge(clienteEditado);
	}
}
