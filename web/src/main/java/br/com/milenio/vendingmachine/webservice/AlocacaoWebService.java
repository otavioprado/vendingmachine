package br.com.milenio.vendingmachine.webservice;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	 * Consultar solicita��o de aloca��o
	 * @return lista de m�quinas pendentes de aloca��o.
	 */
	@GET
	@Path("listarpendentes")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response listarMaquinasPendentesAlocacao() {
		try{
			List<Alocacao> alocacoesPendenteAlocacao = alocacaoService.findAlocacoesPendentesAlocacao();
			
			if(alocacoesPendenteAlocacao == null || alocacoesPendenteAlocacao.isEmpty()) {
				// N�o existe nenhuma aloca��o pendente
				return Response.serverError().build(); // HTTP 500 - Internal server error
			}
			
			JsonArrayBuilder alocacoesBuilder = Json.createArrayBuilder();
			
			for(Alocacao alocacao : alocacoesPendenteAlocacao) {
				// Cria o objeto JSON de m�quina
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
				clienteJsonBuilder.add("telefone", cliente.getTelefoneFixo());
				clienteJsonBuilder.add("logradouro", cliente.getEndereco().getLogradouro());
				clienteJsonBuilder.add("numero", cliente.getEndereco().getNumero());
				JsonObject jsonCliente = clienteJsonBuilder.build(); 
				
				// Cria o objeto JSON de aloca��o e vincula os objetos m�quina e cliente
				JsonObjectBuilder alocacaoJsonBuilder = Json.createObjectBuilder();
				alocacaoJsonBuilder.add("alocacaoId", alocacao.getId());
				alocacaoJsonBuilder.add("dataSolicitacao", new SimpleDateFormat("dd/MM/yyyy").format(alocacao.getDataCadastroAlocacao()));
				alocacaoJsonBuilder.add("maquina", jsonMaquina);
				alocacaoJsonBuilder.add("cliente", jsonCliente);
				JsonObject jsonAlocacao = alocacaoJsonBuilder.build();
				
				// Adicionar ao array de aloca��es o objeto aloca��o criado
				alocacoesBuilder.add(jsonAlocacao);
			}
			
			JsonArray alocacoes = alocacoesBuilder.build();
			return Response.status(200).entity(alocacoes.toString()).build();
		} catch (Exception e) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
	}
	
	/**
	 * Confirma a aloca��o da m�quina
	 */
	@POST
	@Path("{id}/alocar")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response realizarAlocacaoMaquina(@PathParam("id") Long id) {
		try{
			Alocacao alocacao = alocacaoService.findById(id);
			alocacaoService.alocar(alocacao);
			return Response.status(200).build();
		} catch (Exception e) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
	}
}
