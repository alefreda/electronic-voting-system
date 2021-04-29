package com.sweng.elezione.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sweng.elezione.shared.Lista;
import com.sweng.elezione.shared.Voto;
import com.sweng.elezione.shared.Elezione;

public interface ServizioElezioneAsync {
	
	void creaElezione(String nomeElezione, String dataInizio, String dataFine, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void getElezioneNome(String nome, AsyncCallback<Elezione> callback) throws IllegalArgumentException;
	
	void listaElezioni(AsyncCallback<ArrayList<Elezione>> callback) throws IllegalArgumentException;
	
	void listaListeElettoraliDiElezione(String nomeElezione, AsyncCallback<ArrayList<Lista>> callback)
			throws IllegalArgumentException;
	
	void creaListaElettorale(String elezione, String nomeLista, String sindaco, String simbolo,
			ArrayList<String> componenti, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	
	void listaListeElettorali(AsyncCallback<ArrayList<Lista>> callback) throws IllegalArgumentException;
	
	void approveRejectLista(int approveReject, String nomeLista, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void vota(String listaElettorale, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	
	void partecipantiListaElettorale(String nomeLista, AsyncCallback<ArrayList<String>> callback)
			throws IllegalArgumentException;
	
	void votaCandidato(String listaElettorale, String nomeCandidato, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void aggiungiVoto(Voto voto, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	
	void numeroVotiListaElettorale(String nomeLista, AsyncCallback<Integer> callback);
}
