package ryanair.flights.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ryanair.flights.dto.interconnections.InterconnectionsResults;
import ryanair.flights.exceptions.GenericException;
import ryanair.flights.manager.IFlightsManager;


@Controller
@RequestMapping( value = "/" )
@Validated
public class FlightsController{

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightsController.class);	
	
	@Autowired
	@Qualifier("manageFlights")
	IFlightsManager oFlightsManager;
	

	/**
	 * Manage the input data and get all timetables available for the input data
	 * @param departure
	 * @param arrival
	 * @param dateDeparture
	 * @param dateArrival
	 * @return
	 * @throws GenericException
	 */
	@RequestMapping( value = "/interconnections", method = RequestMethod.GET )
	@ApiOperation("Serves information about possible direct and interconnected flights")
	public ResponseEntity<Object> findInterconnections( 
			@ApiParam( value = "Departure airport IATA code", required = true)  @RequestParam(value="departure")  String departure, 
			@ApiParam(value = "Arrival airport IATA code", required = true) @RequestParam(value="arrival")  String  arrival,  
			@ApiParam(value = "Departure time format yyyy-MM-dd'T'HH:mm", required = true) @RequestParam(value="departureDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dateDeparture,
			@ApiParam(value = "Arrival time format yyyy-MM-dd'T'HH:mm", required = true) @RequestParam(value="arrivalDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dateArrival
			) throws GenericException{

			
			long startTime = System.nanoTime();
			LOGGER.info("findInterconnections() departure: {}   arrival: {}  departureDateTime: {} arrivalDateTime : {} ",departure, arrival, dateDeparture, dateArrival);		
			
			InterconnectionsResults oInterconnectionsResults= oFlightsManager.getAllValidTimetables( departure,   arrival,  dateDeparture,  dateArrival);

			long endTime = System.nanoTime();
			long duration = (endTime - startTime)/1000000;  
			LOGGER.info("findInterconnections() : >>>>>  TOTAL TIME SPEND: {} milliseconds in  departure: {}   arrival: {}  departureDateTime: {} arrivalDateTime : {} ",duration, departure, arrival, dateDeparture, dateArrival);
			return new ResponseEntity<>(oInterconnectionsResults.getInterconnection(), HttpStatus.OK);	
			
	}


}