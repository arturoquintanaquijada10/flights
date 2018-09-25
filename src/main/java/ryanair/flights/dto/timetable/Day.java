package ryanair.flights.dto.timetable;

import java.util.Arrays;

public class Day {	

	String day;
	Flight[] flights=new Flight[0];
	
	public Day(){}
	
	public Day(String day, Flight flight){
		setDay(day);
		addFlight(flight);
	}
	
	/**
	 * Add a flight to the flights array if not exists, or update the flight if exist
	 * @param flightIn
	 */
	public void addFlight(Flight flightIn){
		int i=0;
		boolean bHasFlight=false;

		for(Flight iFlight: getFlights()){	

			if(iFlight.getNumber().equals(flightIn.getNumber())){
				flights[i]=flightIn;
				bHasFlight=true;
				break;
			}			
			i++;
		}
		if(!bHasFlight){
			flights = Arrays.copyOf(flights, flights.length + 1);
			flights[flights.length-1]=flightIn;
		}
	}


	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Flight[] getFlights() {
		return flights;
	}

	public void setFlights(Flight[] flights) {
		this.flights = flights;
	}	

}