package com.sweng.elezione.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sweng.elezione.client.ServizioAutenticazione;
import com.sweng.elezione.shared.Utente;
import com.sweng.elezione.shared.DocumentoIdentita;





@SuppressWarnings("serial")
public class AutenticazioneImpl extends RemoteServiceServlet implements ServizioAutenticazione{
	

	private boolean utenteLoggato = false;
	
	//variabile che memorizza il ruolo dell'utente
	private String utenteCorrente;
	
	//variabili che memorizza l'username dell'utente
	private String username;

	
	public DB getDB() {

		DB db = DBMaker.newFileDB(new File("DB-SWENG")).closeOnJvmShutdown().make();

		return db;

	}
	
	
	@Override
	public String getRuolo() {
		if (utenteCorrente == "admin") {
			return "admin";
		} else if(getFunzionario(utenteCorrente)) {
			return "funzionarioComunale";
		} else {
			return "utenteRegistrato";
		}
	}



	@Override
	public Integer login(String Username, String Password) {
		
		
		if ((Username.equalsIgnoreCase("admin")) && (Password.equalsIgnoreCase("admin"))) {  //se login da admin
			utenteLoggato = true;
			utenteCorrente = "admin";
			username= Username;
			return 1;
		} else if(getUtenteLogin(Username, Password)) { //se login ha avuto successo 
			utenteLoggato = true;
			utenteCorrente = Username;
			username= Username;
			return 2;
		} else {
			return 0;
		}
	}
	

	@Override
	public Integer registrazione(String Username, String Password, String RepeatPassword, String nome, String cognome,
			String telefono, String email, String codiceFiscale, String domicilio, String tipoDocumento,
			String numeroDocumento, String enteRilascio, Date dataRilascio, Date dataScadenza) {
	
		int risposta = 0;

		if (!Password.equals(RepeatPassword)) {// confronta le due password 
			risposta = -2;
		} else if (Password.length() < 5) {// 8) {//pass troppo corta
			risposta = -3;
		} else if (verificaUtenteEsiste(Username)) {// utente gia esiste
			risposta = -4;
		} else {

			DocumentoIdentita documento = new DocumentoIdentita(tipoDocumento, numeroDocumento, enteRilascio,
					dataRilascio, dataScadenza);

			Utente utenteRegistrato = new Utente(Username, Password, nome, cognome, telefono, email, codiceFiscale,
					domicilio, documento, false);

			if (inserisciUtenteDB(utenteRegistrato)) { 
				//se true allora OK
				risposta = 1; //inserito con successo
			} else {
				risposta = -1; //false
			}
		}
		return risposta;
	}

	@Override
	public Boolean utenteLoggato() {
		return utenteLoggato;
	}

	@Override
	public Boolean logout() {
		utenteLoggato= false;
		return true;
	}

	@Override
	public Boolean getFunzionario(String username) {

		if (username.equalsIgnoreCase("admin"))
			return true;
		else {
			Utente utenteTmp = this.getUtente(username);
			if (utenteTmp != null) { // Cittadino trovato
				return utenteTmp.isFunzionario();
			} else {
				return false;
			}
		}

	}

	@Override
	public ArrayList<Utente> caricaUtenti() {
		DB db = getDB();
		Map<Integer, Utente> map = db.getTreeMap("cittadini");
		ArrayList<Utente> utenti = new ArrayList<Utente>();
		Set<Integer> keys = map.keySet();
		for (int key : keys) {
			utenti.add(map.get(key));
		}

		return utenti;
	}
	
	/**
	 * aggiorna un cittadino
	 * 
	 * @param utente
	 * @return
	 */
	private Boolean aggiornaUtente(Utente utente) {

		ArrayList<Utente> utenti = this.caricaUtenti();

		for (int i = 0; i < utenti.size(); i++) {
			if (utente.getUsername().equalsIgnoreCase(utenti.get(i).getUsername())) {
				
				utenti.set(i, utente);
			}
		}
		salvaUtente(utenti);

		return true;
	}
	
	

	/**
	 * promuovi un cittadino
	 */
	@Override
	public Boolean rendiFunzionario(String username) {

		Utente utenteTmp = getUtente(username);
		utenteTmp.setFunzionario(true);
		aggiornaUtente(utenteTmp);

		return true;
	}

	@Override
	public Utente getUtente(String text) {
		DB db = getDB();
		Map<Integer, Utente> map = db.getTreeMap("cittadini");

		Set<Integer> keys = map.keySet();

		for (int key : keys) {
			if (map.get(key).getUsername().equalsIgnoreCase(text))
				return map.get(key);
		}

		return null;
	}
	
	
	//salva gli utenti
	
	public void salvaUtente(ArrayList<Utente> utenti) {

		DB db =getDB();
		Map<Integer, Utente> map = db.getTreeMap("cittadini");
		map.clear();
		int contatore = 0;
		for (Utente utente : utenti) {
			map.put(contatore++, utente);
		}

		db.commit(); // commit

	}
	
	
	 //controlla se un utente con lo stesso username del parametro passato esiste

	public Boolean verificaUtenteEsiste(String username) {

		boolean find = false;

		ArrayList<Utente> cittadini = caricaUtenti();

		for (int i = 0; i < cittadini.size(); i++) {

			if (username.equalsIgnoreCase(cittadini.get(i).getUsername())) {
				find = true;
			}
		}

		return find;
	}
	
	
	//true se l'utente esite, false altrimenti
	
	public Boolean getUtenteLogin(String username, String password) {

		ArrayList<Utente> utenti = this.caricaUtenti();

		for (int i = 0; i < utenti.size(); i++) {

			if (username.equalsIgnoreCase(utenti.get(i).getUsername())
					&& password.equals(utenti.get(i).getPassword())) {
				return true;
			}
		}
		return false;
	}
	
	
	 //inserisci un utente nel db

	public Boolean inserisciUtenteDB(Utente utente) {

		ArrayList<Utente> utenti = this.caricaUtenti();

		utenti.add(utente);

		boolean done = true;

		try {
			this.salvaUtente(utenti);
		} catch (Exception e) {
			done = false;
		}

		return done;

	}


	@Override
	public String getUsername() {
		return username;
	}






}
