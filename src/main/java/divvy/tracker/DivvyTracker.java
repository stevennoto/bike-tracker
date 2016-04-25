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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class DivvyTracker {

	@RequestMapping("/")
	@ResponseBody
	public String index() {
		return "Hello world, sup?";
	}
	
	@RequestMapping("/divvy")
	@ResponseBody
	public String divvy() {
		try {
			URL url = new URL("http://www.divvybikes.com/stations/json");
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
			JsonObject jsonObject = new JsonParser().parse(webpageText).getAsJsonObject();
			String executionTime = jsonObject.get("executionTime").getAsString();

			JsonArray stations = jsonObject.getAsJsonArray("stationBeanList");
			JsonObject firstStation = stations.get(0).getAsJsonObject();
			String firstStationName = firstStation.get("stationName").toString();

//                Map<String, Object> attributes = new HashMap<>();
//                attributes.put("message", "Found dataz! executionTime=" + executionTime + ", first station name=" + firstStationName);
//                return new ModelAndView(attributes, "index.ftl");
			return "Found dataz! executionTime=" + executionTime + ", first station name=" + firstStationName;
		} catch (IOException e) {
//                Map<String, Object> attributes = new HashMap<>();
//                attributes.put("message", "Error: " + e.getMessage());
//                return new ModelAndView(attributes, "index.ftl");
			return "Error: " + e.getMessage();
		}
	}
	public static void main(String[] args) {
//		try {
//			InputStream jsonStream = DivvyTracker.class.getResourceAsStream("divvy.json");
//			BufferedReader br = new BufferedReader(new InputStreamReader(jsonStream));
//			String line;
//			String webpageText = "";
//			while ((line = br.readLine()) != null) {
//				webpageText += line;
//			}
//			StationList stationBeanList = new Gson().fromJson(webpageText, StationList.class);
//			Long[] idsToLoad = { 174L, 192L };
//			List<Long> idList = new ArrayList<>();
//			idList.addAll(Arrays.asList(idsToLoad));
//			List<Station> stations = stationBeanList.getStations(idList);
//			System.out.println(stations.get(0).getAvailableBikes());
//			System.out.println(stations.get(1).getAvailableBikes());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		SpringApplication application = new SpringApplication(DivvyTracker.class);
		application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
		SpringApplication.run(DivvyTracker.class, args);
	}
}
