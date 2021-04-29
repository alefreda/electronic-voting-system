package com.sweng.elezione.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sweng.elezione.client.ServizioAutenticazione;
import com.sweng.elezione.client.ServizioAutenticazioneAsync;

import com.sweng.elezione.client.Autenticazione;

public class LoginRegistrazione{
	
	
	private final ServizioAutenticazioneAsync servizioAutenticazione = GWT.create(ServizioAutenticazione.class);
	private Autenticazione autenticazione = new Autenticazione();
	
	public Panel login(){
		// Login Section
		//final Label lblLoginResult = new Label();
		final PasswordTextBox pswField = new PasswordTextBox();
		final TextBox usrField = new TextBox();
		final Button doneLogin = new Button("Login");

		// placeholder - login
		usrField.getElement().setAttribute("placeholder", "user");
		pswField.getElement().setAttribute("placeholder", "password");

		// Pannello DIV - Login
		final VerticalPanel panelLogin = new VerticalPanel();
		//panelLogin.add(lblLoginResult);
		panelLogin.add(usrField);
		panelLogin.add(pswField);
		panelLogin.add(doneLogin);

		doneLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				String username = usrField.getText();
				String password = pswField.getText();


				if ((username.length() > 0) && (password.length() > 0)) {
									
					
					
					//servizio per il login
					
					servizioAutenticazione.login(username, password, new AsyncCallback<Integer>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(Integer result) {
							
								if (result==1) {
									panelLogin.clear();
									Window.alert("Autenticato con successo come admin");
									
								
									autenticazione.ricarica();
									autenticazione.menuHome("home");
											
								} else if(result==2) {
									Window.alert("Autenticato con successo");
									autenticazione.ricarica();
									autenticazione.menuHome("home");
								} else {
									Window.alert("Errore ripeti");
								}

							
						} //end metodo onSuccess
					}); //end servizio
					

					
				} else {
					Window.alert("tutti i campi sono obbligatori");
				}
			};

		});

		return panelLogin;

	}//end panel login
	
	
	
	
	
	
	
	//panel registrazione
	public Panel registrazione() {
		
		final VerticalPanel pannelloRegistrazione = new VerticalPanel();
		
		
		final Label resultOnRegistration = new Label();
		final TextBox username = new TextBox();
		final PasswordTextBox password = new PasswordTextBox();
		final PasswordTextBox ripetiPassword = new PasswordTextBox();
		final TextBox nome = new TextBox();
		final TextBox cognome = new TextBox();
		final TextBox telefono = new TextBox();
		final TextBox email = new TextBox();
		final TextBox codiceFiscale = new TextBox();
		final TextBox indirizzo = new TextBox();
		final ListBox documentoTipo = new ListBox();
		final TextBox numDocumento = new TextBox();
		final TextBox enteRilascio = new TextBox();
		final DateBox dataEmissione = new DateBox();
		final DateBox dataScadenza = new DateBox();
		final Button effettuaRegistrazione = new Button("Registrati");

		documentoTipo.addItem("Carta Identita");
		documentoTipo.addItem("Passaporto");

		

		username.getElement().setAttribute("placeholder", "User");
		password.getElement().setAttribute("placeholder", "Password");
		ripetiPassword.getElement().setAttribute("placeholder", "Ripeti Pass");
		nome.getElement().setAttribute("placeholder", "Nome");
		cognome.getElement().setAttribute("placeholder", "Cognome");
		telefono.getElement().setAttribute("placeholder", "Telefono");
		email.getElement().setAttribute("placeholder", "email");
		indirizzo.getElement().setAttribute("placeholder", "Indirizzo");
		codiceFiscale.getElement().setAttribute("placeholder", "Codice Fiscale");
		numDocumento.getElement().setAttribute("placeholder", "Numero Documento");
		enteRilascio.getElement().setAttribute("placeholder", "Ente Rilascio");
		dataEmissione.getElement().setAttribute("placeholder", "Data di rilascio");
		dataScadenza.getElement().setAttribute("placeholder", "Data di scadenza");

		
		pannelloRegistrazione.add(resultOnRegistration);
		pannelloRegistrazione.add(username);
		pannelloRegistrazione.add(password);
		pannelloRegistrazione.add(ripetiPassword);
		pannelloRegistrazione.add(nome);
		pannelloRegistrazione.add(cognome);
		pannelloRegistrazione.add(telefono);
		pannelloRegistrazione.add(email);
		pannelloRegistrazione.add(indirizzo);
		pannelloRegistrazione.add(codiceFiscale);
		pannelloRegistrazione.add(documentoTipo);
		pannelloRegistrazione.add(numDocumento);
		pannelloRegistrazione.add(enteRilascio);
		pannelloRegistrazione.add(dataEmissione);
		pannelloRegistrazione.add(dataScadenza);
		pannelloRegistrazione.add(effettuaRegistrazione);

		effettuaRegistrazione.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				String user = username.getText();
				String pass = password.getText();
				String ripetiPass = ripetiPassword.getText();
				String nomeUtente = nome.getText();
				String cognomeUtente = cognome.getText();
				String tel = telefono.getText();
				String emailUtente = email.getText();
				String codiceFiscaleUtente = codiceFiscale.getText();
				String indirizzoUtente = indirizzo.getText();
				String tipoDoc = documentoTipo.getValue(documentoTipo.getSelectedIndex());
				String numero = numDocumento.getText();
				String enteRilascioDocumento = enteRilascio.getText();
				Date dataRilascioDocumento = dataEmissione.getValue();
				Date dataScadenzaDocumento = dataScadenza.getValue();

				if ((user.length() > 0) && (pass.length() > 0) && (pass.equalsIgnoreCase(ripetiPass))
						&& (nomeUtente.length() > 0) && (cognomeUtente.length() > 0) && (tel.length() > 0)
						&& (emailUtente.length() > 0) && (codiceFiscaleUtente.length() > 0)
						&& (indirizzoUtente.length() > 0) && (tipoDoc.length() > 0) && (numero.length() > 0)
						&& (enteRilascioDocumento.length() > 0) && (dataRilascioDocumento.before(dataScadenzaDocumento)) ) {
					
					
					
					
					//servizio per la registrazione 
					
					servizioAutenticazione.registrazione(user, pass, ripetiPass, nomeUtente, cognomeUtente, tel, emailUtente,
							codiceFiscaleUtente, indirizzoUtente, tipoDoc, numero, enteRilascioDocumento,
							dataRilascioDocumento, dataScadenzaDocumento, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert(caught.getMessage());
								}

								@Override
								public void onSuccess(Integer result) {
									final DialogBox dialogBox = new DialogBox();
									dialogBox.setText("Remote Procedure Call");
									dialogBox.setAnimationEnabled(true);


									if (result == 1) {
										pannelloRegistrazione.clear();
										Window.alert("Registrazione effettuata, effettua il login!");
										autenticazione.ricarica();
										autenticazione.menuHome("home");
									} else if (result == -1) {
										Window.alert("Errore, inserisci tutti i dati");
									} else if (result == -2) {
										Window.alert("Password non coincidono");
										
									} else if (result == -3) {
										Window.alert("La passoword deve avere almeno 8 caratteri");
									} else if (result == -4) {
										Window.alert("Utente già registrato");
									} else
										Window.alert("R "+ result);

									
								} //end onSUccess
							});


				} else {
					Window.alert("Attenzione, tutti i campi sono obbligatori!!");
				}
			}// end onclick
		});
		
		return pannelloRegistrazione;
	} //end panel registazione
	
}