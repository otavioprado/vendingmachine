package br.com.milenio.vendingmachine.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Endereco;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.EnderecoRepository;

@Stateless
public class EnderecoServiceBean implements EnderecoService {
	
	private static final Logger LOGGER = LogManager.getLogger(EnderecoServiceBean.class);
	
	@EJB
	EnderecoRepository enderecoRepository;
	
	public Endereco consultarCepWebService(String cep) throws InconsistenciaException {
		
		if(cep == null || cep.isEmpty()) {
			throw new InconsistenciaException("O cep informado não pode ser vázio.");
		}
		
		StringBuilder sb = new StringBuilder("http://viacep.com.br/ws/");
		sb.append(cep);
		sb.append("/json/");
		String url = sb.toString();
		
		URL obj;
		HttpURLConnection con;
		
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			
			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			JsonReader leitorDeObjeto = Json.createReader(con.getInputStream());
			
			// Faz a leitura e retorna o nosso objeto
			JsonObject livroJson = leitorDeObjeto.readObject();
			
			Endereco endereco = new Endereco();
			
			//Ajusta os valores, lendo a informação do JSON.
			endereco.setCep(livroJson.getString("cep"));
			endereco.setLogradouro(livroJson.getString("logradouro"));
			endereco.setComplemento(livroJson.getString("complemento"));
			endereco.setBairro(livroJson.getString("bairro"));
			endereco.setCidade(livroJson.getString("localidade"));
			endereco.setEstado(livroJson.getString("uf"));
			
			return endereco;
		} catch (IOException | NullPointerException e) {
			LOGGER.error(e.getStackTrace());
			
			return null;
		}
	}

	@Override
	public void cadastrar(Endereco endereco) {
		enderecoRepository.persist(endereco);
	}
}
