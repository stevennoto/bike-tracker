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
import java.text.SimpleDateFormat;
import java.util.Date;
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
			// Get Divvy info, parse it
			String webpageText = getWebpageSimple(stationsUrl);
			StationList stationBeanList = new Gson().fromJson(webpageText, StationList.class);
			
			// Get web templates
			String webTemplate = getTemplateFile("template.html");
			String stationTemplate = getTemplateFile("template-station.html");
			
			// Fill in station details for desired stations
			String stationHtml = "";
			for (String stationInfo : stationInfoList) {
				StationDTO stationDTO = getStationFromProperty(stationInfo);
				long stationId = stationDTO.getId();
				Station station = stationBeanList.getStationById(stationId);
				int bikes = station.bikes_available;
				int docks = station.docks_available;
				int totalDocks = bikes + docks;
				String thisStationHtml = stationTemplate;
				thisStationHtml = thisStationHtml.replace("{TITLE}", stationDTO.getName());
				thisStationHtml = thisStationHtml.replace("{SUBTITLE}", stationDTO.getAddress());
				thisStationHtml = thisStationHtml.replace("{NUM_BIKES}", "" + bikes);
				thisStationHtml = thisStationHtml.replace("{NUM_DOCKS}", "" + docks);
				thisStationHtml = thisStationHtml.replace("{PERCENT_BIKES}", "" + (100 * bikes / totalDocks));
				thisStationHtml = thisStationHtml.replace("{PERCENT_DOCKS}", "" + (100 * docks / totalDocks));
				stationHtml += thisStationHtml;
			}
			
			// Fill in main page template
			webTemplate = webTemplate.replace("{PAGE_TITLE}", appTitle);
			webTemplate = webTemplate.replace("{STATIONS}", stationHtml);
			String now = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
			webTemplate = webTemplate.replace("{DATA_TIMESTAMP}", now);
			
			// Return generated HTML
			return webTemplate;
		} catch (IOException e) {
			// Return very basic error page
			return "<html><body><h1>Error: " + e.getMessage() + "</h1></body></html>";
		}
	}
	
	private StationDTO getStationFromProperty(String stationInfo) {
		try {
			String[] stationStrings = stationInfo.split("\\|");
			return new StationDTO(
					Long.parseLong(stationStrings[0]),
					stationStrings[1],
					stationStrings[2]);
		} catch (NullPointerException|NumberFormatException|ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * Simple helper method to get a webpage, no muss no fuss
	 * @param url URL to fetch
	 * @return webpage contents
	 * @throws exception if error reading webpage
	 */
	private String getWebpageSimple(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		HttpURLConnection.setFollowRedirects(true);
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		String encoding = conn.getContentEncoding();
		InputStream inStr;
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
		return webpageText;
	}
	
	// Test version of above, for local development
	private String getWebpageLocal(String url) throws IOException {
		InputStream jsonStream = DivvyTracker.class.getResourceAsStream("divvy.json");
		BufferedReader br = new BufferedReader(new InputStreamReader(jsonStream));
		String line;
		String webpageText = "";
		while ((line = br.readLine()) != null) {
			webpageText += line;
		}
		return webpageText;
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
