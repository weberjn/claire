package de.jwi.claire.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.jwi.claire.endpoint.ClaireEndpoint;

public final class WSServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(WSServlet.class.getName());


	@Override
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
		logger.info("init()");
	}

	private String j()
	{
		String m = "{\"origin\":\"chrome\",\"text\":\"hello\",\"date\":1674334246614}";

		JsonArrayBuilder arrayBuilder  = Json.createArrayBuilder();
		
		
        
		
		for (int i = 0; i < 3; i++) {

			arrayBuilder .add(m);
		}

		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("shares", arrayBuilder );
		
		JsonObject jsonObject = objectBuilder.build();
		
		StringWriter sw = new StringWriter();
		JsonWriter jsonWriter = Json.createWriter(sw);
		jsonWriter.writeObject(jsonObject);
		jsonWriter.close();

		String s = sw.toString();

		return s;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String referrer = req.getHeader("referer");
		
		logger.info("referrer: " + referrer);
		
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		
		out.print(j());
		

	}

	
	
}
