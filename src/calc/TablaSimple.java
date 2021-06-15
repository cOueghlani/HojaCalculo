package calc;

import java.util.Scanner;

/**
 * Clase TablaSimple
 * 
 * @author Cristina Oueghlani
 */
public class TablaSimple {

	private int altura; // fila
	private int anchura; // columna
	private Casilla[][] celdas;

	protected int[] anchoColumna;

	
	/**
	 * Constructora de la clase TablaSimple
	 * @param altura
	 * @param anchura
	 */
	public TablaSimple(int altura, int anchura) {
		this.anchura = anchura;
		this.altura = altura;
		this.anchoColumna = new int[anchura];
		celdas = new Casilla[altura][anchura];

		for (int i = 0; i < altura; i++) {
			for (int j = 0; j < anchura; j++) {
				celdas[i][j] = new Casilla(new Coordenada(i, j));
			}
		}
	}

	
	protected int getAltura() {
		return altura;
	}

	protected int getAnchura() {
		return anchura;
	}

	
	/**
	 * esLegal: comprueba si un objeto de tipo Coordenada encaja en las dimensiones fijas de una TablaSimple
	 * 
	 * @param coordenada
	 * @return
	 */
	public boolean esLegal(Coordenada coordenada) {

		if ((0 <= coordenada.getColumna()) && (0 <= coordenada.getFila()) && (coordenada.getColumna() < this.anchura)
				&& (coordenada.getFila() < this.altura)) {
			return true;
		}
		return false;

	}

	/**
	 * GETTERS & SETTERS
	 */
	
	public int getContenido(Coordenada coor) {
		Casilla contenido = celdas[coor.getColumna()][coor.getFila()];
		return contenido.getContenido();
	}

	
	public Casilla getCasilla(Coordenada coor) {
		return celdas[coor.getColumna()][coor.getFila()];
	}

	
	public void setCasilla(Coordenada coor, Casilla casilla) {
		celdas[coor.getColumna()][coor.getFila()] = casilla;
	}

	
	public void setContenido(Coordenada coor, int i) {
		
		if(celdas[coor.getColumna()][coor.getFila()].isEsFormula()) {
			celdas[coor.getColumna()][coor.getFila()].setEsFormula(false);
		}
		
		celdas[coor.getColumna()][coor.getFila()].setContenido(i);
	}

	
	/**
	 * Método calcularAnchoColumnas guarda el espacio necesario para imprimir bien los valores de cada columna
	 */
	protected void calcularAnchoColumnas() {

		int tamaino = Integer.MIN_VALUE;

		for (int j = 0; j < anchura; j++) {
			for (int i = 0; i < altura; i++) {

				if (celdas[i][j].cantidadDeNumero() > tamaino) {
					tamaino = celdas[i][j].cantidadDeNumero();
				}

			}
			anchoColumna[j] = tamaino;
			tamaino = Integer.MIN_VALUE;
		}

	}

	
	/**
	 * Método charChain Devuelve el caracter c tantas veces como se indique por parametro
	 * @param n
	 * @param c
	 * @return cadena de caracteres
	 */
	protected String charChain(int n, char c) {
		if (n == 0) {
			return "";
		} else {
			return c + charChain(n - 1, c);
		}
	}

	
	/**
	 * Metodo toString, genera una Tabla
	 */
	public String toString() {
		calcularAnchoColumnas();
		String resultado = "-";
		
		System.out.println();
		
		for (int l = 0; l < anchura; l++) {
			resultado += charChain(anchoColumna[l] + 3, '-'); // tejado entero
		}

		resultado += " \n";

		for (int i = 0; i < altura; i++) {

			for (int j = 0; j < anchura; j++) {
				Casilla casilla = getCasilla(new Coordenada(i, j));

				if (casilla.cantidadDeNumero() == anchoColumna[j])
					resultado += "|" + " " + casilla.getContenido() + " ";

				else {
					int restante = anchoColumna[j] - casilla.cantidadDeNumero();
					resultado += "|" + " " + charChain(restante, ' ') + casilla.getContenido() + " ";
				}
			}
			resultado += "| \n";

			for (int d = 0; d < anchura; d++) {
				resultado += charChain(anchoColumna[d] + 3, '-'); // tejado entero
			}
			resultado += "- \n";
		}
		return resultado;
	}

	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Bienvenido a la TABLA de CRISTINA OUEGHLANI");

		Scanner s = new Scanner(System.in);

		int numeroFila = 0;
		int numeroColumna = 0;
		int contenido = 0;
		int anchura, altura;

		System.out.println();
		System.out.println("DIMENSIONES DEL TABLERO");
		anchura = pedirAnchura(s);
		altura = pedirAltura(s);
		System.out.println();

		TablaSimple tabla = new TablaSimple(altura, anchura);
		// Tabla generada:
		System.out.println(tabla.toString());

		System.out.println("Tabla generada. Preparada para modificarse. ");
		while (true) {

			System.out.println("Indica la FILA de la casilla que quieras modificar: ");
			numeroFila = s.nextInt();
			if (numeroFila < 0 || numeroFila > altura - 1) {
				System.out.println("Ha introduciodo MAL la FILA... " + numeroFila + " no es un número válido \n");
				continue;
			}

			System.out.println("Indica la COLUMNA de la casilla que quieras modificar: ");
			numeroColumna = s.nextInt();
			if (numeroColumna < 0 || numeroColumna > anchura - 1) {
				System.out.println("Ha introduciodo MAL la COLUMNA... " + numeroColumna +  " no es un número válido \n");
				continue;
			}

			System.out.println("Indica el VALOR ENTERO de la casilla que quieras modificar: ");
			contenido = s.nextInt();

			tabla.setContenido(new Coordenada(numeroFila, numeroColumna), contenido);
			System.out.println("\n" + tabla);

		}

	}

	
	/**
	 * Metodo privado pedirAnchura: el jugador elige la anchura del tablero
	 * 
	 * @param s
	 * @return la anchura
	 */
	private static int pedirAnchura(Scanner s) {

		String output;
		int m;

		do {

			System.out.print("Introduce la anchura del tablero : ");

			while (!s.hasNextInt()) {

				output = s.next();
				System.out.println(output + " no es un número.");

			}

			m = s.nextInt();

		} while (m < 1);

		return m;
	}

	
	/**
	 * Metodo privado pedirAltura: el jugador elige la altura del tablero
	 * 
	 * @param s
	 * @return la altura
	 */
	private static int pedirAltura(Scanner s) {

		String output;
		int n;

		do {

			System.out.print("Introduce la altura del tablero: ");

			while (!s.hasNextInt()) {

				output = s.next();
				System.out.println(output + " no es un número.");

			}

			n = s.nextInt();

		} while (n < 1);

		return n;
	}
}
