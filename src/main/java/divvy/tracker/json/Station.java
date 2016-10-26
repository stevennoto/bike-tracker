package divvy.tracker.json;

public class Station {
	private long id;
	private String stationName;
	private int availableDocks;
	private int totalDocks;
	private double latitude;
	private double longitude;
	private String statusValue;
	private long statusKey;
	private String status;
	private int availableBikes;
	private String stAddress1;
	private String stAddress2;
	private String city;
	private String postalCode;
	private String location;
	private String altitude;
	private boolean testStation;
	private String lastCommunicationTime;
	private boolean is_renting;
	private String landMark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public int getAvailableDocks() {
		return availableDocks;
	}

	public void setAvailableDocks(int availableDocks) {
		this.availableDocks = availableDocks;
	}

	public int getTotalDocks() {
		return totalDocks;
	}

	public void setTotalDocks(int totalDocks) {
		this.totalDocks = totalDocks;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(long statusKey) {
		this.statusKey = statusKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAvailableBikes() {
		return availableBikes;
	}

	public void setAvailableBikes(int availableBikes) {
		this.availableBikes = availableBikes;
	}

	public String getStAddress1() {
		return stAddress1;
	}

	public void setStAddress1(String stAddress1) {
		this.stAddress1 = stAddress1;
	}

	public String getStAddress2() {
		return stAddress2;
	}

	public void setStAddress2(String stAddress2) {
		this.stAddress2 = stAddress2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public boolean isTestStation() {
		return testStation;
	}

	public void setTestStation(boolean testStation) {
		this.testStation = testStation;
	}

	public String getLastCommunicationTime() {
		return lastCommunicationTime;
	}

	public void setLastCommunicationTime(String lastCommunicationTime) {
		this.lastCommunicationTime = lastCommunicationTime;
	}

	public boolean isIs_renting() {
		return is_renting;
	}

	public void setIs_renting(boolean is_renting) {
		this.is_renting = is_renting;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}
	
	
}
