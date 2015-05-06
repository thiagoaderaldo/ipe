/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import br.gov.ce.fortaleza.sesec.util.DateManager;
import br.gov.ce.fortaleza.sesec.util.FacesUtils;

/**
 *
 * @author Thiago Aderaldo Lessa.
 */
@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*private boolean isConfirmedPassword(String password) throws Exception
     * {
     * new Exception();
     * }
     * 
     * @Override
     * public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
     * try {
     * if(!isConfirmedPassword(o.toString())){
     * FacesUtils.mensErro("atdMessages", "A senha digitada não confere.");
     * }
     * } catch (ParseException ex) {
     * Logger.getLogger(DateValidator.class.getName()).log(Level.SEVERE, null, ex);
     * }
     * }*/
}