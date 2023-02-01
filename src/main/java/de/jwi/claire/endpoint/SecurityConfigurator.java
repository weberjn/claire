package de.jwi.claire.endpoint;

import java.security.Principal;
import java.util.logging.Logger;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class SecurityConfigurator extends ServerEndpointConfig.Configurator {

	private static final Logger logger = Logger.getLogger(SecurityConfigurator.class.getName());

	
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
    	
    	Principal principal = request.getUserPrincipal();
    	Object session = request.getHttpSession();
    	logger.info("session: " + session);
    	
    	if (principal != null)
    	{
    		logger.info("UserPrincipal(): " + principal);
    		config.getUserProperties().put(ClaireEndpoint.KEY_USER, principal.getName());
    	}
    	else
    	{
    		//throw new SecurityException("no UserPrincipal");
    		logger.info("no UserPrincipal");
    	}
    }
}