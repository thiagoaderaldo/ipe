/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jorge
 */
public class JpaControllerUtil {
    
    private static EntityManagerFactory emf = buildEntityManagerFactory(); 
    
    private static EntityManagerFactory buildEntityManagerFactory(){
        try {
            return Persistence.createEntityManagerFactory("ipePU");
        } catch (Exception e) {
            System.err.println("Não foi possivel inicializar frabrica de conexões. "+e);
            throw new ExceptionInInitializerError(e);
        }
        
    }
    public static EntityManagerFactory getEntityManagerFactory(){
        return emf;
    }
}
