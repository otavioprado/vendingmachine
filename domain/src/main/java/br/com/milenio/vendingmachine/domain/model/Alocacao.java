package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ALOCACAO")
public class Alocacao implements Serializable {
	private static final long serialVersionUID = -1740798416648579794L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name= "DATA_SOLICITACAO_ALOCACAO")
	@Temporal(value=TemporalType.DATE)
	private Date dataCadastroAlocacao;
	
	@Column(name= "DATA_ALOCACAO")
	@Temporal(value=TemporalType.DATE)
	private Date dataAlocacao;
	
	@Column(name= "DATA_SOLICITACAO_DESALOCACAO")
	@Temporal(value=TemporalType.DATE)
	private Date dataCadastroDesalocacao;
	
	@Column(name= "DATA_DESALOCACAO")
	@Temporal(value=TemporalType.DATE)
	private Date dataDesalocacao;
	
	@ManyToOne
	@JoinColumn(name= "CLIENTE_ID")
	private Cliente cliente = new Cliente();
	
	@ManyToOne
	@JoinColumn(name= "MAQUINA_ID")
	private Maquina maquina = new Maquina();
	
	@ManyToOne
	@JoinColumn(name= "USUARIO_ID")
	private UsuarioSistema usuario = new UsuarioSistema();
	
	@OneToOne
	@JoinColumn(name= "CONTRATO_ID")
	private Contrato contrato = new Contrato();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataAlocacao() {
		return dataAlocacao;
	}

	public void setDataAlocacao(Date dataAlocacao) {
		this.dataAlocacao = dataAlocacao;
	}

	public Date getDataDesalocacao() {
		return dataDesalocacao;
	}

	public void setDataDesalocacao(Date dataDesalocacao) {
		this.dataDesalocacao = dataDesalocacao;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public Date getDataCadastroAlocacao() {
		return dataCadastroAlocacao;
	}

	public void setDataCadastroAlocacao(Date dataCadastroAlocacao) {
		this.dataCadastroAlocacao = dataCadastroAlocacao;
	}

	public Date getDataCadastroDesalocacao() {
		return dataCadastroDesalocacao;
	}

	public void setDataCadastroDesalocacao(Date dataCadastroDesalocacao) {
		this.dataCadastroDesalocacao = dataCadastroDesalocacao;
	}
}
