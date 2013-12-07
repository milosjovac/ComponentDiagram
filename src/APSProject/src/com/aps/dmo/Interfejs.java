package com.aps.dmo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.aps.figures.InterfaceEmptyFigure;
import com.aps.figures.InterfaceFigure;

@Entity(name = "INTERFACE")
public class Interfejs implements java.io.Serializable {

	@Transient
	private static final long serialVersionUID = 3773858305815319422L;

	@Id
	@GeneratedValue
	@Column(name = "INTERFACE_ID")
	private int id;

	@Column(name = "POSITION_X")
	private int posX;
	@Column(name = "POSITION_Y")
	private int posY;
	@Column(name = "TYPE")
	private boolean tip;

	@Column(name = "Name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "COMPONENT_ID")
	private Komponenta komponenta;

	@OneToMany
	@JoinTable(name = "INTERFACE_DEPENDECY", joinColumns = @JoinColumn(name = "INT_FROM"), inverseJoinColumns = @JoinColumn(name = "INT_TO"))
	private Collection<Interfejs> interfejsi = new ArrayList<Interfejs>();

	public Interfejs() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public boolean isTip() {
		return tip;
	}

	public void setTip(boolean tip) {
		this.tip = tip;
	}

	public Komponenta getKomponenta() {
		return komponenta;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKomponenta(Komponenta komponenta) {
		this.komponenta = komponenta;
	}

	public Collection<Interfejs> getInterfejsi() {
		return interfejsi;
	}

	public void setInterfejsi(Collection<Interfejs> interfejsi) {
		this.interfejsi = interfejsi;
	}
}
