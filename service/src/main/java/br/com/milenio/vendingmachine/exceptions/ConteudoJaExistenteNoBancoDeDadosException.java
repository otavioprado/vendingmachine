package br.com.milenio.vendingmachine.exceptions;

public class ConteudoJaExistenteNoBancoDeDadosException extends Exception {
	private static final long serialVersionUID = 7055919283353499099L;
	
    public ConteudoJaExistenteNoBancoDeDadosException(String msg) {
    	super(msg);
    }
}
