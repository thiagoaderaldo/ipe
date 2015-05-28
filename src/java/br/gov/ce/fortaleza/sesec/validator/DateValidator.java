/*
 * A classe DateValidator faz a verificação da realização
 * Quantidade Disponível e Estatus. Se o estatus escolhido for
 * INDISPONIVEL a quantidade disponível de algum material
 * NÃO pode ser maior ou igual a 1. Se o estatus escolhido for
 * DISPONIVEL a quantidade disponível de algum materia NÃO pode
 * ser menor ou igual a 0.
 */

package br.gov.ce.fortaleza.sesec.validator;

import br.gov.ce.fortaleza.sesec.util.DateManager;
import br.gov.ce.fortaleza.sesec.util.FacesUtils;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


/**
 *
 * @author Thiago Aderaldo Lessa
 */
@FacesValidator("DateValidator")
public class DateValidator implements Validator{

    private boolean isValidDate(String date) throws ParseException
    {
       if(DateManager.verificarValidadeDeData(date)){
           return true;
       }else{           
           return false;
       }
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        try {
            if(!isValidDate(o.toString())){
                FacesUtils.mensErro("atdMessages", "A data digitada não é válida.");                
            }
        } catch (ParseException ex) {
            Logger.getLogger(DateValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
