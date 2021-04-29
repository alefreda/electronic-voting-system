package com.sweng.elezione.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Voto implements Serializable{
	private String cittadino;
	@SuppressWarnings("unused")
	private String nomeListaElettorale, nomeComponente;

	public Voto() {

	}

	public Voto(String username, String lista) {
		this.cittadino = username;
		this.nomeListaElettorale = lista;
	}

	public Voto(String username, String lista, String nomeComponente) {
		this.cittadino = username;
		this.nomeListaElettorale = lista;
		this.nomeComponente = nomeComponente;
	}

	public String getNomeListaElettorale() {
		return nomeListaElettorale;
	}

	public String getUsernameCittadino() {
		return cittadino;
	}
	
	public String getNomeComponente() {
		return nomeComponente;
	}
}