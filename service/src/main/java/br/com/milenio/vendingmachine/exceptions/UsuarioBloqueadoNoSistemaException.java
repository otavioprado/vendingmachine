package br.com.milenio.vendingmachine.exceptions;

public class UsuarioBloqueadoNoSistemaException extends Exception {
	private static final long serialVersionUID = -7988381603968389657L;

	public UsuarioBloqueadoNoSistemaException(String msg) {
    	super(msg);
    }
}
