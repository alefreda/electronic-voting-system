package com.sweng.elezione.shared;

import java.io.Serializable;
import java.util.Date;

public class DocumentoIdentita implements Serializable {

	private static final long serialVersionUID = -7915365060598276411L;
	private String tipo, numero, ente_rilascio;
	Date scadenza;
	Date data_rilascio;

	public DocumentoIdentita() {

	}

	public DocumentoIdentita(String tipo, String numero, String ente_rilascio, Date dataRilascio, Date dataScadenza) {
		this.setTipo(tipo);
		this.setNumero(numero);
		this.setEnte_rilascio(ente_rilascio);
		this.setData_rilascio(dataRilascio);
		this.setScadenza(dataScadenza);

	}

	public Date getScadenza() {
		return scadenza;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getData_rilascio() {
		return data_rilascio;
	}

	public void setData_rilascio(Date data_rilascio) {
		this.data_rilascio = data_rilascio;
	}

	public String getEnte_rilascio() {
		return ente_rilascio;
	}

	public void setEnte_rilascio(String ente_rilascio) {
		this.ente_rilascio = ente_rilascio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

}