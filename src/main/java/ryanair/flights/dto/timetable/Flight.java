package ryanair.flights.dto.timetable;

public class Flight {	

	private  String number;
	private  String departureTime;
	private  String arrivalTime;
	public String getNumber() {
		return number;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	

}