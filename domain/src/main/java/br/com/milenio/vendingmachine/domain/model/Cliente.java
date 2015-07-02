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
@Table(name = "CLIENTE")
class Cliente implements Serializable {
	
	@Id
	@Column(name = "ID_CLIENTE")
	@GeneratedValue
	private Long idCliente;
	
	private String razaoSocial;
	
	@Column(name = "NOME_FANTASIA")
	private String nomeFantasia;
	
	@Column(name = "CPF_CNPJ")
	private String cpfCnpj;
	
	@Column(name = "ATIVIDADE")
	private String atividade;
	
	@Column(name = "CONTATO")
	private String contato;
	
	@Column(name = "TELEFONE")
	private String telefone;
	
	@Column(name = "CELULAR")
	private String celular;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CLIENTE_DESDE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date clienteDesde;
	
	@Column(name = "DETALHES_INFORMATIVOS")
	private String detalhesInformativos;
	
	@Column(name = "SITE")
	private String site;
	
	@Column(name = "REPRESENTANTE_COMERCIAL")
	private String representanteComercial;
	
	

}
