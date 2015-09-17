package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NATUREZA_FINANCEIRA")
public class NaturezaFinanceira implements Serializable{

	private static final long serialVersionUID = -6784242152523899661L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "TIPO_NATUREZA_FINANCEIRA")
	private String tipoNaturezaFinanceira;
	
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
	public String getTipoNaturezaFinanceira() {
		return tipoNaturezaFinanceira;
	}
	public void setTipoNaturezaFinanceira(String tipoNaturezaFinanceira) {
		this.tipoNaturezaFinanceira = tipoNaturezaFinanceira;
	}

}
