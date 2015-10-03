package br.com.milenio.vendingmachine.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	
	/**
	 * Método responsável por realizar a criptografia em MD5 de um respectivo valor
	 * Se não conseguir criptografar (erro) devolve a própria String de entrada
	 * @param valor a ser criptografado em MD5
	 * @return valor criptografado em MD5
	 */
	public static String criptografar(String valor) {
		MessageDigest m;
		String hashText = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(valor.getBytes());
			hashText = new BigInteger(1, m.digest()).toString(16);
			
			while(hashText.length() < 32 ) {
				hashText = "0" + hashText;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hashText;
	}
	
	public static void main(String[] args) {
		System.out.println("gudiao = " + criptografar("gudiao"));
		System.out.println("administrador = " + criptografar("administrador"));
		System.out.println("gestor = " + criptografar("gestor"));
		System.out.println("gerente = " + criptografar("gerente"));
		System.out.println("operador = " + criptografar("operador"));
	}
}
