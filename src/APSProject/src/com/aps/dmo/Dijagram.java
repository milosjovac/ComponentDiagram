package com.aps.dmo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity(name = "DIAGRAM")
public class Dijagram implements java.io.Serializable {

	@Transient
	private static final long serialVersionUID = 3773858300015319422L;

	@Id
	@GeneratedValue
	@Column(name = "DIAGRAM_ID")
	private int id;

	@OneToMany(mappedBy = "dijagram")
	private Collection<Komponenta> komponente = new ArrayList<Komponenta>();
	@Column(name = "NAME")
	private String ime;
	@Column(name = "DATE")
	private Date date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Collection<Komponenta> getKomponente() {
		return komponente;
	}

	public void setKomponente(Collection<Komponenta> komponente) {
		this.komponente = komponente;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
