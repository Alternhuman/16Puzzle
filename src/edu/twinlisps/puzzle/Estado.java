package edu.twinlisps.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Clase Estado
 * Representa cada uno de los estados en los que el puzzle se encuentra en cada momento, 
 * incluye información sobre las diferentes casillas y las posibles acciones a tomar a partir
 * del mismo.
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class Estado implements Cloneable{
	public static final int MAXMOVIMIENTOS = 50;
	private Casilla [] puzzle; //Array del tipo Casilla, a inicializarse con el número de casillas del puzzle.
	private int dimension; //Contiene la dimensión del puzzle (3 o 4).
	private int hashCode = -1; //Los estados se almacenan en un HashMap, esta variable evita recalcular el hash cada vez que sea necesario
	
	/**
	 * Constructor con datos iniciales.
	 * @param valores Array con los valores de las casillas. El orden de las casillas seguirá el orden del array.
	 */
	public Estado (int [] valores){
		puzzle = new Casilla[valores.length];
		dimension = (int)Math.sqrt((double)valores.length);
		
		for(int i = 0; i< dimension;i++){
			for(int j=0; j<dimension;j++){
				puzzle[i*dimension+j] = new Casilla(i, j, valores[i*dimension + j]);
			}
		}
	}
	
	/**
	 * Constructor con un array de casillas ya creado
	 * @param datos
	 */
	public Estado(Casilla[] datos){
		puzzle = datos.clone();
		dimension = (int)Math.sqrt((double)datos.length);
	}
	
	/**
	 * Retorna un objeto Casilla con la posición de la casilla vacía
	 * @return Información sobre la casilla vacía
	 */
	public Casilla getPosicionVacia(){
		for (Casilla casilla : puzzle) {
			if(casilla.getValor() == 0)
				return new Casilla(casilla.getX(), casilla.getY(),0);	
		}
		
		return null;
	}
	
	@Override
	/**
	 * Si el contenidos de los arrays de Casilla son iguales, los Estados son iguales.
	 */
	public boolean equals(Object obj){
		if(obj instanceof Estado){
			Estado e2 = (Estado)obj;
			return Arrays.equals(puzzle, e2.getCasillas());
		}
		return false;
	}
	/**
	 * Crea un puzzle aleatorio con la dimensión especificada, 
	 * partiendo de un estado final y realizando un número desconocido de movimientos aleatorios.
	 * @param dimension Valor de la dimensión, 3 o 4.
	 * @return Objeto {@link #Estado Estado} aleatorio
	 */
	public static Estado crearAleatorio(int dimension){
		return crearAleatorio(dimension, new Random().nextInt(MAXMOVIMIENTOS));
	}
	
	/**
	 * Crea un puzzle aleatorio con la dimensión especificada, 
	 * partiendo de un estado final y realizando un número determinado de movimientos aleatorios.
	 * @param dimension Valor de la dimensión, 3 o 4.
	 * @param movimientos Número de movimientos a realizar
	 * @return Objeto {@link #Estado Estado} aleatorio
	 */
	public static Estado crearAleatorio(int dimension, int movimientos){
		if(dimension < 3 || dimension > 4)
			return null;
		Estado estado = crearEstadoFinal(dimension);
		Random r = new Random();
		while(movimientos-->0){
			//ArrayList<Accion> acciones = (ArrayList<Accion>) estado.getAcciones();
			//Accion a = acciones.get(r.nextInt(acciones.size()));
			//estado = a.aplicar(estado);
			estado = estado.getAcciones().get(r.nextInt(estado.getAcciones().size())).aplicar(estado);
		}
		return estado;
	}
	/**
	 * Crea un puzzle aleatorio con la dimensión especificada, 
	 * partiendo de un estado final y realizando un número 
	 * aleatorio de movimientos aleatorios, especificando el mínimo número de movimientos.
	 * @param dimension Dimensión del puzzle a crear
	 * @param minMovimientos mínimo número de movimientos a realizar
	 * @param maxMovimientos máximo número de movimientos a realizar
	 * @return Objeto {@link #Estado Estado} aleatorio
	 */
	public static Estado crearAleatorio(int dimension, int minMovimientos, int maxMovimientos){
		if(maxMovimientos < minMovimientos){
			return null;
		}
		Random r = new Random();
		int movimientos = minMovimientos + r.nextInt(maxMovimientos);
		return crearAleatorio(dimension, movimientos);
	}
	
	/**
	 * Crea el puzzle con las casillas dispuestas en el orden final:
	 * Para dimensión 3:
	 * 1 2 3<br/>
	 * 4 5 6<br/>
	 * 7 8 0<br/>
	 * Para dimensión 4:
	 *  1  2  3  4<br/>
	 *  5  6  7  8<br/>
	 *  9 10 11 12<br/>
	 * 13 14 15 0<br/>
	 * 
	 * @param dimension Dimensión del puzzle a crear
	 * @return Nuevo puzzle con la dimensión especificada dispuesto en el orden final
	 */
	public static Estado crearEstadoFinal(int dimension){
		if(dimension < 3 || dimension > 4)
			return null;
		int [] orden = new int[dimension*dimension];
		for (int i = 0; i < orden.length; i++) {
			if(i != orden.length -1)
				orden[i] = i+1;
			else
				orden[i] = 0;
				
		}
		return new Estado(orden);
	}
	
	/**
	 * Cálculo de todas las acciones posibles según la posición de la casilla vacía.
	 * Para el estado:<br />
	 * 0 1 2<br />
	 * 4 5 6<br />
	 * 7 8 3<br />
	 * Retornará dos objetos {@link #edu.twinlisp.puzzle.Accion Accion}, desplazar la casilla vacía hacia la derecha y hacia abajo
	 * @return Lista de {@link #edu.twinlisp.puzzle.Accion Accion} con las acciones válidas
	 */
	public List<Accion> getAcciones(){
		List<Accion> acciones = new ArrayList<Accion>();
		Casilla posicionVacia = this.getPosicionVacia();
		
		//Movimiento hacia abajo
		if(posicionVacia.getX() > 0){
			Casilla abajo = new Casilla(posicionVacia.getX() - 1, posicionVacia.getY());
			acciones.add(new Accion(abajo, -1, 0));
		}
		
		//Movimiento hacia arriba
		if(posicionVacia.getX() < dimension - 1){
			Casilla arriba = new Casilla(posicionVacia.getX() + 1, posicionVacia.getY());
			acciones.add(new Accion(arriba, +1, 0));
		}
		
		//Movimiento hacia la izquierda
		if(posicionVacia.getY() > 0){
			Casilla izquierda = new Casilla(posicionVacia.getX(), posicionVacia.getY() - 1);
			acciones.add(new Accion(izquierda, 0, -1));
		}
		
		//Movimiento hacia la derecha
		if(posicionVacia.getY() < dimension -1){
			Casilla derecha = new Casilla(posicionVacia.getX(), posicionVacia.getY() +1);
			acciones.add(new Accion(derecha, 0, 1));
		}
		
		return acciones;
		
	}

	/**
	 * Genera una copia de las casillas del estado
	 * @return Array de {@link #edu.twinlisp.puzzle.Casilla Casilla} con una copia de las casillas.
	 */
	public Casilla[] copyCasillas() {
		Casilla [] copy = new Casilla[puzzle.length];
		
		for (int i = 0; i < copy.length; i++) {
			copy[i] = (Casilla) puzzle[i].clone();
		}
		return copy;
	}
	
	@Override
	public Object clone(){
		Casilla [] copy = new Casilla[puzzle.length];
		
		for (int i = 0; i < copy.length; i++) {
			copy[i] = (Casilla) puzzle[i].clone();
		}
		return  new Estado(copy);
	}
	
	@Override
	public int hashCode() {
		if (hashCode == -1){
			int result = 17;
			for (int i = 0; i < puzzle.length; i++) {
					result = 31 * result + puzzle[i].getValor();
			}
			hashCode = result;
		}
		
		return hashCode;
	}
	
	@Override
	public String toString(){
		Iterator<Casilla> it = Arrays.asList(puzzle).iterator();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		while(it.hasNext()){
			Casilla casilla = it.next();
			sb.append(casilla.getValor());
			if(it.hasNext()){
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Retorna una cadena de caracteres con los valores dispuestos de forma bidimensional
	 * @return Cadena de caracteres con los valores dispuestos de forma bidimensional
	 */
	public String toDimensionalString() {
		StringBuilder sr = new StringBuilder();
		for(int i=0;i<getDimension();i++){
			for(int j=0;j< getDimension();j++){
				sr.append(String.format("%2d", getValor(i, j)));
			}
			sr.append("\n");
		}
		
		return sr.toString();
	}

	/*Setters y getters*/
	
	/**
	 * Retorna el valor de una posición determinada
	 * @param x Índice de la fila
	 * @param y Índice de la columna
	 * @return Valor de la casilla
	 */
	public int getValor(int x, int y){
		return puzzle[x*dimension + y].getValor();
	}
	
	/**
	 * Fija el valor de una posición determinada
	 * @param x Índice de la fila
	 * @param y Índice de la columna
	 * @param valor Valor a fijar
	 */
	public void setValor(int x, int y, int valor){
		puzzle[x*dimension+y].setValor(valor);
	}
	
	/**
	 * Obtiene la referencia de las casillas
	 * @return Referencia al array de {@link #edu.twinlisp.puzzle.Casilla Casilla}
	 */
	public Casilla[] getCasillas(){
		return this.puzzle;
	}
	
	/**
	 * Dimensión del lado del puzzle (3 o 4)
	 * @return Entero con la dimensión del lado del puzzle
	 */
	public int getDimension(){
		return this.dimension;
	}
	
	/**
	 * Retorna en un objeto casilla la información de la posición donde se encuentra el valor indicado
	 * @param valor Valor a consultar
	 * @return Objeto {@link #edu.twinlisp.puzzle.Casilla Casilla} con la información sobre la posición
	 */
	public Casilla getPosicion(int valor) {
		for (Casilla casilla : puzzle) {
			if(casilla.getValor() == valor){
				return (Casilla) casilla.clone();
			}
		}
		return null;
	}
	
}
