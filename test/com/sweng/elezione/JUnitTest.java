package com.sweng.elezione;



import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;


import com.sweng.elezione.server.AutenticazioneImpl;
import com.sweng.elezione.server.ElezioneImpl;
import com.sweng.elezione.shared.Elezione;
import com.sweng.elezione.shared.Lista;
import com.sweng.elezione.shared.Utente;







public class JUnitTest {
	public AutenticazioneImpl serverAutenticazione = new AutenticazioneImpl();
	
	public ElezioneImpl serverElezione = new ElezioneImpl();
	
	
	
	public String username = "ciao";
	public String password = "12345678";

	public String nomeElezione = "elezione1";

	public String nomeLista = "listaCiao";
	public Date dataRilascio, dataScadenza;
	
	// test primo sprint
	@Test
	void testRegistrazione() {

		int response = -1;

		response = serverAutenticazione.registrazione(username, password, password, "nome", "cognome", "telefono", "email",
				"codiceFiscale", "domicilio", "tipoDocumento", "numeroDocumento", "enteRilascio", dataRilascio,
				dataScadenza);

		if (response == -4)
			response = 1; // for multi tests

		assertEquals(1, response);
	}
	
	@Test
	public void testLogin() {

		int response = -1;

		response = serverAutenticazione.login(username, password);

		assertEquals(2, response);
	}
	
	
	//test per il secondo sprint
	
	
	//test per caricare tutti gli utenti del db
	@Test
	public void testCaricaUtenti() {
		ArrayList<Utente> utenti = serverAutenticazione.caricaUtenti();
		assertEquals(ArrayList.class, utenti.getClass());
	}
	
	//test per creare elezione
	@Test
	public void testCreaElezione() {
		assertTrue(serverElezione.creaElezione(nomeElezione, "2018-12-1", "2018-12-3"));
	}
	
	//test per caricare tutte le elezioni presenti nel sistema
	@Test
	public void testListaElezioni() {
		ArrayList<Elezione> listaElezioni = serverElezione.listaElezioni();
		assertEquals(ArrayList.class, listaElezioni.getClass());
	}
	
	//test per creare lista elettorale
	@Test
	public void testCreaListaElettorale() {
		ArrayList<String> componentiLista = new ArrayList<String>();
		componentiLista.add(username);

		assertTrue(serverElezione.creaListaElettorale(nomeElezione, nomeLista, username, "simbolo", componentiLista));
	}
	
	//test per promuovere un utente a funzionario
	@Test
	public void rendiFunzionario() {
		assertTrue(serverAutenticazione.rendiFunzionario(username));
	}
	
	//test per la creazione della lista elettorale
	@Test
	public void creaListaElettorale() {
		ArrayList<String> componenti = new ArrayList<String>();
		componenti.add(username);
		
		assertTrue(serverElezione.creaListaElettorale(nomeElezione, nomeLista, username, "mela", componenti));
	}
	
	//test per la verifica delle liste da elezione
	@Test
	public void listaElettoraleDaElezione() {
		ArrayList<Lista> listaListe = serverElezione.listaListeElettorali();
		assertEquals(ArrayList.class, listaListe.getClass());
	}
	
	//test per approvare o rigettare una lista 
	@Test
	public void testApprovaRigetta() {
		assertTrue(serverElezione.approveRejectLista(1, nomeLista));
		}

	
}
