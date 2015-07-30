package br.com.milenio.vendingmachine.exceptions;

public class CadastroInexistenteException extends Exception {

	private static final long serialVersionUID = 6127345963846043486L;

	public CadastroInexistenteException(String msg) {
    	super(msg);
    }
}
