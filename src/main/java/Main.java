import java.util.Map.Entry;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import io.javalin.Javalin;

public class Main {

	private static String cookie = "";
	
    // Main function that starts the web app
    public static void main(String[] args) {
        Javalin app = Javalin.create()
            .port(getHerokuAssignedPort())
            .enableStaticFiles("/public")
            .get("/refresh", (req, res) -> res.body(doLoginAttempt()));
            //.post("/", (req, res) -> res.body(addPlaceName(req.bodyAsClass(Place.class))))
            //.delete("/*", (req, res) -> res.body(removePlaceName(req.path().substring(1)) ? "deleted" : "not found"));
    }

    // Helper function to get the port Heroku wants us to use
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000;
    }
    
    private static String doLoginAttempt() {
    	String output = "";
    	cookie = "";
		
    	Response response = Login.getLogin(System.getenv("TM_UNIT"), System.getenv("TM_USER"), System.getenv("TM_PASS"));
		output += "Status: " + response.getStatus();
		for (Entry<String, NewCookie> entry  : response.getCookies().entrySet()) {
			cookie += entry.getKey() + "=" + entry.getValue();
		}
		
		Response response2 = Login.getScouts();
		
		return output + response2.getStatus() + response2.readEntity(String.class);
    }

    /* Commenting out the PlaceName stuff for now - DELETE later
    
    // Search through the place names and return a list of matches
    private static List<Place> findPlaceNames(String query) {
        List<Place> matches = new ArrayList<Place>();
        for (Entry<String, Place> entry : places.entrySet()) {
            if (entry.getKey().toLowerCase().contains(query.toLowerCase())) {
                matches.add(entry.getValue());
            }
        }
        return matches;
    }

    // Add a given place to the data set
    private static String addPlaceName(Place place) {
        places.put(place.name, place);
        return "added";
    }

    // Deletes a entry from the list of places if it exists
    private static boolean removePlaceName(String key) {
        if (places.remove(key) != null) {
            return true;
        } else {
            return false;
        }
    }

    // Simple data structure to hold place information
    private static class Place {
        public String name;
        public Double lat;
        public Double lon;

        public Place() {

        }

        public Place(String name, Double lat, Double lon) {
            this.name = name;
            this.lat = lat;
            this.lon = lon;
        }
    }
    
    */
}
