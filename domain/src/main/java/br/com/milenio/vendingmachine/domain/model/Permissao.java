package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERMISSAO")
public class Permissao implements Serializable {
	
	private static final long serialVersionUID = 2158027208658254637L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(name = "NOME")
	private String nome;
	
	@NotNull
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@ManyToOne(fetch = FetchType.EAGER)  
	@JoinColumn(name = "DEPENDENCIA_ID", nullable = true)
	private Permissao dependencia;
	
	@NotNull
	@Column(name = "IND_ATRIB_ANY_PERFIL")
	private Boolean indAtribAnyPerfil;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Permissao getDependencia() {
		return dependencia;
	}

	public void setDependencia(Permissao dependencia) {
		this.dependencia = dependencia;
	}
	
	public Boolean getIndAtribAnyPerfil() {
		return indAtribAnyPerfil;
	}

	public void setIndAtribAnyPerfil(Boolean indAtribAnyPerfil) {
		this.indAtribAnyPerfil = indAtribAnyPerfil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dependencia == null) ? 0 : dependencia.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((indAtribAnyPerfil == null) ? 0 : indAtribAnyPerfil.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Permissao other = (Permissao) obj;
		if (dependencia == null) {
			if (other.dependencia != null)
				return false;
		} else if (!dependencia.equals(other.dependencia))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (indAtribAnyPerfil == null) {
			if (other.indAtribAnyPerfil != null)
				return false;
		} else if (!indAtribAnyPerfil.equals(other.indAtribAnyPerfil))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}

