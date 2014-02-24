package com.adhub.objects;

import java.io.Serializable;

public class Propaganda implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer propagandaID;
	private Integer minorID;
	private Integer majorID;
	private String mensagemNotificacao;
	private Long ultimaAtualizacao;
	private String url;
	private Integer proximidade;
	private Integer navbarColor;
	private Integer categoria;

	public Integer getPropagandaID() {
		return propagandaID;
	}

	public void setPropagandaID(Integer propagandaID) {
		this.propagandaID = propagandaID;
	}

	public Integer getMinorID() {
		return minorID;
	}

	public void setMinorID(Integer minorID) {
		this.minorID = minorID;
	}

	public Integer getMajorID() {
		return majorID;
	}

	public void setMajorID(Integer majorID) {
		this.majorID = majorID;
	}

	public String getMensagemNotificacao() {
		return mensagemNotificacao;
	}

	public void setMensagemNotificacao(String mensagemNotificacao) {
		this.mensagemNotificacao = mensagemNotificacao;
	}

	public Long getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(Long ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getProximidade() {
		return proximidade;
	}

	public void setProximidade(Integer proximidade) {
		this.proximidade = proximidade;
	}

	public Integer getNavbarColor() {
		return navbarColor;
	}

	public void setNavbarColor(Integer navbarColor) {
		this.navbarColor = navbarColor;
	}

	public Integer getCategoria() {
		return categoria;
	}

	public void setCategoria(Integer categoria) {
		this.categoria = categoria;
	}
}
