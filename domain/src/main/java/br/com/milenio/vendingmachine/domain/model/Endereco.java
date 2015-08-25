package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ENDERECO")
public class Endereco implements Serializable {
	private static final long serialVersionUID = -6971492763759754776L;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;
	
	@Column(name = "LOGRADOURO")
	private String logradouro;
	
	@Column(name = "NUMERO")
	private Long numero;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "CEP")
	private String cep;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "CIDADE")
	private String cidade;
	
	@Column(name = "BAIRRO")
	private String bairro;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
}
