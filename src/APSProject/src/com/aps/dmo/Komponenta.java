package com.aps.dmo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Komponenta {
	@Id
	private int idKomponente;

	private int posX;
	private int posY;
	private int height;
	private int width;
	private String ime;
	private boolean stereotip;
	private boolean dekoracija;

	public int getIdKomponente() {
		return idKomponente;
	}

	public void setIdKomponente(int idKomponente) {
		this.idKomponente = idKomponente;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public boolean isStereotip() {
		return stereotip;
	}

	public void setStereotip(boolean stereotip) {
		this.stereotip = stereotip;
	}

	public boolean isDekoracija() {
		return dekoracija;
	}

	public void setDekoracija(boolean dekoracija) {
		this.dekoracija = dekoracija;
	}

}
