package com.sweng.elezione.shared;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Utente implements Serializable {

	@SuppressWarnings("unused")
	private String username, password, nome, cognome, numeroTelefono, email, codice_fiscale;
	@SuppressWarnings("unused")
	private DocumentoIdentita documento;
	private boolean funzionario = false;

	public Utente() {

	}

	// Costruttori
	public Utente(String username, String password, String nome, String cognome, String numeroTelefono, String email,
			String codice_fiscale, String domicilio, DocumentoIdentita documento, boolean funzionario) {
		this.username = username.toLowerCase();
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.numeroTelefono = numeroTelefono;
		this.email = email;
		this.codice_fiscale = codice_fiscale;
		this.documento = documento;
		this.funzionario = funzionario;
	}

	public Utente(String username, String password) {
		this.username = username.toLowerCase();
		this.password = password;
		this.funzionario = false;
	}

	// Costruttore per l'admin
	public Utente(String username, String password, boolean funzionario) {
		this.username = username.toLowerCase();
		this.password = password;
		this.funzionario = funzionario;
	}

	public String getUsername() {
		return this.username.toLowerCase();
	}

	public String getPassword() {
		return this.password;
	}

	public boolean isFunzionario() {
		return this.funzionario;
	}

	public void setFunzionario(boolean funzionario) {
		this.funzionario = funzionario;
	}

}
