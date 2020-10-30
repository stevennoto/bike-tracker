package divvy.tracker.json;

import java.util.List;

public class StationList {
	String type;
	public List<Feature> features;

	public Station getStationById(long id) {
		if (features == null) return  null;
		for (Feature feature : features) {
			if (feature.properties.station.id == id) {
				return feature.properties.station;
			}
		}
		return null;
	}
}
