package br.ufpr.ds151.mutante.model;

import java.io.Serializable;

public class HabilidadeDTO implements Serializable{

	private Long id;
	private String descricao;
	private Long idMutante;
	
	public HabilidadeDTO() {
		super();
	}
	
	public HabilidadeDTO(Long id, String descricao, Long idMutante) {
		super();
		this.idMutante = idMutante;
		this.id = id;
		this.descricao = descricao;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getIdMutante() {
		return idMutante;
	}

	public void setIdMutante(Long idMutante) {
		this.idMutante = idMutante;
	}
	
	
}
