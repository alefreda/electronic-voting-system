package com.sweng.elezione.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.sweng.elezione.client.HomeElezione;


public class Home {
	
	private final ServizioAutenticazioneAsync servizioAutenticazione = GWT.create(ServizioAutenticazione.class);
	
	HomeElezione elezione = new HomeElezione();
	
	private final VerticalPanel pannelloHome = new VerticalPanel();
	//se funzionario true
	private boolean funzionario;
	
	public Panel homePage() {

		servizioAutenticazione.utenteLoggato(new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());				
			}
			@Override
			public void onSuccess(Boolean result) {
				
				if (result) {
					pannelloHome.add(new Label("Ti sei autenticato"));
					pannelloHome.add(new Label("Bentornato "));	
					
					
					
					
					//servizio per conoscere il ruolo: admin, funzionario, utenteRegistrato
					servizioAutenticazione.getRuolo(new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							if (result=="admin") {
								pannelloHome.add(new Label("Sei un utente ADMIN"));
								pannelloHome.add(new Label("-------------------------"));
								pannelloHome.add(new Label("CREA ELEZIONE"));
								
								//visualizza creazione elezioni e lista utenti per renderli Funzionari
								pannelloHome.add(elezione.creaElezione());
								pannelloHome.add(new Label("-------------------------"));
								pannelloHome.add(new Label("LISTA UTENTI REGISTRATI"));
								
								pannelloHome.add(elezione.listaUtenti());
								
								pannelloHome.add(new Label("-------------------------"));
								pannelloHome.add(new Label("APPROVA LISTE"));
								pannelloHome.add(elezione.listeElettorali());

							
							}	else if(result== "funzionarioComunale") {
								pannelloHome.add(new Label("Sei un utente FUNZIONARIO COMUNALE"));
								pannelloHome.add(new Label("-------------------------"));
								pannelloHome.add(new Label("CREA ELEZIONE"));
								
								pannelloHome.add(elezione.creaElezione());
								pannelloHome.add(new Label("-------------------------"));
								//apProva liste
								pannelloHome.add(new Label("APPROVA LISTE"));
								pannelloHome.add(elezione.listeElettorali());
						
							} else {
							
								//se è utente registrato
								pannelloHome.add(new Label("-------------------------"));
								pannelloHome.add(new Label("CREA LISTA ELETTORALE"));
								pannelloHome.add(elezione.creazioneListaElettorale());
								pannelloHome.add(new Label("-------------------------"));
								pannelloHome.add(new Label("VOTA"));
								pannelloHome.add(elezione.mostraListaElezioni());
								pannelloHome.add(new Label("-------------------------"));
								//aggiungi panel elezioni concluse
								pannelloHome.add(elezione.mostraElezioniConcluse());
							}
						}
					}); // end getRuolo
					
					
					
					
					
					
				} else {
					//caso di utente non loggato può solo visualizzare le elezioni concluse
					pannelloHome.add(new Label("Non loggato: puoi solo visualizzare le elezioni concluse"));
					pannelloHome.add(new Label("-------------------------"));
					//aggiungi panel elezioni concluse
					pannelloHome.add(elezione.mostraElezioniConcluse());
					
				}
			} //end onSuccess
			
		}); //end servizio utenteloggato
		
		
		
			
		
		return pannelloHome;
	}//end pannello
	
	
}
