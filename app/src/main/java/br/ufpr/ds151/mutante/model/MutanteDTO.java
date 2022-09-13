package br.ufpr.ds151.mutante.model;

import java.io.Serializable;
import java.util.List;

public class MutanteDTO implements Serializable {

	private Long id;
	private String nome;
	private Long idUsuario;
	private List<HabilidadeDTO> habilidades;
	private String foto;
	
	public MutanteDTO() {
		super();
	}

	public MutanteDTO(Long id, String nome, Long idUsuario, List<HabilidadeDTO> habilidades, String foto) {
		super();
		this.id = id;
		this.nome = nome;
		this.idUsuario = idUsuario;
		this.habilidades = habilidades;
		this.foto = foto;
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

	public List<HabilidadeDTO> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<HabilidadeDTO> habilidades) {
		this.habilidades = habilidades;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
}
