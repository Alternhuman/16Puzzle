package edu.twinlisps.puzzle;

/**
 * 
 * @author Alexandra Vicente - Diego Martín
 * Clase en la que se recoge información sobre cada casilla
 */
public class Casilla {
	/**
	 * Datos sobre la casilla
	 * valor Valor de la casilla
	 * x Índice de la fila donde se encuentra la casilla
	 * y Índice de la columna donde se encuentra la casilla
	 */
	private int valor;
	private int x;
	private int y;
	
	/**
	 * Constructor con la posición de la casilla. Se asume valor 0 (casilla vacía para el valor de la misma).
	 * @param _x Índice de la fila donde se encuentra la casilla
	 * @param _y Índice de la columna donde se encuentra la casilla
	 */
	public Casilla(int _x, int _y){
		valor = 0;
		x = _x;
		y = _y;
	}
	
	/**
	 * Constructor con la posición de la casilla y el valor de la misma.
	 * @param _x Índice de la fila donde se encuentra la casilla
	 * @param _y Índice de la columna donde se encuentra la casilla
	 * @param _valor Valor a asignar
	 */
	public Casilla(int _x, int _y, int _valor){
		valor = _valor;
		x = _x;
		y = _y;
	}
	
	
	@Override
	/**
	 * Una casilla es igual a otra si ambas tienen la misma posición en ambas dimensiones y valor.
	 */
	public boolean equals(Object obj){
		if(obj instanceof Casilla){
			Casilla c2 = (Casilla)obj;
			return c2.getX() == x && c2.getY() == y && c2.getValor() == valor;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return String.valueOf(valor);
	}
	
	@Override
	public Object clone(){
		return new Casilla(x, y, valor);
	}
	
	/*Getters y Setters*/
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
		
}
