package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRATO")
public class Contrato implements Serializable {
	private static final long serialVersionUID = -1740798416648579794L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "MODALIDADE")
	private String modalidade;
	
	@Column(name = "VALOR_ALUGUEL")
	private Double valorAluguel;
	
	@Column(name = "PORCENTAGEM")
	private Double valorPorcentagem;
	
	@Column(name = "IND_DISPONIVEL")
	private Boolean indDisponivel;

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

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public Boolean getIndDisponivel() {
		return indDisponivel;
	}

	public void setIndDisponivel(Boolean indDisponivel) {
		this.indDisponivel = indDisponivel;
	}

	public Double getValorAluguel() {
		return valorAluguel;
	}

	public void setValorAluguel(Double valorAluguel) {
		this.valorAluguel = valorAluguel;
	}

	public Double getValorPorcentagem() {
		return valorPorcentagem;
	}

	public void setValorPorcentagem(Double valorPorcentagem) {
		this.valorPorcentagem = valorPorcentagem;
	}
}
