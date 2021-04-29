package com.sweng.elezione.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.sweng.elezione.shared.Lista;
import com.sweng.elezione.shared.Voto;
import com.sweng.elezione.shared.Elezione;

@RemoteServiceRelativePath("elezione")
public interface ServizioElezione extends RemoteService{
	
	boolean creaElezione(String nomeElezione, String dataInizio, String dataFine);
	
	ArrayList<Elezione> listaElezioni();
	
	Elezione getElezioneNome(String nome);
	
	ArrayList<Lista> listaListeElettoraliDiElezione(String nomeElezione);
	
	Boolean creaListaElettorale(String elezione, String nomeLista, String sindaco, String simbolo,
			ArrayList<String> componenti);
	
	ArrayList<Lista> listaListeElettorali();
	
	Boolean approveRejectLista(int approveReject, String nomeLista);
	
	Boolean vota(String listaElettorale);
	
	ArrayList<String> partecipantiListaElettorale(String nome);
	
	Boolean votaCandidato(String listaElettorale, String nomeCandidato);
	
	Boolean aggiungiVoto(Voto voto);
	
	int numeroVotiListaElettorale(String nomeLista);
}
