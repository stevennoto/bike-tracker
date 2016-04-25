package divvy.tracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public List<Station> getStations(Collection<Long> ids) {
		List<Station> stations = new ArrayList<>();
		if (stationBeanList == null || stationBeanList.isEmpty()) {
			return stations;
		}
		for (Station station : stationBeanList) {
			if (station != null && ids.contains(station.getId())) {
				stations.add(station);
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
