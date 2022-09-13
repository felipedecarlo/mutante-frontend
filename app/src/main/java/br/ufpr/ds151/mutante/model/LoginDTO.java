package br.ufpr.ds151.mutante.model;

import java.io.Serializable;

public class LoginDTO implements Serializable{

	private String login;
	private String senha;
	
	public LoginDTO() {
		super();
	}
	
	public LoginDTO(String login, String senha) {
		super();
		this.login = login;
		this.senha = senha;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
		
}
