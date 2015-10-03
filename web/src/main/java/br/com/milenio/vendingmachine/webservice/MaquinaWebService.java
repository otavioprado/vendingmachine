package br.com.milenio.vendingmachine.webservice;

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

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.Produto;
import br.com.milenio.vendingmachine.service.MaquinaService;

@Path("/maquinas")
public class MaquinaWebService {
	
	@Inject
	private MaquinaService maquinaService;
	
	/**
	 * @param id da m�quina
	 * @return lista de produtos que podem ser alocados para uma m�quina espec�fica.
	 */
	@GET
	@Path("{id}/produtos")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response listarProdutosQuePodemSerAlocados(@PathParam("id") Long id) {
		Maquina maquina = maquinaService.findById(id);
		
		if(maquina == null) {
			// N�o existe uma m�quina com o ID informado
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
		
		JsonArrayBuilder produtoBuilder = Json.createArrayBuilder();
		
		for(Produto produto : maquina.getProdutos()) {
			JsonObjectBuilder maquinaJsonBuilder = Json.createObjectBuilder();
			maquinaJsonBuilder.add("id", produto.getId());
			maquinaJsonBuilder.add("codigo", produto.getCodigo());
			maquinaJsonBuilder.add("descricao", produto.getDescricao());
			maquinaJsonBuilder.add("valorUnitario", produto.getValorUnitario());
			maquinaJsonBuilder.add("precoVenda", produto.getPrecoVenda());
			JsonObject jsonMaquina = maquinaJsonBuilder.build();
			
			produtoBuilder.add(jsonMaquina);
		}
		
		JsonArray produtos = produtoBuilder.build();
		return Response.status(200).entity(produtos.toString()).build();
	}
}
