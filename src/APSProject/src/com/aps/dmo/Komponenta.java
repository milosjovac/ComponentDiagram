package com.aps.dmo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "COMPONENT")
public class Komponenta {
	@Id
	@GeneratedValue
	@Column(name = "COMPONENT_ID")
	private int idKomponente;

	@Column(name = "POSITION_X")
	private int posX;

	@Column(name = "POSITION_Y")
	private int posY;

	@Column(name = "HEIGHT")
	private int height;

	@Column(name = "WIDTH")
	private int width;

	@Column(name = "NAME")
	private String ime;

	@Column(name = "STEREOTYPE")
	private boolean stereotip;

	@Column(name = "DECORATION")
	private boolean dekoracija;

	@ManyToOne 
	@JoinColumn (name = "DIAGRAM_ID")
	private Dijagram dijagram;
	
	@OneToMany (mappedBy="komponenta")
	private Collection<Interfejs> interfejsi = new ArrayList<Interfejs>();

	public Collection<Interfejs> getInterfejsi() {
		return interfejsi;
	}

	public void setInterfejsi(Collection<Interfejs> interfejsi) {
		this.interfejsi = interfejsi;
	}

	public Dijagram getDijagram() {
		return dijagram;
	}

	public void setDijagram(Dijagram dijagram) {
		this.dijagram = dijagram;
	}

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
