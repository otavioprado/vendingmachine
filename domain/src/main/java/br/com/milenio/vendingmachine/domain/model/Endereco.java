package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ENDERECO")
class Endereco implements Serializable {
	
	@Id
	@Column(name = "ID_ENDERECO")
	@GeneratedValue
	private Long idEndereco;
	
	@Column(name = "RUA")
	private String rua;
	
	@Column(name = "NUMERO")
	private Long numero;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "CEP")
	private Long cep;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "CIDADE")
	private String cidade;
	
	@Column(name = "BAIRRO")
	private String bairro;	
}
