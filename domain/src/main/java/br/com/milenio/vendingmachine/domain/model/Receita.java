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
@Table(name = "RECEITA")
public class Receita implements Serializable {
	private static final long serialVersionUID = 987099529298319724L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "VALOR")
	private Double valor;
	
	@ManyToOne
	@JoinColumn(name= "NATUREZA_FINANCEIRA_ID")
	private NaturezaFinanceira naturezaFinanceira = new NaturezaFinanceira();
	
	@Column(name= "DATA")
	@Temporal(value=TemporalType.DATE)
	private Date data;
	
	@ManyToOne
	@JoinColumn(name= "MAQUINA_ID")
	private Maquina maquina = new Maquina();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public NaturezaFinanceira getNaturezaFinanceira() {
		return naturezaFinanceira;
	}

	public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
		this.naturezaFinanceira = naturezaFinanceira;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}
}
