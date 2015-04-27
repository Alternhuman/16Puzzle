package edu.twinlisps.heuristicas;

import edu.twinlisps.aestrella.Nodo;
import edu.twinlisps.puzzle.Estado;
/**
 * Clase abstracta que define todos los métodos que debe contener un cálculo de heurística
 * @author Alexandra Vicente - Diego Martín
 *
 */
public abstract class Heuristica {
	public final static int MANHATTAN = 0;
	public final static int NILSSON = 1;
	
	protected String nombre=""; //Nombre de la heurística
	public String getNombre(){
		return nombre;
	}
	/**
	 * Cálculo de la heurística partiendo de un nodo y un estado fin
	 * @param inicio Nodo de inicio
	 * @param fin Estado objetivo
	 * @return Valor de la heurística
	 */
	public abstract int Calcular(Nodo inicio, Estado fin);
	
	/**
	 * Cálculo partiendo de un estado inicio indicando el estado fin
	 * @param estado Estado inicio
	 * @param fin Estado fin
	 * @return Valor de la heurística
	 */
}
