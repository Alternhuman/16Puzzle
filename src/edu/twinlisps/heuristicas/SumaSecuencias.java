package edu.twinlisps.heuristicas;

import edu.twinlisps.aestrella.Nodo;
import edu.twinlisps.puzzle.Casilla;
import edu.twinlisps.puzzle.Estado;

/**
 * Cálculo de la heurística Suma de secuencias, consistente en el cálculo
 * de un valor definido en función de la secuencia que debe seguir el estado final, siguiendo
 * la regla:
 * Para cada ficha del tablero <br/>
 * si la ficha es distinta del 0 :<br/>
 * si esta en el centro → 1<br />
 * si no esta en el centro y su sucesor en la secuencia no es el correspondiente → 2 <br />
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class SumaSecuencias extends Heuristica{

	@Override
	public int Calcular(Nodo estado, Estado fin) {
		Casilla [] e1 = estado.getEstado().getCasillas();
		Casilla [] e2 = fin.getCasillas();
		
		
		int acumulador = 0;
		
			for(int i=0;i<e1.length;i++){
				try{
					
					//Casilla vacía
					if(e1[i+1].getValor() == 0){
						acumulador += 0;
					}
					//Están en sucesión
					if(e1[i+1].getValor() == e2[i+1].getValor()){
						acumulador += 0;
					}else{
						//No están en sucesión
						acumulador += 2;
					}
				}catch(ArrayIndexOutOfBoundsException a){
					acumulador += 0;
				}
			}
		
		return acumulador;
	}

}
