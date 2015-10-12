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

import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.service.AlocacaoService;

@Path("/alocacoes")
public class AlocacaoWebService {
	
	@Inject
	private AlocacaoService alocacaoService;
	
	/**
	 * Consultar solicitação de alocação
	 * @return lista de máquinas pendentes de alocação.
	 */
	@GET
	@Path("listarpendentes")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response listarMaquinasPendentesAlocacao() {
		try{
			List<Alocacao> alocacoesPendenteAlocacao = alocacaoService.findAlocacoesPendentesAlocacao();
			
			if(alocacoesPendenteAlocacao == null || alocacoesPendenteAlocacao.isEmpty()) {
				// Não existe nenhuma alocação pendente
				return Response.serverError().build(); // HTTP 500 - Internal server error
			}
			
			JsonArrayBuilder alocacoesBuilder = Json.createArrayBuilder();
			
			for(Alocacao alocacao : alocacoesPendenteAlocacao) {
				// Cria o objeto JSON de máquina
				Maquina maquina = alocacao.getMaquina();
				JsonObjectBuilder maquinaJsonBuilder = Json.createObjectBuilder();
				maquinaJsonBuilder.add("id", maquina.getId());
				maquinaJsonBuilder.add("codigo", maquina.getCodigo());
				maquinaJsonBuilder.add("modelo", maquina.getModelo());
				JsonObject jsonMaquina = maquinaJsonBuilder.build();
				
				// Cria o objeto JSON de cliente
				Cliente cliente = alocacao.getCliente();
				JsonObjectBuilder clienteJsonBuilder = Json.createObjectBuilder();
				clienteJsonBuilder.add("id", cliente.getId());
				clienteJsonBuilder.add("nomeFantasia", cliente.getNomeFantasia());
				JsonObject jsonCliente = clienteJsonBuilder.build();
				
				// Cria o objeto JSON de alocação e vincula os objetos máquina e cliente
				JsonObjectBuilder alocacaoJsonBuilder = Json.createObjectBuilder();
				alocacaoJsonBuilder.add("maquina", jsonMaquina);
				alocacaoJsonBuilder.add("cliente", jsonCliente);
				JsonObject jsonAlocacao = alocacaoJsonBuilder.build();
				
				// Adicionar ao array de alocações o objeto alocação criado
				alocacoesBuilder.add(jsonAlocacao);
			}
			
			JsonArray alocacoes = alocacoesBuilder.build();
			return Response.status(200).entity(alocacoes.toString()).build();
		} catch (Exception e) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
	}
}
