package ryanair.flights.dto.interconnections;

import java.util.Arrays;

public class InterconnectionsResults {	
	
	private Interconnection[] interconnection=new Interconnection[0];

	public Interconnection[] getInterconnection() {
		return interconnection;
	}

	public void setInterconnection(Interconnection[] interconnection) {
		this.interconnection = interconnection;
	} 	
	
	public void addInterconnection(Interconnection interconnectionIn){
		
		interconnection = Arrays.copyOf(interconnection, interconnection.length + 1);
		interconnection[interconnection.length-1]=interconnectionIn;		
	}
	
	
}