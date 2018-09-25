package ryanair.flights.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import ryanair.flights.dto.timetable.TimetableMonth;
import ryanair.flights.exceptions.GenericException;

public interface ITimetableService {
	
	/**
	 * Get timetable from the service configured
	 * @param vars
	 * @return
	 * @throws GenericException
	 */
	public ResponseEntity<TimetableMonth> getTimetable( Map<String, String> vars) throws GenericException ;
}
