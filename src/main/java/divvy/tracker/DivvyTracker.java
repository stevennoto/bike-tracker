package divvy.tracker;

import divvy.tracker.json.Station;
import divvy.tracker.json.StationList;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
@PropertySource(name="stationsProperties",value="classpath:/divvy/tracker/stations.properties")
public class DivvyTracker {
	@Autowired
	Environment env;
	
	// Properties:
	
	@Value( "${divvytracker.title}" )
	private String appTitle;
	
	@Value( "${divvytracker.stations.url}" )
	private String stationsUrl;
	
	@Value("#{T(java.util.Arrays).asList('${divvytracker.stations}')}")
	private List<String> stationInfoList;
	
	// Routings:
	
	@RequestMapping("/")
	@ResponseBody
	public String divvy() {
		try {
			// Get Divvy info
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
			
			// Parse station info
			StationList stationBeanList = new Gson().fromJson(webpageText, StationList.class);
			
			// Get web templates
			String webTemplate = getTemplateFile("template.html");
			String stationTemplate = getTemplateFile("template-station.html");
			
			// Fill in staion details for desired stations
			String stationHtml = "";
			for (String stationInfo : stationInfoList) {
				StationDTO stationDTO = getStationFromProperty(stationInfo);
				long stationId = stationDTO.getId();
				Station station = stationBeanList.getStation(stationId);
				String thisStationHtml = stationTemplate;
				thisStationHtml = thisStationHtml.replace("{TITLE}", stationDTO.getName());
				thisStationHtml = thisStationHtml.replace("{SUBTITLE}", stationDTO.getAddress());
				thisStationHtml = thisStationHtml.replace("{NUM_BIKES}", "" + station.getAvailableBikes());
				thisStationHtml = thisStationHtml.replace("{NUM_DOCKS}", "" + station.getAvailableDocks());
				thisStationHtml = thisStationHtml.replace("{PERCENT_BIKES}", "" + (100 * station.getAvailableBikes() / station.getTotalDocks()));
				thisStationHtml = thisStationHtml.replace("{PERCENT_DOCKS}", "" + (100 * station.getAvailableDocks() / station.getTotalDocks()));
				stationHtml += thisStationHtml;
			}
			
			// Fill in main template and return
			webTemplate = webTemplate.replace("{PAGE_TITLE}", appTitle);
			webTemplate = webTemplate.replace("{STATIONS}", stationHtml);
			return webTemplate;

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
			
		} catch (IOException e) {
//                Map<String, Object> attributes = new HashMap<>();
//                attributes.put("message", "Error: " + e.getMessage());
//                return new ModelAndView(attributes, "index.ftl");
			return "Error: " + e.getMessage();
		}
	}
	
	private String getStationDetailsString(StationDTO stationDTO, Station station) {
		String details = "<b>" + stationDTO.getName() + "</b> (" + stationDTO.getAddress() + ") (ID "
				+ stationDTO.getId() + "): "
				+ station.getAvailableBikes() + " bikes, "
				+ station.getAvailableDocks() + " docks, "
				+ station.getTotalDocks()+ " total docks";
		return details;
	}
	
	private StationDTO getStationFromProperty(String stationInfo) {
		try {
			String[] stationStrings = stationInfo.split("\\|");
			return new StationDTO(
					Long.parseLong(stationStrings[0]),
					stationStrings[1],
					stationStrings[2]);
		} catch (NullPointerException|NumberFormatException|ArrayIndexOutOfBoundsException e) {
// log? throw?
			return null;
		}
	}
	
	private String getTemplateFile(String filename) throws IOException {
		// TODO: improve reading code. Autowire via Spring?
		InputStream inStream = DivvyTracker.class.getResourceAsStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		String line;
		String fileText = "";
		while ((line = br.readLine()) != null) {
			fileText += line + "\n";
		}
		return fileText;
	}
	
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(DivvyTracker.class);
		application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
		SpringApplication.run(DivvyTracker.class, args);
	}
}
