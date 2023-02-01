
package de.jwi.claire.endpoint;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/share/{clientId}", configurator = SecurityConfigurator.class)
public class ClaireEndpoint {

	private static final Logger logger = Logger.getLogger(ClaireEndpoint.class.getName());

	public static final String KEY_USER = "UserPrincipal";
	private static final String SID = "SID";

	private static final AtomicInteger connectionIds = new AtomicInteger(0);

	static Queue<Session> sessionQueue = new ConcurrentLinkedQueue<>();

	static Queue<Session> activeEndpointQueue = new ConcurrentLinkedQueue<>();

	static AtomicReference<String> lastMessage = new AtomicReference<>();

	public ClaireEndpoint() {

		logger.info("ClaireEndpoint()");

		System.out.println("ClaireEndpoint()");
	}

	@OnOpen
    public void onOpen(@PathParam("clientId") String clientId, Session session) {
    	System.out.printf("@OnOpen(Endpoint=%s,Sesson=%s)%n", this, session);
    	
    	logger.info("clientId: " + clientId);
    	
    	// only set first time after login
    	String user = (String)session.getUserProperties().get(KEY_USER);
    	
    	if (user == null)
    	{
    		user = clientId;
    		session.getUserProperties().put(KEY_USER, user);
    	}
    	
    	System.out.printf("@OnOpen(): %s%n", user);
    	
    	Map<String, Object> userProperties = session.getUserProperties();
    	System.out.println("userProperties: " + userProperties);
    	
    	Map<String,List<String>> requestParameterMap = session.getRequestParameterMap();
    	System.out.println("requestParameterMap: " + requestParameterMap);

    	
        Integer n = connectionIds.getAndIncrement();

        session.getUserProperties().put(SID, n);
        System.out.println("sessionID: " + n);
    	
        sessionQueue.add(session);
        
        greet(session);
        
        String msg = lastMessage.get();
        if (msg != null)
        {
        	try {
        		session.getBasicRemote().sendText(msg);
       	 } catch (IOException e) {
             logger.log(Level.INFO, user, e);
          }    		 
        }
        
    }

	@OnClose
	public void onClose(Session session) {
		String user = (String)session.getUserProperties().get(KEY_USER);
		System.out.println("@OnClose: " + user);

		sessionQueue.remove(session);
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		String user = (String)session.getUserProperties().get(KEY_USER);
		System.out.println("@OnMessage(): " + user);
		broadcast(session, message);

		lastMessage.set(message);
	}

	@OnError
	public void onError(Session session, Throwable t) {
		String user = (String)session.getUserProperties().get(KEY_USER);

		sessionQueue.remove(session);
		logger.log(Level.INFO, "remove: " + user, t);
	}

	private void greet(Session session) {
		String user = (String)session.getUserProperties().get(KEY_USER);
		if (user != null) {
			JsonObject juser = Json.createObjectBuilder().add("user", user).build();

			StringWriter sw = new StringWriter();
			JsonWriter jsonWriter = Json.createWriter(sw);
			jsonWriter.writeObject(juser);
			jsonWriter.close();

			String s = sw.toString();
			try {

				session.getBasicRemote().sendText(s);
			} catch (IOException e) {
				logger.log(Level.INFO, user, e);
			}
		}
	}

	private static void broadcast(Session originSession, String msg) {
		String user = null;
		try {

			for (Session session : sessionQueue) {

				user = (String)session.getUserProperties().get(KEY_USER);

				System.out.printf("BroadCast to Session: %s, %s %n", session.toString(), user);
				session.getBasicRemote().sendText(msg);
				logger.log(Level.INFO, "Sent: {0}", msg);
			}

		} catch (IOException e) {
			logger.log(Level.INFO, user, e);
		}
	}
}
