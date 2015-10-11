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
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.service.AlocacaoService;

@Path("/alocacoes")
public class AlocacaoWebService {
	
	@Inject
	private AlocacaoService alocacaoService;
	
	/**
	 * @return lista de m�quinas pendentes de aloca��o.
	 */
	@GET
	@Path("listarpendentes")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response listarMaquinasPendentesAlocacao() {
		List<Alocacao> alocacoesPendenteAlocacao = alocacaoService.findAlocacoesPendentesAlocacao();
		
		if(alocacoesPendenteAlocacao == null || alocacoesPendenteAlocacao.isEmpty()) {
			// N�o existe nenhuma aloca��o pendente
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
		
		JsonArrayBuilder produtoBuilder = Json.createArrayBuilder();
		
		for(Alocacao alocacao : alocacoesPendenteAlocacao) {
			Maquina maquina = alocacao.getMaquina();
			
			JsonObjectBuilder maquinaJsonBuilder = Json.createObjectBuilder();
			maquinaJsonBuilder.add("id", maquina.getId());
			maquinaJsonBuilder.add("codigo", maquina.getCodigo());
			maquinaJsonBuilder.add("modelo", maquina.getModelo());

			JsonObject jsonMaquina = maquinaJsonBuilder.build();
			
			produtoBuilder.add(jsonMaquina);
		}
		
		JsonArray produtos = produtoBuilder.build();
		return Response.status(200).entity(produtos.toString()).build();
	}
}
