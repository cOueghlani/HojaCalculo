package calc;

/**
 * @author Cristina Oueghlani
 * 
 *  Clase Coordenada: servirá para localizar la dirección absoluta
 *         de cada celdilla
 */

public class Coordenada {
	private int fila;
	private int columna;

	/**
	 * Constructora de la clase coordenada
	 * @param columna
	 * @param fila
	 */
	public Coordenada(int columna, int fila) {
		this.fila = fila;
		this.columna = columna;
	}

	
	/**
	 * Constructor de la clase Coordenada
	 * @param ref
	 */
	public Coordenada(String ref) {
		// B3=8475
		String alfabeto = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";

		this.fila = Character.getNumericValue(ref.charAt(1)); // 3
		this.columna = alfabeto.indexOf(Character.toUpperCase(ref.charAt(0))); // B
	}

	/**
	 * GETTERS AND SETTERS
	 */
	public int getFila() {
		return fila;
	}

	
	public void setFila(int fila) {
		this.fila = fila;
	}

	
	public int getColumna() {
		return columna;
	}

	
	public void setColumna(int columna) {
		this.columna = columna;
	}

	@Override
	public String toString() {
		return "(" + fila + "," + columna + ")";
	}

}
