/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thiago Aderaldo Lessa.
 */
    @ManagedBean  
    @SessionScoped  
    public class Logout {  
      
        public String efetuarLogout(){  
            FacesContext fc = FacesContext.getCurrentInstance();  
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);  
            session.invalidate();
            
            return "/index.jsf";
        }  
    }  
