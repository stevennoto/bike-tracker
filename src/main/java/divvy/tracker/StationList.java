package divvy.tracker;

import java.util.ArrayList;
import java.util.List;

public class StationList {
    private String executionTime;
	private List<Station> stationBeanList;

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}
	
	public List<Station> getStationList() {
		return stationBeanList;
	}
	
	public void setStationList(List<Station> stationList) {
		this.stationBeanList = stationList;
	}
	
	public Station getStations(long id) {
		if (stationBeanList == null || stationBeanList.isEmpty()) {
			return null;
		}
		for (Station station : stationBeanList) {
			if (station != null && station.getId() == id) {
				return station;
			}
		}
		return null;
	}
	
	public List<Station> getStations(List<Long> ids) {
		List<Station> stations = new ArrayList<>();
		if (stationBeanList == null || stationBeanList.isEmpty()) {
			return stations;
		}
		for (Long id : ids) {
			for (Station station : stationBeanList) {
				if (station != null && id != null && id.equals(station.getId())) {
					stations.add(station);
					break;
				}
			}
		}
		return stations;
	}
	
	public Station getStation(String name) {
		if (stationBeanList == null || stationBeanList.isEmpty()) {
			return null;
		}
		for (Station station : stationBeanList) {
			if (station != null && station.getStationName() != null && station.getStationName().equalsIgnoreCase(name)) {
				return station;
			}
		}
		return null;
	}
}
