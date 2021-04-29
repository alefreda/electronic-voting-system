package com.sweng.elezione.client;

import java.util.Date;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sweng.elezione.shared.Utente;

@RemoteServiceRelativePath("autenticazione")
public interface ServizioAutenticazione extends RemoteService {
	
	Integer login(String Username, String Password);

	Integer registrazione(String Username, String Password, String RepeatPassword, String nome, String cognome,
			String telefono, String email, String codiceFiscale, String domicilio, String tipoDocumento,
			String numeroDocumento, String enteRilascio, Date dataRilascio, Date dataScadenza);
	
	Boolean utenteLoggato();
	
	String getRuolo();
	
	String getUsername();


	Boolean logout();
	
	Boolean getFunzionario(String username);
	

	ArrayList<Utente> caricaUtenti();
	

	Boolean rendiFunzionario(String username);
	

	Utente getUtente(String text);

	
}
