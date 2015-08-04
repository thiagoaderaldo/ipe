/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;
import br.gov.ce.fortaleza.sesec.util.GeradorUUID;
/**
 *
 * @author Jorge
 */
public class GeradorUUIDMain{

    public static void main(String[] args) {
        
        String uuid = GeradorUUID.create();
        
        System.out.println(uuid);


    }
}
