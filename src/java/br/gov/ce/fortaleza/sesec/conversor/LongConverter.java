/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.ce.fortaleza.sesec.conversor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
@FacesConverter("longConverter")
public class LongConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) throws ConverterException {

        Long resultado = null;

        try
        {
            resultado =  new Long(Long.parseLong(valor));

        }
        catch (NumberFormatException nfe)
        {
            FacesMessage message = new FacesMessage("Ocorreu um erro de conversão. Digite somente números nesse campo. Erro:" + nfe);

            message.setSeverity(FacesMessage.SEVERITY_ERROR);

            throw new ConverterException(message);
        }
           return resultado;
       }


    @Override
       public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) throws ConverterException {

          String resultado ="";

          if (obj != null)
          {

            resultado = obj.toString();

          }
            return resultado;
          }


}
