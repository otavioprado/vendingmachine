package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USUARIO_SISTEMA")
@NamedQueries({ 
	@NamedQuery(name = "findByIdUsuarioSistema", query = "SELECT b FROM Usuario b WHERE b.idUsuario = :idUsuarioSistema"),
	@NamedQuery(name = "findUsuariosComEmail", query = "SELECT distinct e.idUsuario FROM Usuario e") 
	})
public class Usuario implements Serializable {

	private static final long serialVersionUID = 4885294130406744926L;

	@Id
	@Column(name = "ID_USUARIO_SISTEMA")
	@GeneratedValue
	private Long idUsuario;

	@NotNull
	@Column(name = "LOGIN", unique = true)
	private String login;

	@NotNull(message="E-mail do usuário não pode ficar em branco.")
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "SOBRENOME")
	private String sobreNome;
	
	@NotNull
	@Column(name = "DATA_CADASTRO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@Column(name = "DATA_BLOQUEIO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataBloqueio;
	
	@Column(name = "DATA_ULT_ACESSO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimoAcesso;
	
	@Column(name = "SENHA_APLICACAO")
	private String senhaAplicacao;
	
	@Column(name = "DATA_ULT_TROCA_SENHA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimaTrocaSenha;
	
	@Column(name = "IND_OBRIGA_TROCA_SENHA")
	private Boolean indObrigaTrocaSenha;
	
	@OneToMany
    private Set<Papel> papeis = new HashSet<Papel>();

	@OneToMany
    private Set<Permissao> permissoes = new HashSet<Permissao>();

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobreNome() {
		return sobreNome;
	}

	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataBloqueio() {
		return dataBloqueio;
	}

	public void setDataBloqueio(Date dataBloqueio) {
		this.dataBloqueio = dataBloqueio;
	}

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public String getSenhaAplicacao() {
		return senhaAplicacao;
	}

	public void setSenhaAplicacao(String senhaAplicacao) {
		this.senhaAplicacao = senhaAplicacao;
	}

	public Date getDataUltimaTrocaSenha() {
		return dataUltimaTrocaSenha;
	}

	public void setDataUltimaTrocaSenha(Date dataUltimaTrocaSenha) {
		this.dataUltimaTrocaSenha = dataUltimaTrocaSenha;
	}

	public Boolean isIndObrigaTrocaSenha() {
		return indObrigaTrocaSenha;
	}

	public void setIndObrigaTrocaSenha(Boolean indObrigaTrocaSenha) {
		this.indObrigaTrocaSenha = indObrigaTrocaSenha;
	}
	
	public Set<Papel> getPapeis() {
		return papeis;
	}

	public void setPapeis(Set<Papel> papeis) {
		this.papeis = papeis;
	}

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
}
