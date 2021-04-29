package com.sweng.elezione.client;

import java.util.Date;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.elezione.shared.Utente;

public interface ServizioAutenticazioneAsync {
	
	void login(String Username, String Password, AsyncCallback<Integer> callback);

	void registrazione(String Username, String Password, String RepeatPassword, String nome, String cognome, String telefono,
			String email, String codiceFiscale, String domicilio, String tipoDocumento, String numeroDocumento,
			String enteRilascio, Date dataRilascio, Date dataScadenza, AsyncCallback<Integer> callback);

	void utenteLoggato(AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void logout(AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	
	void getRuolo(AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void getUsername(AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void getFunzionario(String username, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void caricaUtenti(AsyncCallback<ArrayList<Utente>> callback) throws IllegalArgumentException;

	void rendiFunzionario(String username, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void getUtente(String text, AsyncCallback<Utente> callback) throws IllegalArgumentException;
	


}