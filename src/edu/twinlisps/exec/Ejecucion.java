package edu.twinlisps.exec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import edu.twinlisps.aestrella.AEstrella;
import edu.twinlisps.aestrella.AEstrellaTimeout;
import edu.twinlisps.aestrella.Nodo;
import edu.twinlisps.aestrella.Ruta;
import edu.twinlisps.heuristicas.Heuristica;
import edu.twinlisps.heuristicas.Manhattan;
import edu.twinlisps.puzzle.Estado;
/**
 * Clase
 * @author Alexandra Vicente - Diego Martín
 *
 */
public class Ejecucion {
	/**
	 * 
	 * @param dimension dimensión del puzzle
	 * @param minMovs mínimo número de movimientos aleatorios
	 * @param maxMovs máximo número de movimientos aleatorios
	 * @param h heurística a utilizar
	 * @param key Nombre del monitor de JAMon
	 * @return
	 */
	public static int standardExecution(int dimension, int minMovs, int maxMovs, Heuristica h, String key){
		Estado inicio = Estado.crearAleatorio(dimension, minMovs, maxMovs);
		Estado fin = Estado.crearEstadoFinal(dimension);
		
		AEstrellaTimeout aEstrella = new AEstrellaTimeout(inicio, fin, h);
		FutureTask<Ruta> tarea = new FutureTask<Ruta>(aEstrella);
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(tarea);
		Monitor jamon=null;
		Ruta ruta=null;
		try{
			jamon = MonitorFactory.start(key);
			ruta = tarea.get(60, TimeUnit.SECONDS);
			jamon.stop();
		}catch(TimeoutException ex){
			jamon.stop();
			aEstrella.setTerminate(true);
			executor.shutdownNow();
			return -1;
		}catch(InterruptedException i){
		}catch(ExecutionException ee){
		}finally{
			tarea.cancel(true);
		}
		if(ruta == null){
			//System.out.println("No se ha encontrado solución");
			return -1;
		}else{
			//System.out.println("Se ha encontrado solución");
			return ruta.getLongitud();
		}
	}
	/**
	 * Ejecución única, sin iteraciones
	 * @param args Argumentos de línea de comandos, en caso de que sean necesarios
	 */
	public static void standardStandaloneExecution(String[] args){

		int [] arrayInicio = {1, 2, 3, 4, 0, 6, 7, 5, 8};
		int [] arrayFin = {1, 2, 3, 4, 5, 6, 7, 8, 0};
		
		Estado inicio = new Estado(arrayInicio);
		inicio = Estado.crearAleatorio(4, 25);
		Estado fin = new Estado(arrayFin);
		fin = Estado.crearEstadoFinal(4);
		System.err.println("Inicio: \n"+inicio.toDimensionalString());
		System.err.println("Fin   : \n"+fin.toDimensionalString());
		AEstrella as = new AEstrella();
		Monitor jamon = null;
		jamon = MonitorFactory.start("contador");
		Ruta ruta = as.solucion(inicio,fin);
		jamon.stop();
		if(ruta != null)
		{
			System.out.println("Tiene solución");
			ArrayList<Nodo> estados = (ArrayList<Nodo>) ruta.getNodosInverso();
			Iterator<Nodo> it = estados.iterator();
			while(it.hasNext()){
				System.out.println(it.next().getEstado().toDimensionalString());
			}
			
		}else{
			System.out.println("No se ha encontrado solución");
		}
		System.out.println("\n");
		System.out.println("Estadísticas");
		System.out.println("Media: "+jamon.getAvg()+". Mínimo: "+jamon.getMax()+". Hits: "+jamon.getHits());
	}
	
	public static void main(String[] args) {
		int iterations=100;
		int sinSolucion= 0;
		int movimientos=0;
		int aux;
		while(iterations > 0){
			aux = standardExecution(3, 10, 60, new Manhattan(), "monitor3");
			if(aux ==-1)
				sinSolucion++;
			else
				movimientos+=aux;
			iterations--;
		}
		Monitor mon = MonitorFactory.getTimeMonitor("monitor3");
		System.out.println("Estadísticas para dimensión 3");
		System.out.println(String.format("Número de iteraciones: %d", (int)mon.getHits()));
		System.out.println(String.format("%d sin solución", sinSolucion));
		System.out.println(String.format("Máximo: %.3f milisegundos", mon.getMax()));
		System.out.println(String.format("Mínimo: %.3f milisegundos", mon.getMin()));
		System.out.println(String.format("Media: %.3f milisegundos", mon.getAvg()));
		System.out.println(String.format("Número medio de movimientos: %d", movimientos/((int)mon.getHits() - sinSolucion)));
		
		aux=0;
		movimientos=0;
		iterations=20;
		sinSolucion = 0;
		
		while(iterations > 0){
			aux = standardExecution(4, 10, 60, new Manhattan(), "monitor4");
			if(aux == -1)
				sinSolucion++;
			else{
				movimientos += aux;
			}
			iterations--;
		}
		mon = MonitorFactory.getTimeMonitor("monitor4");
		System.out.println("Estadísticas para dimensión 4");
		System.out.println(String.format("Número de iteraciones: %d", (int)mon.getHits()));
		System.out.println(String.format("%d sin solución", sinSolucion));
		System.out.println(String.format("Máximo: %.3f milisegundos", mon.getMax()));
		System.out.println(String.format("Mínimo: %.3f milisegundos", mon.getMin()));
		System.out.println(String.format("Media: %.3f milisegundos", mon.getAvg()));
		System.out.println(String.format("Número medio de movimientos: %d", movimientos/((int)mon.getHits() - sinSolucion)));
		
	}
	
}
