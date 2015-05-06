/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.util;

/**
 *
 * @author Thiago Aderaldo Lessa.
 */
public class IpeUtil {
    
    public static boolean isPasswordCorrect(String password, String confirmationPassord){
        
        if(password.equals(confirmationPassord)){
            return true;
        }else{
            return false;
        }
    }
}
