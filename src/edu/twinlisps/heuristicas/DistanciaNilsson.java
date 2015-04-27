package edu.twinlisps.heuristicas;

import edu.twinlisps.aestrella.Nodo;
import edu.twinlisps.puzzle.Estado;

/**
 * Heurística consistente en aplicar la fórmula:
 * Manhattan(estado) + 3 * SumaSecuencias(estado)
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class DistanciaNilsson extends Heuristica{

	private Manhattan m = new Manhattan();
	private SumaSecuencias s = new SumaSecuencias();
	public DistanciaNilsson(){
		nombre="Distancia de Nilsson";
	}
	@Override
	public int Calcular(Nodo estado, Estado fin) {
		return m.Calcular(estado, fin) + 3 * s.Calcular(estado, fin);
	}
	
}
