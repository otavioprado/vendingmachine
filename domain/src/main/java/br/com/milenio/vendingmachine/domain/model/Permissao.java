package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERMISSAO")
public class Permissao implements Serializable {
	private static final long serialVersionUID = 5202879643892770369L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@NotNull
	private String nome;
	
	@NotNull
	private String dominios;
	
	@NotNull
	private String operacoes;
	
	@Version
	@Column(name = "version")
	private Integer version;
	
	public Set<String> getDominiosSet(){
    	if(dominios == null){
    		return Collections.emptySet();
    	}
    	return new HashSet<String>(Arrays.asList(dominios.split(",")));
    }
	
	public void setDominiosSet(Set<String> dominiosSet){
    	StringBuilder stringBuilder = new StringBuilder();
		for(String dominio : dominiosSet){
			stringBuilder.append(dominio.toString());
			stringBuilder.append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		dominios = stringBuilder.toString();
    }
	
	public Set<String> getOperacoesSet(){
    	if(operacoes == null){
    		return Collections.emptySet();
    	}
    	return new HashSet<String>(Arrays.asList(operacoes.split(",")));
    }
	
	public void setOperacoesSet(Set<String> operacoesSet){
    	StringBuilder stringBuilder = new StringBuilder();
		for(String operacao : operacoesSet){
			stringBuilder.append(operacao.toString());
			stringBuilder.append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		operacoes = stringBuilder.toString();
    }
	
	public String getStringPermissao(){
    	return String.format("%s:%s", dominios, operacoes);
    }
    
	@Override
	public String toString() {
		return nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDominios() {
		return dominios;
	}

	public void setDominios(String dominios) {
		this.dominios = dominios;
	}

	public String getOperacoes() {
		return operacoes;
	}

	public void setOperacoes(String operacoes) {
		this.operacoes = operacoes;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}