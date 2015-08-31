package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUTO")
public class Produto implements Serializable {
	private static final long serialVersionUID = 987099529298319724L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "VALOR_UNITARIO")
	private Double valorUnitario;
	
	@Column(name= "PRECO_VENDA")
	private Double precoVenda;
	
	@Column(name = "IND_PERECIVEL")
	private Boolean indPerecivel;
	
	@ManyToOne
	@JoinColumn(name= "FORNECEDOR_ID")
	private Fornecedor fornecedor = new Fornecedor();

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Boolean getIndPerecivel() {
		return indPerecivel;
	}

	public void setIndPerecivel(Boolean indPerecivel) {
		this.indPerecivel = indPerecivel;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
}
