/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.util;

import java.io.IOException;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
 
/**
 * Login module that simply matches name and password to perform authentication.
 * If successful, set principal to name and credential to "admin".
 *
 * @author Nicolas Fränkel
 * @since 2 avr. 2009
 */
public class PlainLoginModule implements LoginModule {
 
    /** Callback handler to store between initialization and authentication. */
    private CallbackHandler handler;
 
    /** Subject to store. */
    private Subject subject;
 
    /** Login name. */
    private String login;
 
    /**
     * This implementation always return false.
     *
     * @see javax.security.auth.spi.LoginModule#abort()
     */
    @Override
    public boolean abort() throws LoginException {
 
        return false;
    }
 
    /**
     * This is where, should the entire authentication process succeeds,
     * principal would be set.
     *
     * @see javax.security.auth.spi.LoginModule#commit()
     */
    @Override
    public boolean commit() throws LoginException {
 
        try {
 
            PlainUserPrincipal user = new PlainUserPrincipal(login);
            PlainRolePrincipal role = new PlainRolePrincipal("admin");
 
            subject.getPrincipals().add(user);
            subject.getPrincipals().add(role);
 
            return true;
 
        } catch (Exception e) {
 
            throw new LoginException(e.getMessage());
        }
    }
 
    /**
     * This implementation ignores both state and options.
     *
     * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject,
     *      javax.security.auth.callback.CallbackHandler, java.util.Map,
     *      java.util.Map)
     */
    @Override
    public void initialize(Subject aSubject, CallbackHandler aCallbackHandler, Map aSharedState, Map aOptions) {
 
        handler = aCallbackHandler;
        subject = aSubject;
    }
 
    /**
     * This method checks whether the name and the password are the same.
     *
     * @see javax.security.auth.spi.LoginModule#login()
     */
    @Override
    public boolean login() throws LoginException {
 
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("login");
        callbacks[1] = new PasswordCallback("senha", true);
 
        try {
 
            handler.handle(callbacks);
 
            String name = ((NameCallback) callbacks[0]).getName();
            String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
 
            if (!name.equals(password)) {
 
                throw new LoginException("Authentication failed");
                //FacesUtils.mensErro("allMessages", "Login ou senha inválido.");
            }
 
            login = name;
 
            return true;
 
        } catch (IOException e) {
 
            throw new LoginException(e.getMessage());
 
        } catch (UnsupportedCallbackException e) {
 
            throw new LoginException(e.getMessage());
        }
    }
 
    /**
     * Clears subject from principal and credentials.
     *
     * @see javax.security.auth.spi.LoginModule#logout()
     */
    @Override
    public boolean logout() throws LoginException {
 
        try {
 
            PlainUserPrincipal user = new PlainUserPrincipal(login);
            PlainRolePrincipal role = new PlainRolePrincipal("admin");
 
            subject.getPrincipals().remove(user);
            subject.getPrincipals().remove(role);
 
            return true;
 
        } catch (Exception e) {
 
            throw new LoginException(e.getMessage());
        }
    }
}
