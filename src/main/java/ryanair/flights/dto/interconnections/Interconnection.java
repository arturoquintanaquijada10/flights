package ryanair.flights.dto.interconnections;

import java.util.Arrays; 

public class Interconnection {	
	
	private int stops;
	private Leg[] legs=new Leg[0];
	
	public Interconnection(int stops, Leg leg ){
		setStops( stops);
		addLeg( leg);
	}
	
	public Interconnection( ){}
	
	public void addLeg(Leg leg){			
		legs = Arrays.copyOf(legs, legs.length + 1);
		legs[legs.length-1]=leg;	
	}
	
	public int getStops() {
		return stops;
	}
	
	public void setStops(int stops) {
		this.stops = stops;
	}
	
	public Leg[] getLegs() {
		return legs;
	}
	
	public void setLegs(Leg[] legs) {
		this.legs = legs;
	} 		
   
	
}