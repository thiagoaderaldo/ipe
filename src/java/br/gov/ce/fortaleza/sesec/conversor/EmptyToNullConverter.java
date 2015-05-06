/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.ce.fortaleza.sesec.conversor;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
@FacesConverter("emptyToNullConverter")
public class EmptyToNullConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, 
            UIComponent component, String value) throws ConverterException{

        if (value == null || value.isEmpty() ||
                value.trim().isEmpty() || value.trim().length() == 0)
        {
            System.out.println("Entrou primeiro if do conversor.");
            if (component instanceof EditableValueHolder)
            {

                ((EditableValueHolder) component).setSubmittedValue(null);
                System.out.println("Executou o segundo if do conversor.");
            }
            return null;
        }
        return value;
    }

    @Override
    public String getAsString(FacesContext facesContext,
            UIComponent component, Object value)
    {
        return (value == null) ? null : value.toString();
    }

}

