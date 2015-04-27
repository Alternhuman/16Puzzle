package edu.twinlisps.puzzle;

/**
 * 
 * @author Alexandra Vicente - Diego Martín
 * Recoge información sobre una acción a tomar, así como métodos para aplicarla
 */
public class Accion {

	private int movimientoX; //Movimiento en el eje horizontal
	private int movimientoY; //Movimiento en el eje vertical
	private Casilla casilla; //Casilla sobre la que se aplica el movimiento
	private String accionVerbal; //Una representación verbal de la acción a realizar
	
	/**
	 * Creación de la acción con los movimientos en ambos ejes y la casilla sobre la que aplicar dichos movimientos
	 * @param _casilla Información de la casilla
	 * @param x Movimiento en el eje horizontal
	 * @param y Movimiento en el eje vertical
	 */
	public Accion(Casilla _casilla, int x, int y) {
		movimientoX = x;
		movimientoY = y;
		casilla = _casilla;
		//Creación de la acción verbal
		if(x != 0){
			accionVerbal = x < 0 ? "Mover hacia arriba" : "Mover hacia abajo";
		}
		if(y != 0){
			accionVerbal = y < 0 ? "Mover hacia la izquierda" : "Mover hacia la derecha";
		}
	}
	
	/**
	 * Aplicar una acción en un estado
	 * @param e Estado sobre el que aplicar la acción
	 * @return Nuevo estado con el resultado de la acción aplicada
	 */
	public Estado aplicar(Estado e){
		Casilla vacia = e.getPosicionVacia();
		//Valor de la posición que ocupará la casilla vacía
		int reemplazar = e.getValor(vacia.getX() + movimientoX, vacia.getY() + movimientoY);
		
		Estado nuevoEstado = (Estado)e.clone();
		nuevoEstado.setValor(vacia.getX(), vacia.getY(), reemplazar);
		nuevoEstado.setValor(vacia.getX() + movimientoX, vacia.getY() + movimientoY, 0);
		return nuevoEstado;
	}
	
	@Override
	public String toString(){
		return accionVerbal + "la casilla" + casilla.getX() + ","+casilla.getY();
	}
	
	/*Setters y getters*/
	public int getMovimientoX() {
		return movimientoX;
	}

	public int getMovimientoY() {
		return movimientoY;
	}

	public Casilla getCasilla() {
		return casilla;
	}
	
	
	public String getAccionVerbal(){
		return accionVerbal;
	}
	
}
