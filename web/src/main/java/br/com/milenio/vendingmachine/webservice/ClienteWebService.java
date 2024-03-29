package br.com.milenio.vendingmachine.webservice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.service.AlocacaoService;

@Path("/clientes")
public class ClienteWebService {
	
	@Inject
	private AlocacaoService alocacaoService;
	
	/**
	 * Retorna todos os clientes que tem m�quinas alocadas (Login > Gerenciar m�quinas)
	 * @return
	 */
	@GET
	@Path("listar")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8") 
	public Response listar() {
		try {
			List<Alocacao> alocacoes = alocacaoService.findAlocacoesAtivas();
			Set<Cliente> clientes = new HashSet<Cliente>();
			
			JsonArrayBuilder clientesBuilder = Json.createArrayBuilder();
			
			for(Alocacao alocacao : alocacoes) {
				Cliente cliente = alocacao.getCliente();
				
				if(!clientes.contains(cliente)) {
					clientes.add(cliente);
					
					// Contruindo o motivo do bloqueio
					JsonObjectBuilder clienteJsonBuilder = Json.createObjectBuilder();
					clienteJsonBuilder.add("id", cliente.getId());
					clienteJsonBuilder.add("nomeFantasia", cliente.getNomeFantasia());
					JsonObject jsonUsuario = clienteJsonBuilder.build();
					
					clientesBuilder.add(jsonUsuario);
				}
			}
			
			JsonArray clientesJson = clientesBuilder.build();
			return Response.status(200).entity(clientesJson.toString()).build();
		} catch(Exception e) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
	}
	
	/**
	 * Retorna a lista de m�quinas alocadas para o cliente requisitado (Login > Gerenciar m�quinas > Cliente)
	 * @param id do cliente
	 * @return m�quinas alocadas para o ID do cliente informado
	 */
	@GET
	@Path("{id}/maquinas")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8") 
	public Response listarSolicitacoesDeAlocacaoPendentes(@PathParam("id") Long id) {
		try {
			List<Alocacao> listAlocacao;
			
			listAlocacao = alocacaoService.findAlocacoesAtivasByCliente(id);
			
			if(listAlocacao == null || listAlocacao.isEmpty()) {
				// N�o existe aloca��es cadastradas no sistema para o cliente solicitado
				return Response.status(403).build(); // HTTP 403 Forbidden
			}
			
			JsonArrayBuilder maquinasBuilder = Json.createArrayBuilder();
			
			for(Alocacao alocacao : listAlocacao) {
				JsonObjectBuilder maquinaJsonBuilder = Json.createObjectBuilder();
				Maquina maquina = alocacao.getMaquina();
				maquinaJsonBuilder.add("id", maquina.getId());
				maquinaJsonBuilder.add("codigo", maquina.getCodigo());
				maquinaJsonBuilder.add("modelo", maquina.getModelo());
				JsonObject jsonMaquina = maquinaJsonBuilder.build();
				
				maquinasBuilder.add(jsonMaquina);
			}
			
			JsonArray maquinas = maquinasBuilder.build();
			return Response.status(200).entity(maquinas.toString()).build();
		} catch(Exception e) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
	}
}
