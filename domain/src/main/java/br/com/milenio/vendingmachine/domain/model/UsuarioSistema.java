package br.com.milenio.vendingmachine.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
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
	@NamedQuery(name = "findByIdUsuarioSistema", query = "SELECT b FROM UsuarioSistema b WHERE b.id = :idUsuarioSistema"),
	@NamedQuery(name = "findUsuariosComEmail", query = "SELECT distinct e.id FROM UsuarioSistema e") 
	})
public class UsuarioSistema implements Serializable {

	private static final long serialVersionUID = -6030429686620788418L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "LOGIN", unique = true)
	private String login;
	
	@Column(name = "SENHA_APLICACAO")
	private String senhaAplicacao;

	@NotNull(message="E-mail do usuário não pode ficar em branco.")
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "NOME")
	private String nome;
	
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
	
	@Column(name = "DATA_ULT_TROCA_SENHA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimaTrocaSenha;
	
	@Column(name = "IND_ATIVO")
	private Boolean indAtivo;
	
	@ManyToOne
	@JoinTable(name="USUARIO_SISTEMA_PERFIL", joinColumns={@JoinColumn(name="USUARIO_SISTEMA_ID", referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="PERFIL_ID", referencedColumnName="id")})
    private Perfil perfil;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenhaAplicacao() {
		return senhaAplicacao;
	}

	public void setSenhaAplicacao(String senhaAplicacao) {
		this.senhaAplicacao = senhaAplicacao;
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

	public Date getDataUltimaTrocaSenha() {
		return dataUltimaTrocaSenha;
	}

	public void setDataUltimaTrocaSenha(Date dataUltimaTrocaSenha) {
		this.dataUltimaTrocaSenha = dataUltimaTrocaSenha;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
