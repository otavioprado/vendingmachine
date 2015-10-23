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
import br.com.milenio.vendingmachine.service.MaquinaService;

@Path("/desalocacoes")
public class DesalocacaoWebService {
	
	@Inject
	private AlocacaoService alocacaoService;
	
	@Inject
	private MaquinaService maquinaService;
	
	/**
	 * Consultar solicitação de desalocação pendentes
	 * @return lista de máquinas pendentes de desalocação.
	 */
	@GET
	@Path("listarpendentes")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response listarMaquinasPendentesDesalocacao() {
		try{
			List<Alocacao> alocacoesPendenteDesalocacao = alocacaoService.findAlocacoesPendentesDesalocacao();
			
			if(alocacoesPendenteDesalocacao == null || alocacoesPendenteDesalocacao.isEmpty()) {
				// Não existe nenhuma desalocação pendente
				return Response.status(403).build(); // HTTP 403 Forbidden
			}
			
			JsonArrayBuilder alocacoesBuilder = Json.createArrayBuilder();
			
			for(Alocacao alocacao : alocacoesPendenteDesalocacao) {
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
				clienteJsonBuilder.add("telefone", cliente.getTelefoneFixo());
				clienteJsonBuilder.add("logradouro", cliente.getEndereco().getLogradouro());
				clienteJsonBuilder.add("numero", cliente.getEndereco().getNumero());
				JsonObject jsonCliente = clienteJsonBuilder.build(); 
				
				// Cria o objeto JSON de alocação e vincula os objetos máquina e cliente
				JsonObjectBuilder alocacaoJsonBuilder = Json.createObjectBuilder();
				alocacaoJsonBuilder.add("alocacaoId", alocacao.getId());
				alocacaoJsonBuilder.add("dataSolicitacao", new SimpleDateFormat("dd/MM/yyyy").format(alocacao.getDataCadastroDesalocacao()));
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
	
	/**
	 * Confirma a desalocação da máquina
	 */
	@POST
	@Path("{id}/desalocar")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response realizarAlocacaoMaquina(@PathParam("id") Long id) {
		try{
			Alocacao alocacao = alocacaoService.findById(id);
			alocacaoService.desalocar(alocacao);
			return Response.status(200).build();
		} catch (Exception e) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
	}
	
	/**
	 * Realiza o recolhimento direto da máquina, sem a necessidade de uma solicitação de desalocação
	 */
	@POST
	@Path("{id}/recolher")
	public Response realizarRecolhimentoMaquina(@PathParam("id") Long id) {
		try {
			Maquina maquina = maquinaService.findById(id);
			Alocacao alocacao = alocacaoService.findAlocacaoAtualmenteAtivaParaUmaMaquina(maquina);
			alocacaoService.solicitarDesalocacao(alocacao);
			alocacaoService.desalocar(alocacao);
			return Response.status(200).build();
		} catch (Exception e) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
	}
}
