package com.aps.dmo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.standard.DecoratorFigure;

import com.aps.figures.ComponentFigure;
import com.aps.figures.InterfaceFigure;
import com.aps.figures.StereotipDecorator;

@Entity
@Table(name = "COMPONENT")
public class Komponenta implements java.io.Serializable {

	@Transient
	private static final long serialVersionUID = 3773457305815319422L;

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
	@JoinColumn(name = "DIAGRAM_ID")
	private Dijagram dijagram;

	@OneToMany(mappedBy = "komponenta", orphanRemoval = true, cascade = { CascadeType.ALL })
	private Collection<Interfejs> interfejsi = new ArrayList<Interfejs>();

	public Komponenta(Figure fig, Dijagram dijagram, ArrayList<Interfejs> modelInterfejsi) {
		this.posX = fig.displayBox().x;
		this.posY = fig.displayBox().y;
		this.width = fig.displayBox().width;
		this.height = fig.displayBox().height;
		this.dijagram = dijagram;

		if (fig instanceof DecoratorFigure) {
			while (fig instanceof DecoratorFigure) {
				if (fig instanceof StereotipDecorator)
					this.stereotip = true;
				else
					this.dekoracija = true;

				fig = ((DecoratorFigure) fig).peelDecoration();
			}
		}

		this.ime = ((ComponentFigure) fig).getName().getText().toString();

		for (InterfaceFigure ifig : ((ComponentFigure) fig).interfejsi) {
			Interfejs newInterface = new Interfejs();
			this.interfejsi.add(newInterface);
			modelInterfejsi.add(newInterface);
		}
	}

	public Komponenta() {

	}

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
