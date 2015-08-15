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
@Table(name = "AUDITORIA")
public class Auditoria implements Serializable {
	private static final long serialVersionUID = -922944240622308391L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne
	@JoinColumn(name= "USUARIO_ID")
	private UsuarioSistema usuario;
	
	@Column(name = "TITULO")
	private String titulo;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "DATA_ACAO")
	@Temporal(value=TemporalType.DATE)
	private Date dataAcao;
	
	@Column(name = "HORARIO_ACAO")
	@Temporal(value=TemporalType.TIME)
	private Date horarioAcao;

	@Column(name = "IP")
	private String ip;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataAcao() {
		return dataAcao;
	}

	public void setDataAcao(Date dataAcao) {
		this.dataAcao = dataAcao;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Date getHorarioAcao() {
		return horarioAcao;
	}

	public void setHorarioAcao(Date horarioAcao) {
		this.horarioAcao = horarioAcao;
	}
}
