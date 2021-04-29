package com.sweng.elezione.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;





public class Autenticazione {
	
	private final ServizioAutenticazioneAsync servizioAutenticazione = GWT.create(ServizioAutenticazione.class);
	private final VerticalPanel mainPanel = new VerticalPanel();
	private final VerticalPanel contentPanel = new VerticalPanel();
	@SuppressWarnings("unused")
	private boolean utenteLoggato = false;

	
	
	public void ricarica() {
		RootPanel.get().clear();
		RootPanel.get().add(contentPanel);

		final VerticalPanel verticalPanel = this.mainPanel;

		contentPanel.clear();

		contentPanel.add(menu());
		contentPanel.add(verticalPanel);
		menu();

	}

	public void menuHome(String section) {
		this.mainPanel.clear();
		if (section.contentEquals("login")) {
			LoginRegistrazione logReg = new LoginRegistrazione();
			this.mainPanel.add(logReg.login());
		} else if (section.contentEquals("registrazione")) {
			LoginRegistrazione logReg = new LoginRegistrazione();
			this.mainPanel.add(logReg.registrazione());
		} else if (section.contentEquals("home")) {
			Home home = new Home();
			this.mainPanel.add(home.homePage());
		}
	}

	
	//panel per menu
	public Panel menu() {
	
		
		final Button btnLogin = new Button("Login");
		final Button btnRegister = new Button("Registrazione");
		final Button btnDashboard = new Button("Home");
		final Button btnLogout = new Button("Logout");
		

		final HorizontalPanel pannelloMenu = new HorizontalPanel();
		pannelloMenu.clear();
		
		servizioAutenticazione.utenteLoggato(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore");
				
			}

			@Override
			public void onSuccess(Boolean result) {
				
				if (result) {
					pannelloMenu.add(btnLogout);
					pannelloMenu.add(btnDashboard);
				} else {
					pannelloMenu.add(btnLogin);
					pannelloMenu.add(btnRegister);
					pannelloMenu.add(btnDashboard);
					
				}
				
			}
			
		});
		



		btnLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				menuHome("login");
			};
		});

		btnRegister.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				menuHome("registrazione");
			};
		});

		btnDashboard.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				menuHome("home");
			};
		});

		
		
		btnLogout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				servizioAutenticazione.logout(new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Window.alert("Logout effettuato.");
							ricarica();
							

							utenteLoggato = false;
							menuHome("login");
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("Errore durante il logout" + caught.getMessage());
					}
				});

			}
		});

		return pannelloMenu;
	}
	

	
	
	
	
}
