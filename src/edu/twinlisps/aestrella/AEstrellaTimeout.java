package edu.twinlisps.aestrella;

import java.util.List;
import java.util.concurrent.Callable;

import edu.twinlisps.heuristicas.Heuristica;
import edu.twinlisps.puzzle.Accion;
import edu.twinlisps.puzzle.Estado;

/**
 * Implementación del algoritmo A* como objeto llamable dentro de un hilo, para
 * poder implementar timeouts
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class AEstrellaTimeout extends AEstrella implements Callable<Ruta>{
	Estado inicio, fin;
	Heuristica heuristica;
	boolean terminate=false;
	public AEstrellaTimeout(Estado i, Estado f, Heuristica h){
		inicio = i;
		fin = f;
		heuristica = h;
	}
	
	@Override
	public Ruta call() throws Exception {
		return solucion(inicio, fin, heuristica);
	}
	
	public void setTerminate(boolean status){
		terminate = status;
	}
	
	@Override
	protected Ruta resolver(Estado inicio, Estado fin){
		while(!abiertos.isEmpty() && !terminate){
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
}
