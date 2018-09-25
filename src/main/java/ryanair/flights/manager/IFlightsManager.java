package ryanair.flights.manager;

import java.util.Date;

import org.springframework.stereotype.Component;

import ryanair.flights.dto.interconnections.InterconnectionsResults;
import ryanair.flights.exceptions.GenericException;

@Component
public interface IFlightsManager {

	
	/**
	 * Get all flights and timetables available for direct flights or connections flights
	 * @param departure
	 * @param arrival
	 * @param dateDeparture
	 * @param dateArrival
	 * @return
	 * @throws GenericException
	 */
	public InterconnectionsResults getAllValidTimetables(String departure,  String arrival, Date dateDeparture, Date dateArrival) throws GenericException;
	
}