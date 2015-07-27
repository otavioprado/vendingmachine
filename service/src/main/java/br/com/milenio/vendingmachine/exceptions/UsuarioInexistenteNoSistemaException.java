package br.com.milenio.vendingmachine.exceptions;

public class UsuarioInexistenteNoSistemaException extends Exception {
	private static final long serialVersionUID = 7055919283353499099L;
	
    public UsuarioInexistenteNoSistemaException(String msg) {
    	super(msg);
    }
}
