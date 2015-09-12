package br.com.milenio.vendingmachine.webservice;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.service.UsuarioService;
import br.com.milenio.vendingmachine.utils.MD5Util;

@Path("/login")
public class LoginWebService {
	
	@Inject
	private UsuarioService usuarioService;
	
	@GET
	@Path("{login}/{password}")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response loginAplicativo(@PathParam("login") String login, @PathParam("password") String password) {
		
		UsuarioSistema usuario = usuarioService.findByLogin(login);
		
		if(usuario == null) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
			
		String senhaCriptografada = MD5Util.criptografar(password);
			
		if(!usuario.getSenhaAplicacao().equals(senhaCriptografada)) {
			return Response.serverError().build(); // HTTP 500 - Internal server error
		}
		
		// Verifica se o usuário está bloqueado
		if(usuario.getIndAtivo() == false) {
			// Criando um Objeto JSON com Modelo de Objetos:
			 
			// Contruindo o motivo do bloqueio
			JsonObjectBuilder usuarioJsonBuilder = Json.createObjectBuilder();
			usuarioJsonBuilder.add("motivo-bloqueio", usuario.getMotivoBloqueio());
			JsonObject jsonUsuario = usuarioJsonBuilder.build();
			
			return Response.serverError().entity(jsonUsuario.toString()).build(); // HTTP 500 - Internal server error
		}
		
		return Response.ok().build();
	}
}
