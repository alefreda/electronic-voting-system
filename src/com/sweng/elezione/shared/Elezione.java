package com.sweng.elezione.shared;

import java.io.Serializable;
import java.util.ArrayList;
import com.sweng.elezione.shared.Lista;



@SuppressWarnings("serial")
public class Elezione implements Serializable{

	private String nomeElezione, data_inizio, data_fine;

	private ArrayList<Lista> liste_candidate;

	public Elezione() {

	}

	public Elezione(String nome, String dataInizio, String dataFine) {
		this.nomeElezione = nome;
		this.data_fine = dataFine;
		this.data_inizio = dataInizio;
	}

	public String getNomeElezione() {
		return nomeElezione;
	}

	public void setNomeElezione(String nomeElezione) {
		this.nomeElezione = nomeElezione;
	}

	public String getData_inizio() {
		return data_inizio;
	}

	public String getData_fine() {
		return data_fine;
	}

	public ArrayList<Lista> getListe() {
		return this.liste_candidate;
	}
}
