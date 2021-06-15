package calc;

import calc.HojaCalculo.Formula;

public class Casilla {

	
	private Coordenada posicion; // posicion; permite a la celdilla saber su lugar en el mundo
	private int contenido;
	private boolean esFormula;
	private Formula formula;

	/**
	 * Constructor de la clase Casilla que toma como argumento la posición y asigne el contenido inicial 0
	 * a la nueva casilla
	 * @param posicion
	 */
	public Casilla(Coordenada posicion) {
		this.posicion = posicion;
		this.contenido = 0;
	}

	/**
	 * GETTERS & SETTERS
	 */
	
	public boolean isEsFormula() {
		return esFormula;
	}
	
	public void setEsFormula(boolean esFormula) {
		this.esFormula = esFormula;
	}

	public Formula getFormula() {
		return formula;
	}

	
	public void setFormula(Formula f) {
		this.formula = f;
	}

	
	public Coordenada getPosicion() {
		return posicion;
	}

	
	public void setPosicion(Coordenada posicion) {
		this.posicion = posicion;
	}

	
	public int getContenido() {
		return contenido;
	}

	
	public void setContenido(int contenido) {
		this.contenido = contenido;
	}


	/**
	 * Método público cantidadDelNum, que calcula la cantidad de dígitos por los que está compuesto number
	 * @return cantidad de dígitos de number
	 */
	public int cantidadDeNumero() {
		return Integer.toString(contenido).length();
	}
}