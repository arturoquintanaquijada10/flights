package ryanair.flights.dto.routes;

public class Route {	
	
    private  String airportFrom;
    private  String airportTo;
    private  String connectingAirport;
    private  boolean newRoute;
    private  boolean seasonalRoute;
    private  String operator;
    private  String group;
    
    
    public Route(){}
    
	public Route(String airportFrom, String airportTo, String connectingAirport, boolean newRoute,
			boolean seasonalRoute, String operator, String group) {
		super();
		this.airportFrom = airportFrom;
		this.airportTo = airportTo;
		this.connectingAirport = connectingAirport;
		this.newRoute = newRoute;
		this.seasonalRoute = seasonalRoute;
		this.operator = operator;
		this.group = group;
	}

	public String getAirportFrom() {
		return airportFrom;
	}

	public String getAirportTo() {
		return airportTo;
	}

	public String getConnectingAirport() {
		return connectingAirport;
	}

	public String getOperator() {
		return operator;
	}

	public String getGroup() {
		return group;
	}
	
	
	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}

	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}

	public void setConnectingAirport(String connectingAirport) {
		this.connectingAirport = connectingAirport;
	}
	

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isNewRoute() {
		return newRoute;
	}

	public void setNewRoute(boolean newRoute) {
		this.newRoute = newRoute;
	}

	public boolean isSeasonalRoute() {
		return seasonalRoute;
	}

	public void setSeasonalRoute(boolean seasonalRoute) {
		this.seasonalRoute = seasonalRoute;
	}

	
}