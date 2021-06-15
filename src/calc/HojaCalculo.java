package calc;

import java.util.Scanner;
import java.util.Stack;


/**
 * Clase HojaCalculo
 *
 * @author Cristina Oueghlani
 */
public class HojaCalculo extends TablaSimple {

	private String alfabeto = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";

	/**
	 * Constructor de la clase HojaCalculo
	 *
	 * @param altura
	 * @param anchura
	 */
	public HojaCalculo(int altura, int anchura) {
		super(altura, anchura);
	}

	/**
	 * Método getAlfabeto()
	 *
	 * @return alfabeto
	 */
	public String getAlfabeto() {
		return alfabeto;
	}

	/**
	 * El método setFormula modifica la formula para la casilla indicada.
	 *
	 * @param coor
	 * @param f
	 */
	public void setFormula(Coordenada coor, Formula f) {
		// modificar la casilla a formula
		Casilla casilla = getCasillaHC(coor);
		casilla.setFormula(f);
		casilla.setEsFormula(true);
		casilla.setContenido(f.evaluar());
		super.setCasilla(new Coordenada(coor.getFila(), coor.getColumna()), casilla);
	}

	
	/**
	 * El método getCasillaHC obtiene la casilla de la coordenada
	 *
	 * @param coor
	 * @return la casilla seleccionada
	 */
	public Casilla getCasillaHC(Coordenada coor) {
		return super.getCasilla(new Coordenada(coor.getFila(), coor.getColumna()));
	}

	
	/**
	 * El método actualizar actualiza los valores
	 */
	public void actualizar() {
		boolean terminado = false;
		Casilla casilla;
		Coordenada c;

		while (!terminado) {
			terminado = true;

			for (int i = 0; i < super.getAltura(); i++) {
				for (int j = 0; j < super.getAnchura(); j++) {

					c = new Coordenada(i, j);
					casilla = getCasilla(c);

					if (casilla.isEsFormula()) {

						if (casilla.getContenido() != casilla.getFormula().evaluar()) {
							casilla.setContenido(casilla.getFormula().evaluar());
							super.setCasilla(c, casilla);
							terminado = false;
						}

					}
				}
			}
		}
	}

	
	/**
	 * Método toString, genera la hoja de cálculo a partir del método toString de la
	 * clase Tabla
	 */
	@Override
	public String toString() {
		int[] anchoColumna = super.anchoColumna;
		String result = "   ";
		String s = super.toString();
		String[] strings = s.split("\n");

		for (int z = 0; z < anchoColumna.length; z++) {

			if (1 == anchoColumna[z])
				result += "  " + this.alfabeto.charAt(z) + " ";

			else {
				int restante = anchoColumna[z] - 1;
				result += "  " + charChain(restante, ' ') + this.alfabeto.charAt(z) + " ";
			}
		}

		for (int i = 0; i < strings.length; i++) {
			if (i % 2 != 0) {
				strings[i] = " " + i / 2 + " " + strings[i];
			} else {
				strings[i] = "   " + strings[i];
			}
		}

		for (int j = 0; j < strings.length; j++) {
			result += "\n" + strings[j];
		}

		return result;
	}

	
	/**
	 * Metodo calcularTamaino que calcula el tamaño del array sin parentesis
	 * @param s
	 * @return el cardinal de elementos del array
	 */
	private int calcularTamaino(String[] s) {
		
		int indice=0;
		
		for(String ss: s) {
			
			if(ss.equals("(") || ss.equals(")") ) {
				continue;
			}
			indice++;
		}
		return indice;
	}
	
	
	/**
	 * El método generarArbolFormula, genera el arbol de la fórmula introducida
	 *
	 * @param s
	 * @return una Fórmula
	 */
	public Formula generarArbolFormula(String[] s) {

		Stack<Formula> pila = new Stack<Formula>();
		Formula f;
		for (int i = 0; i < s.length; i++) {

			// Si es operador --> añadir a la pila
			if (!operador(s[i])) {
				f = tipoFor(s[i]);
				pila.push(f);
			
			} else { // operar
				f = new Operacion(s[i], pila.pop(), pila.pop());
				pila.push(f);
			}
		}
		f = pila.peek();
		pila.pop();

		return f;
	}

	
	/**
	 * El método operador devuelve true en caso de que sea el operador *, /, +, -, (, )
	 * @param s
	 * @return true en caso de que sea *, /, +, -, (o ); false en caso contrario
	 */
	private boolean operador(String s) {
		if (s.equals("*") || s.equals("/") || s.equals("+") || s.equals("-") || s.equals("(") || s.equals(")"))
			return true;
		return false;
	}

	
	/**
	 * La función reordenar va a implemetar el algoritmo "Shunting-yard", reordenará la 
	 * fórmula a tipo sufijo (postfix)
	 * @param expression
	 * @return un array de la expresión en modo sufijo
	 */
	public String[] reordenar(String[] expression) {

		Stack<String> pila = new Stack<>();
		String[] resultado = new String[calcularTamaino(expression)];
		int indice = 0;

		for (int i = 0; i < expression.length; ++i) {
			String c = expression[i];

			// Si es letra o dígito -> añadir al resultado
			if (letraODigito(c)) {
				resultado[indice] = String.valueOf(c);
				indice++;
			}

			// Si es "(" -> añadimos a la pila
			else if (c.equals("("))
				pila.push(c);

			//Si es ")" -> retirar elementos de la pila hasta encontrar "("
			else if (c.equals(")")) {
				while (!pila.isEmpty() && pila.peek().equals("(")) {
					resultado[indice] = String.valueOf(pila.pop());
					indice++;
				}
				pila.pop();
			}

			// Si es un operador -> comparar prioridad de los operadores
			else {
				while (!pila.isEmpty() && prioridad(c) <= prioridad(pila.peek())) {
					resultado[indice] = String.valueOf(pila.pop());
					indice++;
				}
				pila.push(c);
			}
		}

		// Retirar los elementos restantes de la pila y añadirlos al resultado
		while (!pila.isEmpty()) {
			if (pila.peek().equals("("))
				return null;
			resultado[indice] = String.valueOf(pila.pop());
			indice++;
		}
		return resultado;
	}


	/**
	 * Metodo letraODigito que comprueba si el string es una letra o dígito
	 * @param c
	 * @return false si es *, /, +, -; true en caso contrario
	 */
	private static boolean letraODigito(String c) {
		if (c.equals("+") || c.equals("-") ||c.equals("*") ||c.equals("/") )
			return false;
		return true;
	}

	
	/**
	 * El método prioridad establece una gerarquia de prioridades 
	 * @param ch
	 * @return un entero
	 */
	private int prioridad(String s) {
		if (s.equals("+") || s.equals("-"))
			return 1;
		else if (s.equals("*") || s.equals("/"))
			return 2;
		else
			return -1;
	}

	
	/**
	 * Metodo privado para saber si una fórmula es constante o referencia
	 *
	 * @param s
	 * @return una Fórmula
	 */
	private Formula tipoFor(String s) {

		if (s.matches("[0-9]+")) {
			return new Constante(Integer.parseInt(s));

		} else {
			return new Referencia(new Coordenada(s));
		}
	}

	
	/**
	 * Main
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Bienvenido a la HOJA DE CALCULO de CRISTINA OUEGHLANI");

		Scanner s = new Scanner(System.in);

		int numeroFila = 0;
		String letraColumna = null;
		String[] contenido;
		int anchura, altura;

		System.out.println();
		System.out.println("DIMENSIONES DEL TABLERO --> REQUISITOS: dimensiones mínimas 2x2, dimensiones máximas Z9");
		System.out.println();
		anchura = pedirAnchura(s);
		altura = pedirAltura(s);
		System.out.println();

		HojaCalculo tabla = new HojaCalculo(altura, anchura);
		// Tabla generada:
		System.out.println(tabla.toString());

		System.out.println("Tabla generada. Preparada para modificarse. ");
		while (true) {
			System.out.println("Indica la COORDENADA (ejem: H8) de la casilla que quieras modificar: ");
			letraColumna = s.next();
			
			while(letraColumna.length() != 2) {
				System.out.println("No se ha indicado correctamente la coordenada. Vuelva a intentarlo: (ejem: H8)");
				letraColumna = s.next();
			}
			
			int numeroColumna = tabla.getAlfabeto().indexOf(Character.toUpperCase(letraColumna.charAt(0)));
			numeroFila = Integer.parseInt(String.valueOf(letraColumna.charAt(1)));

			if (letraColumna.length() != 2 || numeroColumna > anchura - 1) {
				System.out.println(
						"Ha introduciodo MAL la COLUMNA... " + letraColumna.charAt(0) + " no es una columna válida \n");
				continue;
			}

			if (numeroFila < 0 || numeroFila > altura - 1) {
				System.out.println("Ha introduciodo MAL la FILA... " + numeroFila + " no es un número válido \n");
				continue;
			}
			
			System.out.println("Indica el VALOR ENTERO o LA FORMULA(= A8) de la casilla que quieras modificar: ");
			String nextLinee = "";  
			while (nextLinee.equals("")) {
				nextLinee = s.nextLine();
			}
			
			contenido = tabla.sinBlancos(nextLinee);

			if (contenido.length < 1) {
				continue;
			}

			if (contenido.length < 2) { // si es nº
				int contenidoDelNumero = Integer.parseInt(contenido[0]);
				tabla.setContenido(new Coordenada(numeroFila, numeroColumna), contenidoDelNumero);

			} else { // es una formula

				if (!contenido[0].equals("=")) { // comprobar si la estructura-->=
					continue;
				}
				
				String[] cont = new String[contenido.length - 1];
				
				for(int i = 0; i< contenido.length - 1; i++) {
					cont[i] =contenido[i + 1];
				}
				
				Formula f = tabla.generarArbolFormula(tabla.reordenar(cont));
				tabla.setFormula(new Coordenada(numeroColumna, numeroFila), f);
			}

			tabla.actualizar();
			System.out.println("\n" + tabla);

		}

	}
	
	
	/**
	 * Metodo sinBlancos que lee lo que le pasen por paremetro y lo convierte
	 * sin espacios en blanco
	 * @param s
	 * @return
	 */
	private String[] sinBlancos(String s) {
		String sinBlancos = s.replaceAll("\\s+", "");
		String buffer = "";
		int tamaino = 0;
		String[] resultado;
		int indice = 0;
		
		//Modificar con un valor
		if(!s.contains("=") ) {
			resultado = new String[1];
			resultado[0] = s;
			return resultado;
		}
		
		//Formula:
		for(int c = 0; c<sinBlancos.length(); c++) {			
			if( !letraODigito(String.valueOf(sinBlancos.charAt(c))) || sinBlancos.charAt(c)=='=') {
				tamaino+=2;
			}
			else if(sinBlancos.charAt(c) == '('  || sinBlancos.charAt(c) == ')') {
				 tamaino++;
			}
		}		
		
		resultado = new String[tamaino];
		
		for(char c : sinBlancos.toCharArray()) {	
			
			if( !letraODigito(String.valueOf(c)) || c=='=' || c=='(' || c==')') {
				
				if(!buffer.equals("")) { //Para el 1º (vacio)
					resultado[indice] = buffer;
					indice++;
					buffer = "";
				}
								
				resultado[indice] = String.valueOf(c);
				indice++;
				continue;
			}
			buffer += String.valueOf(c);
		}
		
		if(!buffer.equals("")) { 
			resultado[indice] = buffer;
		}
		
		return resultado;
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

		} while (m < 2 || m > 27);

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

		} while (n < 2 || n > 9);

		return n;
	}

	
	
	
	
	
	// ____________________________________________FÓRMULA_____________________________________________________

	/**
	 * Interfaz Fórmula
	 *
	 * @author Cristina Oueghlani
	 */
	public interface Formula {

		public int evaluar();
	}

	/**
	 * Clase Referencia
	 *
	 * @author Cristina Oueghlani
	 */
	public class Referencia implements Formula {
		private Coordenada ref;

		/**
		 * Constructor de la clase Referencia
		 *
		 * @param ref
		 */
		public Referencia(Coordenada ref) {
			this.ref = ref;
		}

		/**
		 * Método getRef() usado para obtener la referencia
		 *
		 * @return Coordenada
		 */
		public Coordenada getRef() {
			return ref;
		}

		/**
		 * Método setRef() usado para modificar la referencia
		 *
		 * @param ref
		 */
		public void setRef(Coordenada ref) {
			this.ref = ref;
		}

		/**
		 * Método evaluar()
		 *
		 * @return el contenido de la casilla que hace referencia
		 */
		public int evaluar() {
			return HojaCalculo.this.getContenido(new Coordenada(ref.getFila(), ref.getColumna()));

		}
	}

	/**
	 * Clase Constante
	 *
	 * @author Cristina Oueghlani
	 */
	public class Constante implements Formula {

		private int consta;

		/**
		 * Constructora de la clase Constante
		 *
		 * @param consta
		 */
		public Constante(int consta) {
			this.consta = consta;
		}

		/**
		 * Método evaluar que devuelve una constante
		 *
		 * @return consta
		 */
		public int evaluar() {
			return consta;
		}
	}

	/**
	 * Clase Operacion
	 *
	 * @author Cristina Oueghlani
	 */
	public class Operacion implements Formula {

		private String operador;
		private Formula operando1;
		private Formula operando2;

		/**
		 * Constructor de la clase Operacion
		 *
		 * @param operador
		 * @param operando1
		 * @param operando2
		 */
		public Operacion(String operador, Formula operando1, Formula operando2) {
			this.operador = operador;
			this.operando1 = operando1;
			this.operando2 = operando2;
		}

		/**
		 * Método evaluar() que evalua los operarandos y realiza la operación
		 * correspondiente
		 */
		public int evaluar() {

			switch (this.operador) {
			case "+":
				return operando2.evaluar() + operando1.evaluar();
			case "-":
				return operando2.evaluar() - operando1.evaluar();
			case "*":
				return operando2.evaluar() * operando1.evaluar();
			case "/":
				return operando2.evaluar() / operando1.evaluar();

			default:
				throw new IllegalArgumentException("Operador no admitido: " + this.operador);
			}
		}

	}

}