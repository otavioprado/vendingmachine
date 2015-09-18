package br.com.milenio.vendingmachine.webservice;

import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.service.ClienteService;

@Path("/clientes")
public class ClientesWebService {
	
	@Inject
	private ClienteService clienteService;
	
	@GET
	@Path("listar")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8") 
	public Response loginAplicativo() {
		
		List<Cliente> lstClientes;
		Cliente filtro = new Cliente();
		filtro.setIndAtivo(true);
		
		try {
			lstClientes = clienteService.buscarClientesComFiltro(filtro);
		} catch (CadastroInexistenteException e) {
			// Não existe clientes cadastrados no sistema
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
		
		JsonArrayBuilder clientesBuilder = Json.createArrayBuilder();
		
		
		for(Cliente cliente : lstClientes) {
			// Contruindo o motivo do bloqueio
			JsonObjectBuilder clienteJsonBuilder = Json.createObjectBuilder();
			clienteJsonBuilder.add("nome-fantasia", cliente.getNomeFantasia());
			JsonObject jsonUsuario = clienteJsonBuilder.build();
			
			clientesBuilder.add(jsonUsuario);
		}
		
		JsonArray clientes = clientesBuilder.build();
		return Response.status(200).entity(clientes.toString()).build();
	}
}
