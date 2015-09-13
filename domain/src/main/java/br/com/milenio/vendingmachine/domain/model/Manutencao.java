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
@Table(name = "MANUTENCAO")
public class Manutencao implements Serializable {
	private static final long serialVersionUID = -1740798416648579794L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name= "DATA_CADASTRO")
	@Temporal(value=TemporalType.DATE)
	private Date dataCadastro;
	
	@Column(name= "DATA_RETORNO")
	@Temporal(value=TemporalType.DATE)
	private Date dataRetorno;
	
	@Column(name= "CUSTO")
	private Double custo;
	
	@Column(name= "DESCRICAO")
	private String descricao;
	
	@Column(name= "MOTIVO")
	private String motivo;
	
	@ManyToOne
	@JoinColumn(name= "MAQUINA_ID")
	private Maquina maquina = new Maquina();
	
	@ManyToOne
	@JoinColumn(name= "FORNECEDOR_ID")
	private Fornecedor fornecedor = new Fornecedor();
	
	@Column(name= "IND_EFETIVADO")
	private Boolean indEfetivado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataRetorno() {
		return dataRetorno;
	}

	public void setDataRetorno(Date dataRetorno) {
		this.dataRetorno = dataRetorno;
	}

	public Double getCusto() {
		return custo;
	}

	public void setCusto(Double custo) {
		this.custo = custo;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getIndEfetivado() {
		return indEfetivado;
	}

	public void setIndEfetivado(Boolean indEfetivado) {
		this.indEfetivado = indEfetivado;
	}
}
