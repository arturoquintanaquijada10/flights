package ryanair.flights.services;

import ryanair.flights.dto.routes.Routes;
import ryanair.flights.exceptions.GenericException;

public interface IRoutesService {

	
	/**
	 * Get all routes available from the service configured.
	 * @return
	 * @throws GenericException
	 */
	public  Routes getRoutes( ) throws GenericException;
}
