package br.com.milenio.vendingmachine.exceptions;

public class InconsistenciaException extends Exception {

	private static final long serialVersionUID = 1677127322508091438L;

	public InconsistenciaException(String msg) {
    	super(msg);
    }
}
