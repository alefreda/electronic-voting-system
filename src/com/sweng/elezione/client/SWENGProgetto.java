package com.sweng.elezione.client;


import com.google.gwt.core.client.EntryPoint;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SWENGProgetto implements EntryPoint {
private Autenticazione init = new Autenticazione();
	
	public void onModuleLoad() {
		
		init.ricarica();
	}
}
