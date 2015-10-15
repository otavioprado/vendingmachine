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
	 * @param id da máquina
	 * @return lista de produtos que podem ser alocados para uma máquina específica.
	 */
	@GET
	@Path("{id}/produtos")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response listarProdutosQuePodemSerAlocados(@PathParam("id") Long id) {
		Maquina maquina = maquinaService.findById(id);
		
		if(maquina == null) {
			// Não existe uma máquina com o ID informado
			return Response.status(403).build(); // HTTP 403 Forbidden
		}
		
		JsonArrayBuilder produtoBuilder = Json.createArrayBuilder();
		
		for(Produto produto : maquina.getProdutos()) {
			JsonObjectBuilder produtoJsonBuilder = Json.createObjectBuilder();
			produtoJsonBuilder.add("id", produto.getId());
			produtoJsonBuilder.add("codigo", produto.getCodigo());
			produtoJsonBuilder.add("descricao", produto.getDescricao());
			produtoJsonBuilder.add("valorUnitario", produto.getValorUnitario());
			produtoJsonBuilder.add("precoVenda", produto.getPrecoVenda());
			JsonObject jsonProduto = produtoJsonBuilder.build();
			
			produtoBuilder.add(jsonProduto);
		}
		
		JsonArray produtos = produtoBuilder.build();
		return Response.status(200).entity(produtos.toString()).build();
	}
}
