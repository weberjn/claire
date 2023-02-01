package claire;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

public class JTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
		System.err.println(s);
	}

}
