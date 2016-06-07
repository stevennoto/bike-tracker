package divvy.tracker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
@Configuration
@PropertySource("classpath:/divvy/tracker/stations.properties")
public class DivvyTracker {
	@Autowired
	Environment env;
	
	// Properties:
	
	@Value( "${divvytracker.stations.url}" )
	private String stationsUrl;
	
	@Value( "${divvytracker.origin.station.ids}" )
	private String originStationIds;
	
	@Value( "${divvytracker.destination.station.ids}" )
	private String destinationStationIds;
	
	// Routings:
	
	@RequestMapping("/")
	@ResponseBody
	public String divvy() {
		try {
			// todo: check properties			
			URL url = new URL(stationsUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			String encoding = conn.getContentEncoding();
			InputStream inStr = null;
			if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				inStr = new GZIPInputStream(conn.getInputStream());
			} else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
				inStr = new InflaterInputStream(conn.getInputStream(), new Inflater(true));
			} else {
				inStr = conn.getInputStream();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(inStr));
			String line;
			String webpageText = "";
			while ((line = br.readLine()) != null) {
				webpageText += line;
			}

			// Offline test:
//			InputStream jsonStream = DivvyTracker.class.getResourceAsStream("divvy.json");
//			BufferedReader br = new BufferedReader(new InputStreamReader(jsonStream));
//			String line;
//			String webpageText = "";
//			while ((line = br.readLine()) != null) {
//				webpageText += line;
//			}
			
//			JsonObject jsonObject = new JsonParser().parse(webpageText).getAsJsonObject();
//			String executionTime = jsonObject.get("executionTime").getAsString();
//
//			JsonArray stations = jsonObject.getAsJsonArray("stationBeanList");
//			JsonObject firstStation = stations.get(0).getAsJsonObject();
//			String firstStationName = firstStation.get("stationName").toString();
//                Map<String, Object> attributes = new HashMap<>();
//                attributes.put("message", "Found dataz! executionTime=" + executionTime + ", first station name=" + firstStationName);
//                return new ModelAndView(attributes, "index.ftl");
//			return "Found data! executionTime=" + executionTime + ", first station name=" + firstStationName;
			
			StationList stationBeanList = new Gson().fromJson(webpageText, StationList.class);
			List<Long> idsToLoadOrigin = getStationIds(originStationIds);
			List<Long> idsToLoadDestination = getStationIds(destinationStationIds);
			List<Station> stations = stationBeanList.getStations(idsToLoadOrigin);
			String returnString = "";
			for (Station station : stations) {
				returnString += getStationDetails(station);
				returnString += "<br/><br/>";
			}
				returnString += "<br/><br/>";
			stations = stationBeanList.getStations(idsToLoadDestination);
			for (Station station : stations) {
				returnString += getStationDetails(station);
				returnString += "<br/><br/>";
			}
			return returnString;
		} catch (IOException e) {
//                Map<String, Object> attributes = new HashMap<>();
//                attributes.put("message", "Error: " + e.getMessage());
//                return new ModelAndView(attributes, "index.ftl");
			return "Error: " + e.getMessage();
		}
	}
	
	private String getStationDetails(Station station) {
		String details = "Station '" + station.getStationName() + "' ("
				+ station.getId() + "): "
				+ station.getAvailableBikes() + " bikes, "
				+ station.getAvailableDocks() + " docks";
		return details;
	}
	
	private List<Long> getStationIds(String propertyString) {
		List<Long> listStationIds = new ArrayList<>();
		if (propertyString == null) {
			return listStationIds;
		}
		String stationIdsString[] = propertyString.split(",");
		for (String stationIdString: stationIdsString) {
			try {
				listStationIds.add(Long.parseLong(stationIdString));
			} catch (NumberFormatException e) {}
		}
		return listStationIds;
	}
	
//	@RequestMapping("/test")
//	@ResponseBody
//	public String testThing() {
//		if (env == null) return "env=null";
////		String thing = env.getRequiredProperty("divvytracker.source.station.ids");
//				//env.getRequiredProperty("divvytracker.source.station.ids")
//		return thing;
////		System.out.println("prop:" + thing != null ? thing : "null"); // , String.class
//	}
	
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(DivvyTracker.class);
		application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
		SpringApplication.run(DivvyTracker.class, args);
		DivvyTracker dt = new DivvyTracker();
	}
}
