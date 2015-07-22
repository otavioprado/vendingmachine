package br.com.milenio.vendingmachine.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	
	/**
	 * M�todo respons�vel por realizar a criptografia em MD5 de um respectivo valor
	 * Se n�o conseguir criptografar (erro) devolve a pr�pria String de entrada
	 * @param valor a ser criptografado em MD5
	 * @return valor criptografado em MD5
	 */
	public static String criptografar(String valor) {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(valor.getBytes());
			return (new BigInteger(1, m.digest()).toString(16));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return valor;
	}
	
	public static void main(String[] args) {
		System.out.println(criptografar("gudiao"));
	}
}
