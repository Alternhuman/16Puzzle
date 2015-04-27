package edu.twinlisps.heuristicas;

import java.util.HashSet;
import java.util.Set;

import edu.twinlisps.aestrella.Nodo;
import edu.twinlisps.puzzle.Estado;

/**
 * Heurística consistente en la aplicación de múltiples heurísticas
 * Contiene un HashSet (evitando duplicados) en el que se incluyen todas las heurísticas.
 * Posteriormente se recorrerá y se sumarán los valores de todas ellas
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class MultipleHeuristica extends Heuristica{

	public Set<Heuristica> heuristicas;
	
	public MultipleHeuristica(){
		heuristicas = new HashSet<Heuristica>();
	}
	
	public MultipleHeuristica(HashSet<Heuristica> _heuristicas){
		heuristicas = _heuristicas;
	}
	
	public void insertarHeuristica(Heuristica h){
		heuristicas.add(h);
	}
	

	@Override
	public int Calcular(Nodo estado, Estado fin) {
		int acumulador=0;
		
		for (Heuristica heuristica : heuristicas) {
			acumulador += heuristica.Calcular(estado, fin);
		}
		
		return acumulador;
	}

}
