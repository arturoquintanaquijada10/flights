package ryanair.flights.dto.service;

import java.util.Calendar;

public class InputData {	
	
	private  String departure;
	private  String arrival;
	private  Calendar departureDateTime;
	private Calendar arrivalDateTime;
	
	
	public InputData(String departure, String arrival, Calendar departureDateTime, Calendar arrivalDateTime) {
		this.departure = departure;
		this.arrival = arrival;
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
	}
	
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getArrival() {
		return arrival;
	}
	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
	public Calendar getDepartureDateTime() {
		return departureDateTime;
	}
	public void setDepartureDateTime(Calendar departureDateTime) {
		this.departureDateTime = departureDateTime;
	}
	public Calendar getArrivalDateTime() {
		return arrivalDateTime;
	}
	public void setArrivalDateTime(Calendar arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}
	
	@Override
	public String toString(){		
		return (new StringBuilder()).append(" departure : ").append(departure).append(" arrival : ").append(arrival).append(" departureDateTime : ").append(departureDateTime).append(" arrivalDateTime : ").append(arrivalDateTime).toString();
	}

}