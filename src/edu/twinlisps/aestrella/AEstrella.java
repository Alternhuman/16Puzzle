/*
 * Implementación basada en https://en.wikipedia.org/wiki/A*_search_algorithm
 */
package edu.twinlisps.aestrella;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import edu.twinlisps.heuristicas.Heuristica;
import edu.twinlisps.heuristicas.Manhattan;
import edu.twinlisps.puzzle.Accion;
import edu.twinlisps.puzzle.Estado;

/**
 * Implementación del algoritmo A*
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class AEstrella {
	/*Listas para el algoritmo*/
	protected Set<Estado> cerrados;
	protected Set<Nodo> abiertos;
	protected Queue<Nodo> ruta;
	
	public AEstrella(){
		cerrados = new HashSet<Estado>();
		abiertos = new HashSet<Nodo>();
	}
	
	public Ruta solucion(Estado inicio, Estado fin){
		ruta = new PriorityQueue<Nodo>(10000, new ComparadorNodos(fin, new Manhattan()));
		
		Nodo aux = new Nodo(inicio);
		aux.setCoste(0);
		
		//f_score[start] := g_score[start] + heuristic_cost_estimate(start, goal)
		abiertos.add(aux);
		ruta.add(aux);
		
		return resolver(inicio, fin);
	}
	
	public Ruta solucion(Estado inicio, Estado fin, Heuristica heuristica){
		ruta = new PriorityQueue<Nodo>(10000, new ComparadorNodos(fin, heuristica));
		
		Nodo aux = new Nodo(inicio);
		aux.setCoste(0);
		
		abiertos.add(aux);
		ruta.add(aux);
		
		return resolver(inicio, fin);
	}
	
	/**
	 * Inicialización del algoritmo
	 * @param inicio Estado inicial
	 * @param fin Estado final
	 */
	public void iniciar(Estado inicio, Estado fin){
		
		ruta = new PriorityQueue<Nodo>(10000, new ComparadorNodos(fin, new Manhattan()));
		
		Nodo aux = new Nodo(inicio);
		aux.setCoste(0);
		
		//f_score[start] := g_score[start] + heuristic_cost_estimate(start, goal)
		abiertos.add(aux);
		ruta.add(aux);
	}
	
	protected Ruta resolver(Estado inicio, Estado fin){
		while(!abiertos.isEmpty()){
			Nodo actual = ruta.poll();
			abiertos.remove(actual);
			
			if(actual == null)	continue;
			
			Estado e = actual.getEstado();
			
			if(e.equals(fin)){
				
				return new Ruta(actual); //return reconstruct_path(came_from, goal)
			}
			
			cerrados.add(e);
			
			List<Accion> posiblesAcciones = e.getAcciones();
			
			for (Accion accion : posiblesAcciones) {
				Estado aux = accion.aplicar(e);
				
				if(!cerrados.contains(aux)){
					Nodo vecino = new Nodo(aux);
					
					vecino.setPadre(actual);
					vecino.setAccionElegida(accion);
					vecino.setCoste(actual.getCoste() + 1);
					
					if(!abiertos.contains(vecino)){
						abiertos.add(vecino);
						ruta.add(vecino);
					}
					
				}
				
			}
			
		}
		return null;
	}
	
	public static class ComparadorNodos implements Comparator<Nodo>{
		Estado fin;
		Heuristica heuristica;
		public ComparadorNodos(){}
		public ComparadorNodos(Estado _fin){
			fin = _fin;
			heuristica = new Manhattan();
		}
		
		public ComparadorNodos(Estado _fin, Heuristica _h){
			fin = _fin;
			heuristica = _h;
		}
		
		
		@Override
		public int compare(Nodo o1, Nodo o2) {
			
			int comparacionHeuristica = (o1.getCoste() + heuristica.Calcular(o1, fin) ) - (o2.getCoste() + heuristica.Calcular(o1, fin));
			if(comparacionHeuristica == 0){
				comparacionHeuristica = o2.getCoste() - o1.getCoste();
			}
			return comparacionHeuristica;
		}
		
	}
	
	
}
