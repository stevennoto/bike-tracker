package divvy.tracker;

import java.util.List;

public class StationList {
	private List<Station> stationList;
	
	public List<Station> getStationList() {
		return stationList;
	}
	
	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}
	
	public Station getStation(long id) {
		if (stationList == null || stationList.isEmpty()) {
			return null;
		}
		for (Station station : stationList) {
			if (station != null && station.getId() == id) {
				return station;
			}
		}
		return null;
	}
	
	public Station getStation(String name) {
		if (stationList == null || stationList.isEmpty()) {
			return null;
		}
		for (Station station : stationList) {
			if (station != null && station.getStationName() != null && station.getStationName().equalsIgnoreCase(name)) {
				return station;
			}
		}
		return null;
	}
}
