package br.com.milenio.vendingmachine.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Cliente;
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

		clienteRepository.persist(cliente);
	}
}
