package edu.twinlisps.heuristicas;

import edu.twinlisps.aestrella.Nodo;
import edu.twinlisps.puzzle.Casilla;
import edu.twinlisps.puzzle.Estado;

/**
 * Cálculo de la heurística de Manhattan
 * Ref: http://heuristicswiki.wikispaces.com/Manhattan+Distance
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class Manhattan extends Heuristica{

	public Manhattan(){
		nombre = "Manhattan";
	}
	
	@Override
	public int Calcular(Nodo estado, Estado fin){
		int valor = estado.getCoste();
		if(valor == -1){
			/*Para optimizar el tiempo de cálculo, si el valor de este nodo
			 * no ha sido calculado previamente, se almacena, y en próximas invocaciones
			 * se retornará el valor previamente calculado
			 * */
			int acumulador = 0;
			Casilla [] allCells = estado.getEstado().getCasillas();
			int dimension = estado.getEstado().getDimension();
			
			for(int i = 0; i< allCells.length;i++){
				int value = allCells[i].getValor();
				if(value == 0){
					continue;
				}
				
				int row = i /dimension;
				int column = i % dimension;
				int expectedRow = (value - 1) / dimension;
				int expectedColumn = (value - 1) % dimension;
				int difference = Math.abs(row - expectedRow)
						+ Math.abs(column - expectedColumn);
				acumulador += difference;
				
			}
			valor = acumulador;
			estado.setCoste(valor);
		}
		
		return valor;
	}

	
	
	@Override
	public int hashCode(){
		return nombre.hashCode();
	}

}
