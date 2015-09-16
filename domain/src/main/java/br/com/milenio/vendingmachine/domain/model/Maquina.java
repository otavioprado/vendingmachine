package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MAQUINA")
public class Maquina implements Serializable {
	private static final long serialVersionUID = -1740798416648579794L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "MODELO")
	private String modelo;
	
	@Column(name = "QTD_MAX_TIPO_PRODUTOS")
	private Integer qtdMaxTipoProdutos;
	
	@Column(name = "CUSTO_AQUISICAO")
	private Double custoAquisicao;

	@Column(name = "GARANTIA")
	private Integer garantia;
	
	@Column(name= "DATA_AQUISICAO")
	@Temporal(value=TemporalType.DATE)
	private Date dataAquisicao;
	
	@ManyToOne
	@JoinColumn(name= "FORNECEDOR_ID")
	private Fornecedor fornecedor = new Fornecedor();
	
	@ManyToOne
	@JoinColumn(name= "MAQUINA_STATUS_ID")
	private MaquinaStatus maquinaStatus = new MaquinaStatus();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="MAQUINA_PRODUTO", joinColumns={@JoinColumn(name="MAQUINA_ID", referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="PRODUTO_ID", referencedColumnName="id")})
	private List<Produto> produtos = new ArrayList<Produto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getQtdMaxTipoProdutos() {
		return qtdMaxTipoProdutos;
	}

	public void setQtdMaxTipoProdutos(Integer qtdMaxTipoProdutos) {
		this.qtdMaxTipoProdutos = qtdMaxTipoProdutos;
	}

	public Double getCustoAquisicao() {
		return custoAquisicao;
	}

	public void setCustoAquisicao(Double custoAquisicao) {
		this.custoAquisicao = custoAquisicao;
	}

	public Integer getGarantia() {
		return garantia;
	}

	public void setGarantia(Integer garantia) {
		this.garantia = garantia;
	}

	public Date getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(Date dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public MaquinaStatus getMaquinaStatus() {
		return maquinaStatus;
	}

	public void setMaquinaStatus(MaquinaStatus maquinaStatus) {
		this.maquinaStatus = maquinaStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((custoAquisicao == null) ? 0 : custoAquisicao.hashCode());
		result = prime * result + ((dataAquisicao == null) ? 0 : dataAquisicao.hashCode());
		result = prime * result + ((garantia == null) ? 0 : garantia.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		result = prime * result + ((produtos == null) ? 0 : produtos.hashCode());
		result = prime * result + ((qtdMaxTipoProdutos == null) ? 0 : qtdMaxTipoProdutos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Maquina other = (Maquina) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (custoAquisicao == null) {
			if (other.custoAquisicao != null)
				return false;
		} else if (!custoAquisicao.equals(other.custoAquisicao))
			return false;
		if (dataAquisicao == null) {
			if (other.dataAquisicao != null)
				return false;
		} else if (!dataAquisicao.equals(other.dataAquisicao))
			return false;
		if (garantia == null) {
			if (other.garantia != null)
				return false;
		} else if (!garantia.equals(other.garantia))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modelo == null) {
			if (other.modelo != null)
				return false;
		} else if (!modelo.equals(other.modelo))
			return false;
		if (produtos == null) {
			if (other.produtos != null)
				return false;
		} else if (!produtos.equals(other.produtos))
			return false;
		if (qtdMaxTipoProdutos == null) {
			if (other.qtdMaxTipoProdutos != null)
				return false;
		} else if (!qtdMaxTipoProdutos.equals(other.qtdMaxTipoProdutos))
			return false;
		return true;
	}
}
