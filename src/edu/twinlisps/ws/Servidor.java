/**
 * @author Alexandra Vicente - Diego Martin
 *
 */
package edu.twinlisps.ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.twinlisps.aestrella.AEstrellaTimeout;
import edu.twinlisps.aestrella.Nodo;
import edu.twinlisps.aestrella.Ruta;
import edu.twinlisps.heuristicas.DistanciaNilsson;
import edu.twinlisps.heuristicas.Heuristica;
import edu.twinlisps.heuristicas.Manhattan;
import edu.twinlisps.puzzle.Estado;


@Path("/")
public class Servidor {
	private static final int SEGUNDOS = 30; // Tiempo maximo de ejecucion del algoritmo
	
	@GET
	@Path("random/{dimension}")
	@Produces("application/json")
	public Response randomPuzzle(@PathParam("dimension") int dimension){
		if(dimension < 3 || dimension > 4){
			//Dimension erronea
			return Response.status(404).build();
		}
		
		Estado e = Estado.crearAleatorio(dimension, 10, Estado.MAXMOVIMIENTOS);
    	    	
    	return Response.ok(e.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("end/{dimension}")
	@Produces("application/json")
	public Response end(@PathParam("dimension") int dimension){
		if(dimension < 3 || dimension > 4){
			//Dimension erronea
			return Response.status(404).build();
		}
		return Response.ok(Estado.crearEstadoFinal(dimension).toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("solve")
	@Produces("application/json")
	public Response solve(@FormParam("heuristic") int heuristic,
			@FormParam("dimension") int dimension,
			@FormParam("puzzle") String puzzle){
		
		JSONArray array = new JSONArray(puzzle);
    	int casillas[] = new int[dimension*dimension];
    	for (int i = 0; i < casillas.length; i++) {
			casillas[i] = array.getInt(i);
		}
    	
    	Estado es = new Estado(casillas);
    	
		Estado eFinal = Estado.crearEstadoFinal(dimension);
		
		Heuristica heuristica=null;
		
		switch(heuristic){
			case Heuristica.MANHATTAN:
				heuristica = new Manhattan();
				break;
			case Heuristica.NILSSON:
				heuristica = new DistanciaNilsson();
				break;
			case 2:
			default:
				heuristica = new Manhattan();
				break;
		}
		
		
		AEstrellaTimeout t = new AEstrellaTimeout(es, eFinal, heuristica);
		FutureTask<Ruta> tarea = new FutureTask<Ruta>(t);
		Ruta ruta=null;
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(tarea); //Ejecucion del algoritmo A* en un Future. 
		//El algoritmo comenzara a ejecutarse en un hilo paralelo y el resultado se obtendra en una llamada a get()
		//En caso de excederse el tiempo de ejecucion especificado, se retornara un error.
		
		try{
			ruta = tarea.get(SEGUNDOS, TimeUnit.SECONDS);
		}catch(TimeoutException ex){
			//Timeout excedido
			JSONObject jo = new JSONObject();
			jo.put("estado", false);
			jo.put("statusMessage", "No se ha encontrado solución en el tiempo dado");
			tarea.cancel(true);
			executor.shutdownNow();
			t.setTerminate(true);
			return Response.ok(jo.toString(), MediaType.APPLICATION_JSON).build();
		}catch(InterruptedException i){
			//Interrupcion
			JSONObject jo = new JSONObject();
			jo.put("estado", false);
			jo.put("statusMessage", "No se ha encontrado solución debido a una interrupción");
			tarea.cancel(true);
			return Response.ok(jo.toString(), MediaType.APPLICATION_JSON).build();
		}catch(ExecutionException ee){
			//Fallo en la ejecucion
			JSONObject jo = new JSONObject();
			jo.put("estado", false);
			jo.put("statusMessage", "No se ha encontrado solución debido a un fallo en la ejecución");
			tarea.cancel(true);
			
			return Response.ok(jo.toString(), MediaType.APPLICATION_JSON).build();
		}catch(OutOfMemoryError E){
			//Fallo en memoria
			System.err.println("Fallo de memoria");
			tarea.cancel(true);
			t.setTerminate(true);
			executor.shutdownNow();
			JSONObject jo = new JSONObject();
			jo.put("estado", false);
			jo.put("statusMessage", "No se ha encontrado solución debido a un fallo en la aplicacion");
			tarea.cancel(true);
			executor.shutdownNow();
			t.setTerminate(true);
			return Response.ok(jo.toString(), MediaType.APPLICATION_JSON).build();
		}catch(Exception E){
			tarea.cancel(true);
			t.setTerminate(true);
			executor.shutdownNow();
			JSONObject jo = new JSONObject();
			jo.put("estado", false);
			jo.put("statusMessage", "No se ha encontrado solución en el tiempo dado");
			tarea.cancel(true);
			executor.shutdownNow();
			t.setTerminate(true);
			return Response.ok(jo.toString(), MediaType.APPLICATION_JSON).build();
		}finally{
			tarea.cancel(true);
		}
		
		
    	if(ruta != null)
		{
    		StringBuilder sb = new StringBuilder();
    		sb.append("[");
			ArrayList<Nodo> estados = (ArrayList<Nodo>) ruta.getNodosInverso();
			Iterator<Nodo> it = estados.iterator();
			while(it.hasNext()){
				sb.append(it.next().getEstado().toString());
				if(it.hasNext()){
					sb.append(",");
				}
			}
			sb.append("]");
    		
			JSONArray j = new JSONArray(sb.toString());
			
			JSONObject jo = new JSONObject();
			jo.put("estado", true);
			jo.put("statusMessage", "Solución encontrada");
			jo.put("solution", j);
			jo.put("pasos", ruta.getLongitud());
			return Response.ok(jo.toString(), MediaType.APPLICATION_JSON).build();
		}else{
			JSONObject jo = new JSONObject();
			jo.put("estado", false);
			jo.put("statusMessage", "No se ha encontrado solución al puzzle");
			return Response.ok(jo.toString(), MediaType.APPLICATION_JSON).build();
		}
	}
}