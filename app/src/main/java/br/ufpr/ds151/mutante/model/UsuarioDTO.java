package br.ufpr.ds151.mutante.model;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {

	private static final Long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String senha;

	public UsuarioDTO() {
		super();
	}
	

	public UsuarioDTO(Long id, String nome, String senha) {
		super();
		this.id = id;
		this.nome = nome;
		this.senha = senha;
	}


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


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
