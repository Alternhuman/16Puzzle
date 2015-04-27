package edu.twinlisps.aestrella;

import edu.twinlisps.puzzle.Accion;
import edu.twinlisps.puzzle.Estado;

/**
 * Representación de un nodo en el árbol de expansión
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class Nodo{
	private Nodo padre=null; //Apuntador al padre en el árbol de expansión
	private Estado estado; //Estado del nodo
	private int coste=0; //Coste
	private int heuristica=-1; //Función heurística
	private Accion accionElegida;
	
	
	public Nodo(Estado e){
		estado = e;
	}
	
	@Override
	public int hashCode(){
		return  estado.hashCode();
	}
	
	/*Setters y getters*/
	public int getHeuristica() {
		return heuristica;
	}


	public void setHeuristica(int hCoste) {
		this.heuristica = hCoste;
	}

	public Nodo getPadre() {
		return padre;
	}


	public void setPadre(Nodo padre) {
		this.padre = padre;
		
	}


	public Estado getEstado() {
		return estado;
	}


	public void setEstado(Estado estado) {
		this.estado = estado;
	}


	public int getCoste() {
		return coste;
	}


	public void setCoste(int coste) {
		this.coste = coste;
	}


	public Accion getAccionElegida() {
		return accionElegida;
	}


	public void setAccionElegida(Accion accionElegida) {
		this.accionElegida = accionElegida;
	}
	
	
}
