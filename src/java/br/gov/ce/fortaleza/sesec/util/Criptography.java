/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Thiago Aderaldo Lessa.
 */
public final class Criptography {
    /**
	 * Método para encriptar senhas.
	 * @param senha
	 * @return String com a senha md5 encriptada.
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String senha) throws NoSuchAlgorithmException  {  
		MessageDigest messageDigest  = MessageDigest.getInstance("MD5");  
		BigInteger hash = new BigInteger(1, messageDigest.digest(senha.getBytes()));  
		return  hash.toString(16);  
	}
        
}
