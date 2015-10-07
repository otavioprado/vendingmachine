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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "HISTORICO_MAQUINA")
public class HistoricoMaquina implements Serializable {
	private static final long serialVersionUID = -1740798416648579794L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name= "DATA")
	@Temporal(value=TemporalType.DATE)
	private Date data;
	
	@Column(name= "STATUS")
	private String status;
	
	@ManyToOne
	@JoinColumn(name= "USUARIO_SISTEMA_ID")
	private UsuarioSistema usuarioSistema = new UsuarioSistema();
	
	@ManyToOne
	@JoinColumn(name= "MAQUINA_ID")
	private Maquina maquina = new Maquina();
	
	@ManyToOne
	@JoinColumn(name= "CLIENTE_ID")
	private Cliente cliente = new Cliente();
	
	public HistoricoMaquina() {
		super();
	}

	public HistoricoMaquina(Date data, String status, UsuarioSistema usuarioSistema, Maquina maquina, Cliente cliente) {
		super();
		this.data = data;
		this.status = status;
		this.usuarioSistema = usuarioSistema;
		this.maquina = maquina;
		this.cliente = cliente;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UsuarioSistema getUsuarioSistema() {
		return usuarioSistema;
	}

	public void setUsuarioSistema(UsuarioSistema usuarioSistema) {
		this.usuarioSistema = usuarioSistema;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
