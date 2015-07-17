/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.rest.security;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Jorge
 */
public class Authenticator {
    
    private static Authenticator authenticator = null;
    //Usado para armazenar <usuario, senha>
    private final Map<String, String> usersStorage = new HashMap();
    //Usado para guarda <chave-servico, usuario>
    private final Map<String, String> serviceKeyStorage = new HashMap();
    //Usado para guarda o Token de autenticacao <chave-servico, token>
    private final Map<String, String> authorizationTokensStorage = new HashMap();

    private Authenticator() {
        //Usado para representar a tabela usuario do banco de dados
        usersStorage.put("username1", "passwordForUser1");
        usersStorage.put("username2", "passwordForUser2");

        /*
         * As chaves de servico são pre-geradas pelo sistema e 
         * dada ao cliente para acessar a API REST. Aqui,
         * Cada usuario possui a sua respectica chave
         */
        serviceKeyStorage.put("f80ebc87-ad5c-4b29-9366-5359768df5a1", "username1");
        serviceKeyStorage.put("3b91cab8-926f-49b6-ba00-920bcf934c2a", "username2");
    }

    public static Authenticator getInstance() {
        if (authenticator == null) {
            authenticator = new Authenticator();
        }
        return authenticator;
    }

    public String login(String servicekey, String username, String password) throws LoginException {

        if (serviceKeyStorage.containsKey(servicekey)) {
            String usernameMath = serviceKeyStorage.get(servicekey);
            if (usernameMath.equals(username) && usersStorage.containsKey(username)) {
                String passwordMatch = usersStorage.get(username);
                if (passwordMatch.equals(password)) {
                    /*
                     * Uma vez que todos os parametros sao correspondidos,
                     * o authToken será armazenado no authorizationTokens
                     * O authToken sera necessario para cada chamada na APIRest
                     * e so e valida dentro da secao de login
                     */
                    String authToken = UUID.randomUUID().toString();
                    authorizationTokensStorage.put(authToken, username);
                    return authToken;
                }
            }
        }
        throw new LoginException("O login falhou");
    }
    
    public boolean isAuthTokenValid(String serviceKey, String authToken){
        if (isServiceKeyValid(serviceKey)) {
            String usernameMath1 = serviceKeyStorage.get(serviceKey);
            if (authorizationTokensStorage.containsKey(authToken)) {
                String usernameMath2 = authorizationTokensStorage.get(authToken);
                if (usernameMath1.equals(usernameMath2)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void logout(String serviceKey, String authToken) throws GeneralSecurityException{
        if (serviceKeyStorage.containsKey(serviceKey)) {
            String usernameMatch1 = serviceKeyStorage.get(serviceKey);
            if (authorizationTokensStorage.containsKey(authToken)) {
                String usernameMatch2 = authorizationTokensStorage.get(authToken);
                if (usernameMatch1.equals(usernameMatch2)) {
                    authorizationTokensStorage.remove(authToken);
                }
            }
        }
        throw new GeneralSecurityException("chave ou token invalidos");
    }

    public boolean isServiceKeyValid(String serviceKey) {
        return serviceKeyStorage.containsKey(serviceKey);
    }
    
}
