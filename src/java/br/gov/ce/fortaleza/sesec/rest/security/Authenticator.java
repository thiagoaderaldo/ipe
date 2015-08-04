/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.rest.security;

import br.gov.ce.fortaleza.sesec.entities.ChaveApi;
import br.gov.ce.fortaleza.sesec.entities.Usuarios;
import br.gov.ce.fortaleza.sesec.jpa.controller.ChaveApiJpaController;
import br.gov.ce.fortaleza.sesec.util.Criptography;
import br.gov.ce.fortaleza.sesec.util.JpaControllerUtil;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Jorge
 */
public class Authenticator {

    private final static Logger log = Logger.getLogger(Authenticator.class.getName());
    //COD - ORIGINAL
    //Usado para guarda <chave-servico, usuario>
    //private final Map<String, String> serviceKeyStorage = new HashMap();
    //Usado para guarda o Token de autenticacao <chave-servico, token>
    //private final Map<String, String> usersStorage = new HashMap();
    //private final Map<String, String> authorizationTokensStorage = new HashMap();
    private static Authenticator authenticator = null;
    private List<ChaveApi> serviceKey;
    private ChaveApiJpaController jpaController = null;
    //Armazenamento de chave api para tokens
    private final Map<String, List<String>> serviceKeyAuthTokenStorage = new HashMap();
    //tokens

    private ChaveApiJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ChaveApiJpaController(JpaControllerUtil.getEntityManagerFactory());
        }
        return jpaController;
    }

    private Authenticator() {
        //COD - ORIGINAL
        //Usado para representar a tabela usuario do banco de dados
        //usersStorage.put("username1", "passwordForUser1");
        //usersStorage.put("username2", "passwordForUser2");

        /*
         * As chaves de servico são pre-geradas pelo sistema e 
         * dada ao cliente para acessar a API REST. Aqui,
         * Cada usuario possui a sua respectica chave
         */
        //serviceKeyStorage.put("f80ebc87-ad5c-4b29-9366-5359768df5a1", "username1");
        //serviceKeyStorage.put("3b91cab8-926f-49b6-ba00-920bcf934c2a", "username2");
        if (serviceKey == null) {
            serviceKey = getJpaController().findChaveApiEntities();
            log.info("NOVO OBJETO AUTENTICADOR");
        }
    }

    public static Authenticator getInstance() {
        if (authenticator == null) {
            authenticator = new Authenticator();
        }
        return authenticator;
    }

    public String login(String servicekey, Usuarios usuario, String password)
            throws LoginException, NoSuchAlgorithmException {

        if (usuario.getSenha().equals(Criptography.md5(password))) {
            /*
             * Uma vez que todos os parametros sao correspondidos,
             * o authToken será armazenado no authorizationTokens
             * O authToken sera necessario para cada chamada na APIRest
             * e so e valida dentro da secao de login
             */
            if (serviceKeyAuthTokenStorage.containsKey(servicekey)) {
                String authToken = UUID.randomUUID().toString();
                log.info("token gerado");
                serviceKeyAuthTokenStorage.get(servicekey).add(authToken);
                return authToken;
            } else {
                String authToken = UUID.randomUUID().toString();
                log.info("token gerado e adicionado a nova chave");
                List<String> tokens = new ArrayList<String>();
                serviceKeyAuthTokenStorage.put(servicekey, tokens);
                serviceKeyAuthTokenStorage.get(servicekey).add(authToken);
                return authToken;
            }
        }
        throw new LoginException("O login falhou");
        /* COD - ORIGINAL
         if (serviceKeyStorage.containsKey(servicekey)) {
         String usernameMath = serviceKeyStorage.get(servicekey);
         if (usernameMath.equals(username) && usersStorage.containsKey(username)) {
         String passwordMatch = usersStorage.get(username);
         if (passwordMatch.equals(password)) {
                    
         String authToken = UUID.randomUUID().toString();
         authorizationTokensStorage.put(authToken, usuario.getLogin());
         return authToken;
         }
         }
         }
         */
    }

    public boolean isAuthTokenValid(String serviceKey, String authToken) {
        if (isServiceKeyValid(serviceKey)) {
            if (serviceKeyAuthTokenStorage.get(serviceKey) != null) {
                for (String token : serviceKeyAuthTokenStorage.get(serviceKey)) {
                    if (token.equals(authToken)) {
                        log.info("Token valido");
                        return true;
                    }else{
                        log.info("Token não é valido ou não pertence a chave: "+serviceKey);
                    }
                }
            } else {
                //implementar um retorno json para caso o token não for mais valido
                log.info("Lista de tokens vazia");
            }
        }
        return false;
        /*COD - ORIGINAL
         * if (isServiceKeyValid(serviceKey)) {
         String usernameMath1 = serviceKeyStorage.get(serviceKey);
         if (authorizationTokensStorage.containsKey(authToken)) {
         String usernameMath2 = authorizationTokensStorage.get(authToken);
         if (usernameMath1.equals(usernameMath2)) {
         return true;
         }
         }
         }
         return false;
         */
    }

    public void logout(String serviceKey, String authToken) throws GeneralSecurityException {
        if (isServiceKeyValid(serviceKey)) {
            if (serviceKeyAuthTokenStorage.get(serviceKey) != null) {
                for (Iterator<String> i = serviceKeyAuthTokenStorage.get(serviceKey).iterator(); i.hasNext();) {
                    String token = i.next();
                    if (token.equals(authToken)) {
                        i.remove();
                        log.info("token removido");
                        return;
                    }
                }
            } else {
                log.info("Lista de tokens vazia");
                throw new GeneralSecurityException("chave ou token invalidos");
            }
        }
        throw new GeneralSecurityException("chave ou token invalidos");
        /*COD - ORIGINAL
         if (serviceKeyStorage.containsKey(serviceKey)) {
         String usernameMatch1 = serviceKeyStorage.get(serviceKey);
         if (authorizationTokensStorage.containsKey(authToken)) {
         String usernameMatch2 = authorizationTokensStorage.get(authToken);
         if (usernameMatch1.equals(usernameMatch2)) {
         authorizationTokensStorage.remove(authToken);
         }
         }
         }
         throw new GeneralSecurityException("chave ou token invalidos");*/
    }

    public boolean isServiceKeyValid(String sKey) {
        for (ChaveApi key : serviceKey) {
            if (key.getChave().equals(sKey)) {
                return true;
            }
        }
        return false;
        //código original
        //return serviceKeyStorage.containsKey(serviceKey);
    }
}
