package br.com.milenio.vendingmachine.repository;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Test {
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String s = "gudiao";
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes());
		System.out.println("MD5: " + new BigInteger(1, m.digest()).toString(16));
		System.out.println("a3d4a413a3f981422388a46cbf1292fb");
	}
}
