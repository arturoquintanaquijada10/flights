package ryanair.flights.dto.routes;

/**
 * @author Arturo Quintana
 *
 */
public class Interconnections {	
	
    private final String departure;
    private final String arrival;
    private final String departureDateTime;
    private final String arrivalDateTime;

    public Interconnections(String departure, String arrival,String departureDateTime,String arrivalDateTime) {
        this.departure = departure;
        this.arrival = arrival;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }

	public String getDeparture() {
		return departure;
	}

	public String getArrival() {
		return arrival;
	}

	public String getDepartureDateTime() {
		return departureDateTime;
	}

	public String getArrivalDateTime() {
		return arrivalDateTime;
	}
	
   
}