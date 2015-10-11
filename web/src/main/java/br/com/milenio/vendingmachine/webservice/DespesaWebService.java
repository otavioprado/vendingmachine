package br.com.milenio.vendingmachine.webservice;

import java.io.Reader;
import java.io.StringReader;
import java.util.Date;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.milenio.vendingmachine.domain.model.Despesa;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.NaturezaFinanceira;
import br.com.milenio.vendingmachine.service.DespesaService;
import br.com.milenio.vendingmachine.service.MaquinaService;
import br.com.milenio.vendingmachine.service.NaturezaFinanceiraService;
import br.com.milenio.vendingmachine.util.Constants;

@Path("/despesas")
public class DespesaWebService {
	
	@Inject
	private DespesaService despesaService;
	
	@Inject
	private MaquinaService maquinaService;
	
	@Inject
	private NaturezaFinanceiraService naturezaFinanceiraService;
	
	/**
	 * Abrir máquina
	 */
	@PUT
	@Path("cadastrar/{idMaquina}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response cadastrarDespesa(@PathParam("idMaquina") Long idMaquina, String jsonRequest) {
		Despesa despesa = new Despesa();
		Double totalDespesa = 0D;
		
		if(jsonRequest == null || jsonRequest.isEmpty()) {
			// Não existe nenhum parâmetro informado
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
		
		Reader reader = new StringReader(jsonRequest);
		JsonReader leitorDeObjeto = Json.createReader(reader);
		JsonArray readArray = leitorDeObjeto.readArray();
		
		for(int i = 0; i < readArray.size(); i++) {
			JsonNumber jsonNumber = null;
			JsonObject jsonObject = readArray.getJsonObject(i);
			
			jsonNumber = jsonObject.getJsonNumber("valorTotal");
			totalDespesa += jsonNumber.doubleValue();
		}

		try {
			NaturezaFinanceira naturezaFinanceira = naturezaFinanceiraService.findByDescricao(Constants.ABASTECIMENTO);
			Maquina maquina = maquinaService.findById(idMaquina);
			
			despesa.setValor(totalDespesa);
			despesa.setData(new Date());
			despesa.setMaquina(maquina);
			despesa.setNaturezaFinanceira(naturezaFinanceira);
			
			despesaService.cadastrar(despesa);
		} catch (Exception e) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
		
		return Response.ok().build(); // HTTP 200
	}
	
	public static void main(String[] args) {
		DespesaWebService teste = new DespesaWebService();
		teste.cadastrarDespesa(1L, "[{\"codigo\":\"P123\",\"descricao\":\"Coca-cola\",\"id\":1,\"precoVenda\":5.0,\"quantidade\":5,\"valorTotal\":25.0,\"valorUnitario\":4.0},{\"codigo\":\"P456\",\"descricao\":\"Pepsi\",\"id\":2,\"precoVenda\":4.0,\"quantidade\":10,\"valorTotal\":40.0,\"valorUnitario\":3.5}]");
		// [{"codigo":"P123","descricao":"Coca-cola","id":1,"precoVenda":5.0,"quantidade":5,"valorTotal":25.0,"valorUnitario":4.0},{"codigo":"P456","descricao":"Pepsi","id":2,"precoVenda":4.0,"quantidade":10,"valorTotal":40.0,"valorUnitario":3.5}]
	}
}
