package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERFIL")
public class Perfil implements Serializable {
	
	private static final long serialVersionUID = -2417211373479105720L;

	@Id
    @Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotNull
	@Column(name = "NOME")
	private String nome;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="PERFIL_PERMISSAO", joinColumns={@JoinColumn(name="PERFIL_ID", referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="PERMISSAO_ID", referencedColumnName="id")})
    private List<Permissao> permissoes;

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

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
