package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERFIL")
public class Perfil implements Serializable {
	
	private static final long serialVersionUID = -2417211373479105720L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
	
	@NotNull
	@Column(name = "NOME")
	private String nome;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name="PERFIL_PERMISSAO", joinColumns={@JoinColumn(name="PERFIL_ID", referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="PERMISSAO_ID", referencedColumnName="id")})
    private Set<Permissao> permissoes;

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

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
