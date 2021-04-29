package com.sweng.elezione.shared;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Lista implements Serializable{

	private String sindaco, nomeLista, simbolo;
	private ArrayList<String> componenti = new ArrayList<>();
	private String elezione;
	private int statoApprovazione;
	private int totalVoti;

	public Lista() {

	}

	public Lista(String elezione, String sindaco, String nomeLista, String simbolo,
			ArrayList<String> componenti) {

		this.elezione = elezione;
		this.sindaco = sindaco;
		this.nomeLista = nomeLista;
		this.simbolo = simbolo;
		this.componenti = componenti;

	}

	public String getSimbolo() {
		return simbolo;
	}

	public String getNomeLista() {
		return nomeLista;
	}

	public String getSindaco() {
		return sindaco;
	}

	public void setTotalVoti(int totalVoti) {
		this.totalVoti = totalVoti;
	}

	public int getTotalVoti() {
		return this.totalVoti;
	}

	public int getStatoApprovazione() {
		return this.statoApprovazione;
	}

	public void setStatoApprovazione(int stApprovazione) {
		this.statoApprovazione = stApprovazione;
	}

	public ArrayList<String> getComponenti() {
		return componenti;
	}

	public String getElezione() {
		return elezione;
	}
}

