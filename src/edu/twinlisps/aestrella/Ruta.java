package edu.twinlisps.aestrella;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Representación de una ruta dentro del árbol de expansión
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class Ruta {
	/*Referencia al nodo hoja*/
	private Nodo hoja;
	/*Longitud del camino*/
	private int longitud=0;
	private List<Nodo> nodos=null;
	private List<Nodo> nodosInverso=null;
	
	public Ruta(Nodo _hoja){
		hoja = _hoja;
	}
	
	public int getLongitud(){
		if(longitud == 0)
			getNodos();
		return longitud - 1;
	}
	
	/**
	 * 
	 * @return Lista con los partiendo del nodo hoja (estado final)
	 */
	public List<Nodo> getNodos(){
		if(nodos == null){
			nodos = new ArrayList<Nodo>();
			Nodo inicio = hoja;
			Nodo aux = null;
			while(inicio != null){
				aux = inicio.getPadre();
				nodos.add(inicio);
				inicio = aux;
				longitud++;
			}
		}
		return nodos;
	}
	
	/**
	 * 
	 * @return Lista con los partiendo del nodo inicial
	 */
	public List<Nodo> getNodosInverso(){
		if(nodosInverso == null){
			Stack<Nodo> nodos = new Stack<Nodo>();
			nodosInverso = new ArrayList<Nodo>();
			Nodo inicio = hoja;
			Nodo aux = null;
			while(inicio != null){
				aux = inicio.getPadre();
				nodos.add(inicio);
				inicio = aux;
			}
			
			while(!nodos.empty()){
				nodosInverso.add(nodos.pop());	
			}
		}
		return nodosInverso;
	}
	
	public Nodo getHoja(){
		return this.hoja;
	}
}
