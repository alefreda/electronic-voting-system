package com.sweng.elezione.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sweng.elezione.client.Autenticazione;
import com.sweng.elezione.client.ServizioElezione;
import com.sweng.elezione.shared.Elezione;
import com.sweng.elezione.shared.Lista;
import com.sweng.elezione.shared.Utente;
import com.sweng.elezione.shared.Voto;

@SuppressWarnings("serial")
public class ElezioneImpl extends RemoteServiceServlet implements ServizioElezione {
	
	private AutenticazioneImpl autenticazioneServizio = new AutenticazioneImpl(); 

	public DB getDB() {

		DB db = DBMaker.newFileDB(new File("DB-SWENG-ELEZIONE")).closeOnJvmShutdown().make();

		return db;

	}
	
	
	//crea elezione 
	@Override
	public boolean creaElezione(String nomeElezione, String dataInizio, String dataFine) {
		Elezione nuovaElezione = new Elezione(nomeElezione, dataInizio, dataFine);

		ArrayList<Elezione> listaElezioni = caricaElezioni();
		listaElezioni.add(nuovaElezione);
		salvaElezione(listaElezioni);

		return true;
	}
	
	//salva elezione nel db
	public void salvaElezione(ArrayList<Elezione> elezioni) {
			
			DB db = getDB();
			Map<Integer, Elezione> map = db.getTreeMap("elezione");
			map.clear();
			int contatore = 0;
			for (Elezione lista : elezioni) {
				map.put(contatore++, lista);
			}
			
			db.commit(); // commit dopo le modifiche
			
		}
	
	//carica la liste delle elezioni presenti nel db
	public ArrayList<Elezione> caricaElezioni() {
			
			DB db = getDB();
			Map<Integer, Elezione> map = db.getTreeMap("elezione");
			ArrayList<Elezione> elezioni = new ArrayList<Elezione>();
			Set<Integer> keys = map.keySet();
			for (int key : keys) {
				elezioni.add(map.get(key));
			}
			return elezioni;
		}

	
	//ritorna la lista delle elezioni
	@Override
	public ArrayList<Elezione> listaElezioni() {
		ArrayList<Elezione> listaElezioni = caricaElezioni();
		return listaElezioni;
	}

	
	//ritorna elezione dato un nome di una elezione, utilizzato per verificare se 
	//l'elezione creata è gia stata creata e quindi presente du DB
	//null se il nome elezione non è presente nel DB
	@Override
	public Elezione getElezioneNome(String nomeElezione) {
		DB db = getDB();
		Map<Integer, Elezione> map = db.getTreeMap("elezione");
		Set<Integer> keys = map.keySet();
		
		for (int key : keys) {
			if (map.get(key).getNomeElezione().equals(nomeElezione))
				return map.get(key);
		}

		return null;
	}
	
	//data una elezione ritorna la lista delle liste elettorali per quella elezione
	@Override
	public ArrayList<Lista> listaListeElettoraliDiElezione(String nomeElezione) {
			
			DB db = getDB();
			Map<Integer, Lista> map = db.getTreeMap("liste");
			ArrayList<Lista> liste = new ArrayList<Lista>();
			Set<Integer> keys = map.keySet();
	
			for (int key : keys) {
				if (map.get(key).getElezione().equals(nomeElezione))
					liste.add(map.get(key));
			}
			return liste;
		}
	
	//crea lista elettore e aggiunge tramite il metodo memorizzaLista la lista nel db
	@Override
	public Boolean creaListaElettorale(String elezione, String nomeLista, String sindaco, String simbolo,
			ArrayList<String> componenti) {
		
		//creo la lista
		Lista nuovaLE = new Lista(elezione, sindaco, nomeLista, simbolo, componenti);
		
		//carico la lista delle liste elettorali
		ArrayList<Lista> listaListeElettorali = listaListeElettorali();

		//aggiungo alla lista delle liste elettorali la nuova lista creata
		listaListeElettorali.add(nuovaLE);
		
		//memorizzo la lista creata nel db
		memorizzaListaElettorale(listaListeElettorali);

		return true;
	
	}
	
	//restituire la lista delle liste elettorali
	@Override
	public ArrayList<Lista> listaListeElettorali() {
			
			
			DB db = getDB();
			Map<Integer, Lista> map = db.getTreeMap("liste");
			ArrayList<Lista> liste = new ArrayList<Lista>();
			Set<Integer> keys = map.keySet();
			for (int key : keys) {
				liste.add(map.get(key));
			}
	
			return liste;
			
		}
	
	//data la lista delle liste elettorali memorizza nel db le liste lettorali
	public void memorizzaListaElettorale(ArrayList<Lista> listaelettorale) {

		DB db = getDB();
		Map<Integer, Lista> map = db.getTreeMap("liste");
		//pulisco
		map.clear();
		
		int contatore = 0;
		//inserisco la lista delle liste elettorali
		for (Lista lista : listaelettorale) {
			map.put(contatore++, lista);
		}
		db.commit(); //commit 

	}
	
	//data una lista modifica lo stato di approvazione per quella lista
	@Override
	public Boolean approveRejectLista(int statoApprovazione, String nomeLista) {
		
		Lista listaElettorale = listaElettoraleDaNome(nomeLista);
		
		listaElettorale.setStatoApprovazione(statoApprovazione);
		
		//modifica la lista elettorale dato lo stato di approvazione
		modificaListaElettorale(listaElettorale);

		return true;
	}
	
	//ritorna la lista dato il suo nome
	public Lista listaElettoraleDaNome(String nome) {

		DB db = getDB();
		Map<Integer, Lista> map = db.getTreeMap("liste");

		Set<Integer> keys = map.keySet();

		//ritorna la lista elettorale con quel nome
		for (int key : keys) {
			if (map.get(key).getNomeLista().equals(nome))
				return map.get(key);
		}

		return null;
	}
	
	//modifica una lista data
	private Boolean modificaListaElettorale(Lista listaElettorale) {

		//carica la lista delle liste elettorali 
		ArrayList<Lista> listaListeElettorali = this.listaListeElettorali();

		for (int i = 0; i < listaListeElettorali.size(); i++) {
			//se la lista esiste settala
			if (listaElettorale.getNomeLista().equalsIgnoreCase(listaListeElettorali.get(i).getNomeLista())) {
				listaListeElettorali.set(i, listaElettorale);
			}
		}
		//memorizzo la lista nel db
		memorizzaListaElettorale(listaListeElettorali);

		return true;
	}


	//data una lista elettorale determina il voto per quella
	@Override
	public Boolean vota(String listaElettorale) {
		//definisco utente registato in questo momento
		String userVotante = autenticazioneServizio.getUsername();
		Voto voto = new Voto(userVotante, listaElettorale);
		aggiungiVoto(voto);
		//controlla se l'user ha già votato
		aggiungiVoto(voto);
		return true;
//		if (verificaVotoValido(userVotante)) {
//			//vota
//			aggiungiVoto(voto);
//			
//			return true;
//		} else {
//			return false;
//		}
	}
	
	//verifica se l'utente loggato in questo momento userUtente
	//puo effettivamente votare 
	//false se non può votare 
//	public boolean verificaVotoValido(String userUtente) {
//		ArrayList<Voto> allVoti = listaDeiVoti();
//		boolean prova = true;
//		for (Voto voto : allVoti) {
//			if (userUtente.equals(voto.getUsernameCittadino())) {
//				prova = false;
//				break;
//			}
//		}
//		return prova;
//	}
	
	//aggiunge il voto
	@Override
	public Boolean aggiungiVoto(Voto voto) {
		
		ArrayList<Voto> listaVoti = listaDeiVoti();
		//aggiungo il voto alla lista
		listaVoti.add(voto);
		//memorizza il voto nel db
		memorizzaVoto(listaVoti);

		// modifico la lista dato il voto
		Lista lElett = listaElettoraleDaNome(voto.getNomeListaElettorale());
		lElett.setTotalVoti(lElett.getTotalVoti() + 1);
		modificaListaElettorale(lElett);
		System.out.println("Ho aggiunto il voto");
		return true;
	}
	
	//ritorna la lista dei voti
	public ArrayList<Voto> listaDeiVoti() {

		DB db = getDB();
		Map<Integer, Voto> map = db.getTreeMap("voti");
		
		ArrayList<Voto> voti = new ArrayList<Voto>();
		
		Set<Integer> keys = map.keySet();
		for (int key : keys) {
			voti.add(map.get(key));
		}
		return voti;

	}
	
	//data una lista di voti li memorizza nel db
	public void memorizzaVoto(ArrayList<Voto> voti) {

		DB db = getDB();
		Map<Integer, Voto> map = db.getTreeMap("voti");
		
		//pulisco
		map.clear();
		
		int contatore = 0;
		
		//inserisco i voti
		for (Voto voto : voti) {
			
			map.put(contatore++, voto);
		}
		
		db.commit(); //commit

	}
	
	//dato un nome di una lista elettorale ritorna tutti i partecipanti
	@Override
	public ArrayList<String> partecipantiListaElettorale(String nome) {
		DB db = getDB();
		Map<Integer, Lista> map = db.getTreeMap("liste");

		Set<Integer> keys = map.keySet();
		for (int key : keys) {
			if (map.get(key).getNomeLista().equals(nome)) {
				return map.get(key).getComponenti();
			}
		}

		return null;
	}

	//dato un candicato e una lista, l'utente attualemte loggato, vota il cadidato
	@Override
	public Boolean votaCandidato(String listaElettorale, String nomeCandidato) {
		
		//identifico l'username dell'utente registato in questo momento
		String userVotante = autenticazioneServizio.getUsername();
		
		//creo il voto
		Voto voto = new Voto(userVotante, listaElettorale, nomeCandidato);
		aggiungiVoto(voto);
		return true;
		
//		//controlla se ha già votato
//		if (verificaVotoValido(userVotante)
//				&& !listaElettoraleDaNome(listaElettorale).getSindaco().equalsIgnoreCase(nomeCandidato)) {
//			//aggiungi il voto
//			aggiungiVoto(voto);
//			return true;
//		} else {
//			return false;
//		}
		
	}
	
	
	//ritorna il numero di voti per una lista elettorale
	@Override
	public int numeroVotiListaElettorale(String nomeLista) {
		int totalVoti = 0;
		ArrayList<Voto> allVoti = listaDeiVoti();

		for (Voto voto : allVoti) {
			if (nomeLista.equals(voto.getNomeListaElettorale())) {
				totalVoti++;
			}
		}
		System.out.println("OK restituisco il numero di voti per questa lista elettorale: " + totalVoti);
		return totalVoti;
	}
	
}
