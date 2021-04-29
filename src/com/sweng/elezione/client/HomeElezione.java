package com.sweng.elezione.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sweng.elezione.shared.Elezione;
import com.sweng.elezione.shared.Lista;
import com.sweng.elezione.shared.Utente;

public class HomeElezione {
	
	int numero = 0;
	int num = 0;
	String nomeVincitrice = "LISTA";
	Integer votoFinale = 2;
	String nomeElezione = "b";
	Date today = new Date();
	
	private final static ServizioElezioneAsync servizioElezione = GWT.create(ServizioElezione.class);
	private final ServizioAutenticazioneAsync servizioAutenticazione = GWT.create(ServizioAutenticazione.class);
	
	
	//panel per la creazione nuova elezione usati da admin e funzionario comunale
	public Panel creaElezione() {
		final Button creaElezione = new Button("Crea elezione");
		final VerticalPanel nuovaElezione = new VerticalPanel();
		nuovaElezione.setWidth("100%");
		nuovaElezione.setHeight("100%");
		nuovaElezione.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		nuovaElezione.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		nuovaElezione.add(creaElezione);
		
		
	    
		final TextBox nome = new TextBox();
		final Button crea = new Button("Crea");
		final Button indietro = new Button("Torna indietro");
		final TextBox tbDataInizio = new TextBox();
		final TextBox tbDataFine = new TextBox();
		
		creaElezione.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Accertati di inserire le date giuste!");
				Window.alert("La data deve essere yyyy-mm-dd ");
			
				nome.getElement().setAttribute("placeholder", "NomeElezione");
				tbDataInizio.getElement().setAttribute("placeholder", "Data inizio");
				tbDataFine.getElement().setAttribute("placeholder", "Data fine");

				nuovaElezione.add(nome);
				nuovaElezione.add(tbDataInizio);
				nuovaElezione.add(tbDataFine);
				nuovaElezione.add(crea);
				nuovaElezione.add(indietro);

				
				
				//click handler Crea
				crea.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						final String nomeElezione = nome.getText();
						
						//controllo se l'elezione creata esiste già
						servizioElezione.getElezioneNome(nomeElezione, new AsyncCallback<Elezione>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore");
							}

							@Override
							public void onSuccess(Elezione result) {
								
								//se null elezione non esiste dunque posso crearla
								if (result == null) {
									
									String dataInizio = tbDataInizio.getText();
									String dataFine = tbDataFine.getText();
									
									//crea elezione
									
									servizioElezione.creaElezione(nomeElezione, dataInizio, dataFine, new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert(caught.getMessage());
											
										}

										@Override
										public void onSuccess(Boolean result) {
											Window.alert("Elezione creata!");
											nuovaElezione.clear();
											
										}
										
									}); //end RPC crea elezione
									
									
								} else {
									Window.alert("L'elezione già esiste!");
								}
							} //end onSuccess
							
						});
						
					} //fine onclick
				}); //fine crea elezione


				//torna indietro
				indietro.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Autenticazione autenticazione = new Autenticazione();
						autenticazione.ricarica();
						autenticazione.menuHome("home");
					}
				}); //fine click handler ritorna indietro
				
				
				
			} //fine onClick crea elezione
		}); //fine crea elezione
		
		return nuovaElezione;
	} //end panel crea elezione

	
	//mostra lista utenti con possibilita di renderli funzionari
	//si riferisce ad utenti
	//visualizzabile solo all'admin
	public Panel listaUtenti(){

		final VerticalPanel panelListaCittadini = new VerticalPanel();
		final VerticalPanel table = new VerticalPanel();
		
		final Button listaUtenti = new Button("Vedi utenti registrati");
		final Button chiudi = new Button("Chiudi");
		
		chiudi.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Autenticazione autenticazione = new Autenticazione();
				autenticazione.ricarica();
				autenticazione.menuHome("home");
			}
		});
		
		
		listaUtenti.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final FlexTable listaTabella = new FlexTable();
				listaUtenti.setVisible(false);
				table.clear();
				table.add(listaTabella);

				servizioAutenticazione.caricaUtenti(new AsyncCallback<ArrayList<Utente>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore caricamento utenti");
						
					}

					//ritorna una lista di utenti
					@Override
					public void onSuccess(ArrayList<Utente> result) {
							
						
						
						//se lista vuota non ci sono utenti
						if (result.size() == 0) {
							
							Window.alert("Non esistono utenti registrati");
							
						} else {
							for (Utente utente : result) {

								Button bottoneNominaFunzionario = new Button("Nomina Funzionario");
								
								//se utente è già registrato non mostro il bottone
								if (utente.isFunzionario()) {
									bottoneNominaFunzionario.setVisible(false);
								}

								//nomina utente come funzionario
								bottoneNominaFunzionario.addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {

										int riga = listaTabella.getCellForEvent(event).getRowIndex();

										String nomeFunzionario = listaTabella.getText(riga, 0);
										
										servizioAutenticazione.rendiFunzionario(nomeFunzionario,
												new AsyncCallback<Boolean>() {
													@Override
													public void onFailure(Throwable caught) {
														Window.alert("Errore.");
													}

													@Override
													public void onSuccess(Boolean result) {

														if (result) {
															listaUtenti.click();
														} else {
															Window.alert("Errore ");
														}
													}
												});
									}
								});
								
								
								int rowNum = listaTabella.getRowCount();
								listaTabella.setText(rowNum, 0, utente.getUsername());
								
								//se utente è funzionario visualizza solo nome e funzionario
								if (utente.isFunzionario()) {
									listaTabella.setText(rowNum, 1, "Funzionario");
								} else {
									listaTabella.setText(rowNum, 1, "Utente");
								}
								
								listaTabella.setWidget(rowNum, 2, bottoneNominaFunzionario);
							}
							table.add(chiudi);
						}
						
						
						
					}
					
				}); //end RPC che carica gli utenti
			}
		}); // end click hendler
		
		
		panelListaCittadini.add(listaUtenti);
		panelListaCittadini.add(table);

		return panelListaCittadini;
		
	}


	
	//panel visibile solo ad utente registrato
	//panel per la creazione della lista elettorale
	//utilizzato da utente registrato
	public Panel creazioneListaElettorale() {

		final VerticalPanel panelNuovaListaElettorale = new VerticalPanel();
		final Button btnCreaLista = new Button("Crea Lista Elettorale");
		final ListBox lbNomeElezione = new ListBox();
		final TextBox tbNomeLista = new TextBox();
		final TextBox tbNomeSimbolo = new TextBox();
		final ListBox seSindaco = new ListBox();
		final ArrayList<CheckBox> tuttiCB = new ArrayList<CheckBox>();
		final Label sindaco = new Label("Scegliere il candidato sindaco.");
		final Button fatto = new Button("Fatto");
		final Button indietro = new Button("Torna indietro");
		

		lbNomeElezione.getElement().setAttribute("placeholder", "Nome Elezione");
		tbNomeLista.getElement().setAttribute("placeholder", "Nome Lista");
		tbNomeSimbolo.getElement().setAttribute("placeholder", "Nome Simbolo");
		panelNuovaListaElettorale.add(btnCreaLista);

		// Definisco cosa accade quando clicco su back

		indietro.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Autenticazione uiManager = new Autenticazione();
				uiManager.ricarica();
				uiManager.menuHome("home");
			}
		});

		// controlla prima di tutto se ci sono elezioni 
		btnCreaLista.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				servizioElezione.listaElezioni(new AsyncCallback<ArrayList<Elezione>>() {

					@Override
					public void onFailure(Throwable caught) {
						System.out.println(caught.getLocalizedMessage());

					}

					@Override
					public void onSuccess(final ArrayList<Elezione> result) {
						Window.alert("Il sindaco non puo essere un cadidato!");
						//lista delle elezioni da popolare
						final ArrayList<Elezione> tutteElezioni = new ArrayList<>();
						
						if (result.size() == 0) {
							Window.alert("Nessuna elezione nel sistema.");
						} else {
							for (int i = 0; i < result.size(); i++) {
								Elezione elezione = result.get(i);
								String tmp_nome = elezione.getNomeElezione();
								lbNomeElezione.addItem(tmp_nome);
								tutteElezioni.add(elezione);
							}
							//carico gli utenti registrati
							servizioAutenticazione.caricaUtenti(new AsyncCallback<ArrayList<Utente>>() {
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nel caricare i cittadini: " + caught.getMessage());
								}

								@Override
								public void onSuccess(ArrayList<Utente> result) {

									for (Utente cittadino : result) {
										String tmp_name = cittadino.getUsername();

										seSindaco.addItem(tmp_name);
										tuttiCB.add(new CheckBox(tmp_name.toString()));

									}

									panelNuovaListaElettorale.clear();
									panelNuovaListaElettorale.add(new Label("Scegli elezione"));
									panelNuovaListaElettorale.add(lbNomeElezione);
									panelNuovaListaElettorale.add(sindaco);
									panelNuovaListaElettorale.add(seSindaco);
									panelNuovaListaElettorale.add(tbNomeLista);
									panelNuovaListaElettorale.add(tbNomeSimbolo);
									panelNuovaListaElettorale.add(new Label("Scegli i candidati"));
									for (CheckBox utenteDaCeckare : tuttiCB) {
										panelNuovaListaElettorale.add(utenteDaCeckare);
									}

									for (CheckBox elezioniListaDaScegliere : tuttiCB)
										panelNuovaListaElettorale.add(elezioniListaDaScegliere);
									panelNuovaListaElettorale.add(fatto);
									panelNuovaListaElettorale.add(indietro);
								}
							});
						}

					}
				});

				//crea lista elettorale
				fatto.addClickHandler(new ClickHandler() {
					boolean esiste = false;

					@Override
					public void onClick(ClickEvent event) {

						String nomeElezione = lbNomeElezione.getValue(lbNomeElezione.getSelectedIndex());
						final String nomeLista = tbNomeLista.getText();
						//RPC per caricare la lista delle liste elettorali di una elezione
						servizioElezione.listaListeElettoraliDiElezione(nomeElezione,
								new AsyncCallback<ArrayList<Lista>>() {

									@Override
									public void onFailure(Throwable caught) {
										System.out.println(caught.getMessage());
									}

									@Override
									public void onSuccess(ArrayList<Lista> result) {
										for (int i = 0; i < result.size(); i++) {
											if (result.get(i).getNomeLista().equals(nomeLista)) {
												Window.alert(
														"Lista già esistente cambia nome!.");
												esiste = true;
											}
										}
									}
								});

						if (esiste == false) {
							String nomeSindaco = seSindaco.getValue(seSindaco.getSelectedIndex());
							String nomeSimbolo = tbNomeSimbolo.getText();
							if (nomeElezione.length() > 0 && nomeLista.length() > 0 && nomeSindaco.length() > 0
									&& nomeSimbolo.length() > 0) {
								ArrayList<String> listaCandidati = new ArrayList<String>();
								for (CheckBox cittadinoCB : tuttiCB) {
									if (!cittadinoCB.getText().equals(nomeSindaco)) {
										if(cittadinoCB.getValue()) {
											listaCandidati.add(cittadinoCB.getText());		
										}
									}

								}

								if (listaCandidati.size() != 0) {
									servizioElezione.creaListaElettorale(nomeElezione, nomeLista, nomeSindaco,
											nomeSimbolo, listaCandidati, new AsyncCallback<Boolean>() {

												@Override
												public void onSuccess(Boolean result) {
													Window.alert("Successo!");
												}

												@Override
												public void onFailure(Throwable caught) {
													System.out.println("Errore: " + caught.getLocalizedMessage());

												}
											});
								} else
									Window.alert("Non hai selezionato alcun componente.");
							} else
								Window.alert("Tutti i campi devono sono obbligatori.");
						}
					}
				});
			}
		});
		return panelNuovaListaElettorale;
	}

	//panel che mostra la lista delle liste elettorali ancora da accettare dal funzionario comunale
	//visibile solo al funzionario comunale
	public Panel listeElettorali() {
		final VerticalPanel panelListaUtenti = new VerticalPanel();
		final Button listeElettorali = new Button("Visualizza richieste lista");
		final Button chiudi = new Button("Chiudi");
		final VerticalPanel panelTableContainer = new VerticalPanel();
		panelListaUtenti.add(listeElettorali);

		chiudi.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Autenticazione autenticazione = new Autenticazione();
				autenticazione.ricarica();
				autenticazione.menuHome("home");
			}
		});

		listeElettorali.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final FlexTable tableListaListeElettorali = new FlexTable();
				panelTableContainer.clear();
				panelTableContainer.add(tableListaListeElettorali);

				servizioElezione.listaListeElettorali(new AsyncCallback<ArrayList<Lista>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(ArrayList<Lista> result) {

						for (Lista listaElettorale : result) {

							Button btnAccettaLista = new Button("Accetta");
							btnAccettaLista.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {

									int rigaIndex = tableListaListeElettorali.getCellForEvent(event).getRowIndex();

									String nomeLista = tableListaListeElettorali.getText(rigaIndex, 0);

									servizioElezione.approveRejectLista(1, nomeLista,
											new AsyncCallback<Boolean>() {
												@Override
												public void onFailure(Throwable caught) {
													System.out.println(caught.getLocalizedMessage());
												}

												@Override
												public void onSuccess(Boolean result) {
													if (result) {
														listeElettorali.click();
														Window.alert("Hai approvato la lista!");
													}
												}
											});

								}
							});

							Button btnRigettaLista = new Button("Rigetta");
							btnRigettaLista.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {

									int rowIndex = tableListaListeElettorali.getCellForEvent(event).getRowIndex();

									String nomeLista = tableListaListeElettorali.getText(rowIndex, 0);

									servizioElezione.approveRejectLista(2, nomeLista,
											new AsyncCallback<Boolean>() {
												@Override
												public void onFailure(Throwable caught) {
													
													System.out.println(caught.getLocalizedMessage());
												}

												@Override
												public void onSuccess(Boolean result) {
													if (result) {
														listeElettorali.click();
														Window.alert("Hai rifiutato la lista!");
													}
												}
											});

								}
							});

							int rigaNum = tableListaListeElettorali.getRowCount();
							tableListaListeElettorali.setText(rigaNum, 0, listaElettorale.getNomeLista());
							tableListaListeElettorali.setText(rigaNum, 1,
									Integer.toString(listaElettorale.getTotalVoti()));
							tableListaListeElettorali.setText(rigaNum, 5, listaElettorale.getElezione());

							if (listaElettorale.getStatoApprovazione() == 0) {
								tableListaListeElettorali.setText(rigaNum, 2, "IN ATTESA");
								tableListaListeElettorali.setWidget(rigaNum, 3, btnAccettaLista);
								tableListaListeElettorali.setWidget(rigaNum, 4, btnRigettaLista);
							} else if (listaElettorale.getStatoApprovazione() == 1) {
								tableListaListeElettorali.removeRow(rigaNum);
							} else {
								tableListaListeElettorali.removeRow(rigaNum);
							}
						}

						panelTableContainer.add(chiudi);
					}
				});
			}
		});
		panelListaUtenti.add(panelTableContainer);

		return panelListaUtenti;
	}

	
	
	
	
	
	//mostra la lista delle elezioni, permette di mostrare i dettagli di ogni elezione
	//in questo caso richiama un altro panel
	public Panel mostraListaElezioni() {
			
			final VerticalPanel panelListaElezioni = new VerticalPanel();
			final VerticalPanel panelTableContainer = new VerticalPanel();
			
			final Button caricaListaElezioni = new Button("Vota");
			final Button btnBack = new Button("Back");
	
			btnBack.addClickHandler(new ClickHandler() {
	
				@Override
				public void onClick(ClickEvent event) {
					Autenticazione autenticazione = new Autenticazione();
					autenticazione.ricarica();
					autenticazione.menuHome("home");
				}
			});
	
			caricaListaElezioni.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					panelTableContainer.clear();
					
					//RPC per la lista della elezioni presenti nel sistema
					servizioElezione.listaElezioni(new AsyncCallback<ArrayList<Elezione>>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore nel caricare tutte le elezioni: " + caught.getLocalizedMessage());
						}
	
						@Override
						public void onSuccess(final ArrayList<Elezione> result) {
							final FlexTable tableLista = new FlexTable();
							if (result.size() == 0) {
								Window.alert("Non ci sono elezioni presenti nel sistema.");
							} else {
								panelTableContainer.clear();
								panelTableContainer.add(tableLista);
	
								tableLista.setText(0, 0, "Nome Elezione");
								tableLista.setText(0, 1, "Data Inizio");
								tableLista.setText(0, 2, "Data Conclusione");
								tableLista.setText(0, 3, "Dettagli Elezione");
	
								//per ogni elezione presente nel sistema prende i dati e li mostra
								for (Elezione elezione : result) {
									Button dettagliElezione = new Button("Vedi le liste candidate");
									int rowNum = tableLista.getRowCount();
									tableLista.setText(rowNum, 0, elezione.getNomeElezione());
									tableLista.setText(rowNum, 1, elezione.getData_inizio());
									tableLista.setText(rowNum, 2, elezione.getData_fine());
									tableLista.setWidget(rowNum, 3, dettagliElezione);
	
									//mostra i dettagli dell'elezione, aggiunge altro panel
									dettagliElezione.addClickHandler(new ClickHandler() {
										@Override
										public void onClick(ClickEvent event) {
											int rowIndex = tableLista.getCellForEvent(event).getRowIndex();
											String nomeElezione = tableLista.getText(rowIndex, 0);
											
											//chiama altro panel
											panelTableContainer.add(mostraListaDataElezione(nomeElezione));
										}
									});
								}
							}
							if (tableLista.getRowCount() == 0 && result.size() != 0) {
								Window.alert("Non ci sono liste candidate per questa elezione");
							}
						}
					});
				}
			});
	
			panelListaElezioni.add(caricaListaElezioni);
			panelListaElezioni.add(panelTableContainer);
	
			return panelListaElezioni;
	
		}


	
	//mostra la lista delle liste elettorali per una elezione
	//permette di mostrare la lista dei candidati per quella lista elettorale 
	//chiama un altro panel
	public Panel mostraListaDataElezione(final String elezione) {
		final VerticalPanel panelListaCittadini = new VerticalPanel();
		final VerticalPanel panelTableContainer = new VerticalPanel();
		final FlexTable tableListaListeElettorali = new FlexTable();

		// Carico le liste elettorali per una data elezione
		servizioElezione.listaListeElettoraliDiElezione(elezione, new AsyncCallback<ArrayList<Lista>>() {
			int listeApprovate = 0;

			@Override
			public void onSuccess(ArrayList<Lista> result) {
				if (result.size() != 0) {
					tableListaListeElettorali.setText(0, 0, "Nome Lista");
					tableListaListeElettorali.setText(0, 1, "Nome Elezione");
					tableListaListeElettorali.setText(0, 2, "Visualizza Candidati");
					tableListaListeElettorali.setText(0, 3, "Vota solo lista");
					for (int i = 1; i <= result.size(); i++) {
						final Lista listaElettorale = result.get(i - 1);
						if (listaElettorale.getStatoApprovazione() == 1) {
							listeApprovate++;
							final Button votaLista = new Button("Vota la Lista");
							final Button mostraCandidatiLista = new Button("Vedi i candidati");
							tableListaListeElettorali.setText(i, 0, listaElettorale.getNomeLista());
							tableListaListeElettorali.setText(i, 1, listaElettorale.getElezione());
							tableListaListeElettorali.setWidget(i, 2, mostraCandidatiLista);
							tableListaListeElettorali.setWidget(i, 3, votaLista);

							mostraCandidatiLista.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									
									int rowIndex = tableListaListeElettorali.getCellForEvent(event).getRowIndex();
									String nomeLista = tableListaListeElettorali.getText(rowIndex, 0);
									//chiama altro panel per i dettagli della lista
									panelListaCittadini.add(mostraDettagliLista(nomeLista));
								}
							});

							votaLista.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {

									int rowIndex = tableListaListeElettorali.getCellForEvent(event).getRowIndex();
									String nomeElezione = tableListaListeElettorali.getText(rowIndex, 0);

									servizioElezione.vota(nomeElezione, new AsyncCallback<Boolean>() {

										@Override
										public void onSuccess(Boolean result) {
											if(result == true)
												Window.alert("Hai votato la lista");
											else
												Window.alert("Hai gia' votato per questa elezione");
										}

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Errore nella votazione: "+caught.getLocalizedMessage());
										}
									});

								}
							});
						}
					}

				} else if (result.size() != 0 && listeApprovate == 0) {
					Window.alert("Nessuna lista approvata per questa elezione");
				} else if (result.size() == 0) {
					Window.alert("Nessuna lista candidata per questa elezione");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("E2: " + caught.getMessage());
			}
		});
		panelTableContainer.clear();
		panelTableContainer.add(tableListaListeElettorali);
		panelListaCittadini.add(panelTableContainer);
		return panelListaCittadini;
	}

	//mostra dettagli data una lista eletterale
	//questo panel viene chiamato anche dal panel creazioneListaElettorale
	public Panel mostraDettagliLista(final String nomeLista) {

		final VerticalPanel panelContainer = new VerticalPanel();
		final FlexTable tableListaCandidati = new FlexTable();

		// Carica tutti i candidati della lista per votarli
		servizioElezione.partecipantiListaElettorale(nomeLista, new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("E1: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<String> candidati) {

				for (String nomeCandidato : candidati) {
					Button votaCandidato = new Button("Vota Candidato");
					votaCandidato.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							int rowIndex = tableListaCandidati.getCellForEvent(event).getRowIndex();

							String nomeCandidato = tableListaCandidati.getText(rowIndex, 0);

							servizioElezione.votaCandidato(nomeLista, nomeCandidato,
									new AsyncCallback<Boolean>() {

										@Override
										public void onSuccess(Boolean result) {
											if (result) {
												Window.alert("Complimenti, hai votato il candidato!");
											} else
												Window.alert("Hai gia' votato per questa elezione.");
										}

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("E1: " + caught.getMessage());
										}
									});
						}
					});
					int rowNum = tableListaCandidati.getRowCount();
					tableListaCandidati.setText(rowNum, 0, nomeCandidato);
					tableListaCandidati.setWidget(rowNum, 1, votaCandidato);
				}

			}
		});
		Label label = new Label("Candidati della lista: " + nomeLista);
		panelContainer.clear();
		panelContainer.add(label);
		panelContainer.add(tableListaCandidati);

		return panelContainer;
	}

	
	//panel visibile ad utente visitatore
	public Panel mostraElezioniConcluse() {
			
	
			final VerticalPanel panelElezioniConcluse = new VerticalPanel();
			final VerticalPanel panelTableContainer = new VerticalPanel();
			final Button aggiungiLista = new Button("Carica Lista Elezioni Concluse.");
			final Button indietro = new Button("Back");
	
			indietro.addClickHandler(new ClickHandler() {
	
				@Override
				public void onClick(ClickEvent event) {
					Autenticazione uiManager = new Autenticazione();
					uiManager.ricarica();
					uiManager.menuHome("home");
				}
			});
	
			aggiungiLista.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					panelTableContainer.clear();
	
					// Vengono caricate tutte le elezioni
					servizioElezione.listaElezioni(new AsyncCallback<ArrayList<Elezione>>() {
	
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore nel caricare le elezioni concluse.");
						}
	
						@Override
						public void onSuccess(ArrayList<Elezione> result) {
							ArrayList<Elezione> elezioniConcluse = new ArrayList<>();
	
							final FlexTable tableLista = new FlexTable();
							panelTableContainer.clear();
							// Per determinare se sia conclusa o meno, fa il confronto con la data odierna
							//Date dataOdierna = new Date();
							//Date dataOdierna = Calendar.getInstance().getTime();
							// Per mostrare che sono concluse
							//dataOdierna.setYear(2018);
							//dataOdierna.setMonth(11);
	
							//Date date = null;
							for (Elezione elezione : result) {
								//date = convertiStringaInData(elezione.getData_fine());
								//numero = dataOdierna.compareTo(date);
								//if (numero > 0) {
									elezioniConcluse.add(elezione);
								//}
							}
							
							if (elezioniConcluse.size() > 0) {
								tableLista.setText(0, 0, "Nome elezione-");
								tableLista.setText(0, 1, "Nome lista vincitrice-");
								tableLista.setText(0, 2, "Voti Finali");
								
								
	
						
								for (int i = 0; i < elezioniConcluse.size(); i++) {
									votoFinale = 2;
									nomeVincitrice = "a";
									nomeElezione = elezioniConcluse.get(i).getNomeElezione();
									//Window.alert("Ora il nome elezione è : " + nomeElezione);
								
							
									// per ogni elezione, vengono caricate le liste elettorali
									servizioElezione.listaListeElettoraliDiElezione(nomeElezione,
											new AsyncCallback<ArrayList<Lista>>() {
	
												@Override
												public void onSuccess(ArrayList<Lista> result) {
	
													ArrayList<Lista> listeElettorali = new ArrayList<>();
													for (int j = 0; j < result.size(); j++) {
														listeElettorali.add(result.get(j));
													}
												
	
													for (int k = 0; k < listeElettorali.size(); k++) {
														
														// Vengono chiesti i voti delle singole liste elettorali
														final String nomeLista = listeElettorali.get(k).getNomeLista();
														
														servizioElezione.numeroVotiListaElettorale(nomeLista,
																new AsyncCallback<Integer>() {
	
																	// Vengono confrontati i risultati ottenuti
																	@Override
																	public void onSuccess(Integer result) {
																		if (votoFinale < result) {
																			votoFinale = result;
																			nomeVincitrice = nomeLista;
																		}
																		
																		
	
																	}
	
																	@Override
																	public void onFailure(Throwable caught) {
																		System.out.println(caught.getLocalizedMessage());
																	}
																});
														
														
													} // end for
													
												
												}
	
												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Errore nel caricare le liste dell'elezione.");
												}
												
											}); //fine servizio
	
								
									int rowNum = tableLista.getRowCount();
									Window.alert("ora nome vincitrice: " + nomeVincitrice);
									tableLista.setText(rowNum, 0, nomeElezione);
									tableLista.setText(rowNum, 1, nomeVincitrice);
									tableLista.setText(rowNum, 2, votoFinale.toString());
									
								} //end for per ogni elezione
								

								
								panelTableContainer.add(tableLista);
	
								panelTableContainer.add(indietro);
	
							} else {
								Window.alert("Al momento non sono presenti elezioni concluse.");
							}
						}
					});
				}
			});
			panelElezioniConcluse.add(aggiungiLista);
			panelElezioniConcluse.add(panelTableContainer);
			return panelElezioniConcluse;
		}

	public Date convertiStringaInData(String a) {
		Date nuovaData = null;
		try {
			nuovaData = DateTimeFormat.getFormat("yyyy-MM-dd").parse(a);
		} catch (IllegalArgumentException iae) {
			Window.alert(
					"Impossibile ottenere la data. Controlla che il formato yyyy-MM-dd sia inserito correttamente!");
		}
		return nuovaData;
	}
	
	
}
