/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.ce.fortaleza.sesec.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
@FacesValidator("emailValidator")
public class EmailValidator implements Validator{

    @Override
    public void validate(FacesContext facesContext,
            UIComponent uIComponent, Object object) throws ValidatorException {

        String enteredEmail = (String)object;
        //Configura o padrão de String para email
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

        //Relaciona a string de email passada com o padrão esperado.
        Matcher m = p.matcher(enteredEmail);

        //Verifica se as marcas do padrão estão dentro da string,
        //tais como o '@' e o '.'.

        boolean matchFound = m.matches();

        //Se as marcas não forem encontradas um mensagem é enviada ao
        //usuário informando que o campo foi digitidado incorretamente
        //e é exibido um exemplo de como deve ser preenchido o campo.
        if (!matchFound) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Email não válido. " +
                    "Exemplo: fulano@provedordeemail.com");
            message.setSummary("Email não válido. " +
                    "Exemplo: fulano@provedordeemail.com");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }


}
