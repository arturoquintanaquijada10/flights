package ryanair.flights.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import ryanair.flights.dto.interconnections.Interconnection;
import ryanair.flights.dto.interconnections.InterconnectionsResults;
import ryanair.flights.dto.interconnections.Leg;
import ryanair.flights.dto.routes.Route;
import ryanair.flights.dto.routes.Routes;
import ryanair.flights.dto.service.InputData;
import ryanair.flights.dto.timetable.Day;
import ryanair.flights.dto.timetable.Flight;
import ryanair.flights.dto.timetable.Timetable;
import ryanair.flights.dto.timetable.TimetableMonth;
import ryanair.flights.dto.timetable.TimetableYear;
import ryanair.flights.exceptions.GenericException;
import ryanair.flights.services.IRoutesService;
import ryanair.flights.services.ITimetableService;
import ryanair.flights.util.Constants;


/**
 * @author Arturo Quintana
 *
 */
@Component
@Qualifier("manageFlights")
public class FlightsManager implements IFlightsManager{

	@Autowired
	Environment env;	


	@Autowired
	ITimetableService oTimetableService;

	@Autowired
	IRoutesService oRoutesService;

	@Autowired
	@Qualifier("manageFlights")
	IFlightsManager oFlightsManager;


	private static final Logger LOGGER = LoggerFactory.getLogger(FlightsManager.class);



	@Override
	public InterconnectionsResults getAllValidTimetables( String departure,  String arrival, Date dateDeparture, Date dateArrival) throws GenericException {


		InputData oInputData=getInputData( departure,   arrival,  dateDeparture,  dateArrival);

		validateAirportCode(oInputData);

		LOGGER.info("getAllValidTimetables(): Init : oInputData= {} ",oInputData);

		List<Route> routesValid=getValidRoutes(oInputData);

		InterconnectionsResults oInterconnectionsResults=new InterconnectionsResults();

		for(Route route: routesValid){

			if(route.getConnectingAirport()!=null){
				manageTimetableInterconnection(oInterconnectionsResults , oInputData, route);									
			}else{				
				addDirect( oInterconnectionsResults ,  getTimetableFiltered( oInputData), oInputData.getDeparture(), oInputData.getArrival());				
			}
		}
		if(oInterconnectionsResults.getInterconnection().length>0){
			return oInterconnectionsResults;
		}else{
			throw new GenericException(env.getProperty("app.message.error.notfound"), HttpStatus.NOT_FOUND);
		}	
		
	}	


	/**
	 * Generate InputData object with params required
	 * @param departure
	 * @param arrival
	 * @param dateDeparture
	 * @param dateArrival
	 * @return
	 */
	private InputData getInputData(String departure,  String arrival, Date dateDeparture, Date dateArrival){

		Calendar calDep=Calendar.getInstance();
		calDep.clear();
		calDep.setTime(dateDeparture);
		
		Calendar calArr=Calendar.getInstance();
		calArr.clear();
		calArr.setTime(dateArrival);		

		return new InputData(departure.toUpperCase(),  arrival.toUpperCase(),  calDep, calArr);		
	}



	/**
	 * Gets all valid routes with the Routes service result datas 
	 * @param oInputData
	 * @return
	 * @throws GenericException
	 */
	private List<Route> getValidRoutes(InputData oInputData ) throws GenericException {

		Routes routes=oRoutesService.getRoutes();

		LOGGER.info("getValidRoutes(): init routes length= {} ", String.valueOf(routes.getAlRoutes().length));	

		List<Route> routesValid=	Stream.of(routes.getAlRoutes()).filter(route -> route.getAirportTo().equalsIgnoreCase(oInputData.getArrival())).filter(route -> route.getAirportFrom().equalsIgnoreCase(oInputData.getDeparture())).collect(Collectors.toList());

		if(routesValid.isEmpty()){
			throw new GenericException(env.getProperty("app.message.error.notfound") , HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("getValidRoutes(): return routesValid size= {} ", String.valueOf(routesValid.size()));
		return routesValid;
	}
	

	/**
	 * Extract the hour from the time string
	 * @param time
	 * @return
	 */
	private String getHour(String time){
		if(time!=null && time.contains(":")){			
			return (time.split(":"))[0];
		}
		return "";
	}



	/**
	 * Extract the minutes from the time string
	 * @param time
	 * @return
	 */
	private String getMinutes(String time){
		if(time!=null && time.contains(":")){			
			return (time.split(":"))[1];
		}
		return "";
	}


	/**
	 * Get the departure date as String
	 * @param year
	 * @param month
	 * @param day
	 * @param oFlight
	 * @return
	 */
	private  String getStringDateDeparture(String year, String month, String day, Flight oFlight){
		return getCalendarString( year,  month,  day,  getHour(oFlight.getDepartureTime()),  getMinutes(oFlight.getDepartureTime()));			
	}


	/**
	 * Get the arrival date as String
	 * @param year
	 * @param month
	 * @param day
	 * @param oFlight
	 * @return
	 */
	private  String getStringDateArrival(String year, String month, String day, Flight oFlight){
		return getCalendarString( year,  month,  day,  getHour(oFlight.getArrivalTime()),  getMinutes(oFlight.getArrivalTime()));
	}


	/**
	 * Get date as String with the format "yyyy-MM-dd'T'HH:mm"
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	private String getCalendarString(String year, String month, String day, String hour, String minute){
		Calendar cal = getCalendar( year,  month,  day,  hour,  minute);
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.INPUT_DATE_FORMAT);	
		sdf.setCalendar(cal);
		return  sdf.format(cal.getTime());
	}	
	
	
	/**
	 * Get the date as Calendar
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	private Calendar getCalendar(String year, String month, String day, String hour, String minute){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month));
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		cal.set(Calendar.MINUTE, Integer.parseInt(minute));
		return cal;		
	}



	/**
	 * Add to InterconnectionsResults object the direct flights
	 * @param oInterconnectionsResults
	 * @param timetable
	 * @param departure
	 * @param arrival
	 */	
	private  void  addDirect(InterconnectionsResults oInterconnectionsResults , Timetable timetable, String departure, String arrival) {

		LOGGER.info("addDirect(): init  departure= {}, arrival={} ", departure, arrival);

		for(TimetableYear iTimetableYear : timetable.getTimetables()){
			String year=iTimetableYear.getYear();

			for(TimetableMonth iTimetableMonth : iTimetableYear.getMonths()){
				String month=iTimetableMonth.getMonth();

				for( Day oDay : iTimetableMonth.getDays()){
					String day=oDay.getDay();

					for(Flight iFlight : oDay.getFlights()){
						Interconnection oInterconnection=new Interconnection(0,new Leg( departure,  arrival, getStringDateDeparture(year, month, day, iFlight),  getStringDateArrival(year, month, day, iFlight) ));
						oInterconnectionsResults.addInterconnection(oInterconnection);	
					}
				}
			}			
		}	
	}

	/**
	 * Get the Departure Date as Calendar
	 * @param year
	 * @param month
	 * @param day
	 * @param oFlight
	 * @return
	 */
	private  Calendar getCalendarDeparture(String year, String month, String day, Flight oFlight) {		
		return getCalendar( year,  month,  day,  getHour(oFlight.getDepartureTime()),  getMinutes(oFlight.getDepartureTime()));	
	}


	/**
	 * Get the Arrival Date as Calendar
	 * @param year
	 * @param month
	 * @param day
	 * @param oFlight
	 * @return
	 */
	private  Calendar getCalendarArrival(String year, String month, String day, Flight oFlight) {
		return getCalendar( year,  month,  day,  getHour(oFlight.getArrivalTime()),  getMinutes(oFlight.getArrivalTime()));
	}


	/**
	 * Validate the airport code format for arrival and departure airports
	 * @param oInputData
	 * @throws GenericException
	 */
	private void validateAirportCode(InputData oInputData) throws GenericException{
		String regex = "^[A-Za-z]{3}$";	
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(oInputData.getDeparture());
		if(!matcher.matches()){
			throw new GenericException(env.getProperty("app.message.error.parameter.airportcode.invalid"), HttpStatus.BAD_REQUEST);
		}
		matcher = pattern.matcher(oInputData.getArrival());

		if(!matcher.matches()){
			throw new GenericException(env.getProperty("app.message.error.parameter.airportcode.invalid"), HttpStatus.BAD_REQUEST);
		}
	}


	/**
	 * Add to InterconnectionsResults object the interconnection flights
	 * @param oInterconnectionsResults
	 * @param oInputData
	 * @param route
	 * @throws GenericException
	 */
	private  void manageTimetableInterconnection(InterconnectionsResults oInterconnectionsResults , InputData oInputData, Route route ) throws GenericException {

		LOGGER.info("manageTimetableInterconnection(): init oInputData= {} ", oInputData);

		Timetable timetable1=getTimetableFiltered(new InputData(oInputData.getDeparture(), route.getConnectingAirport(),  oInputData.getDepartureDateTime(), oInputData.getArrivalDateTime()));
		Timetable timetable2=getTimetableFiltered(new InputData(route.getConnectingAirport(), oInputData.getArrival(), oInputData.getDepartureDateTime(), oInputData.getArrivalDateTime()));

		for(TimetableYear otimetable1 : timetable1.getTimetables()){
			String year=otimetable1.getYear();
			for(TimetableMonth TimetableMonth1 : otimetable1.getMonths()){
				String month=TimetableMonth1.getMonth();
				for(Day oDay1 : TimetableMonth1.getDays()){
					String day=oDay1.getDay();
					for(Flight oFlight1 :oDay1.getFlights()){

						findInterconnections(oInterconnectionsResults, oInputData, route, timetable2, year,	month, day, oFlight1);	
					}
				}
			}			
		}

	}



	/**
	 * Add to InterconnectionsResults object the interconnection flights
	 * @param oInterconnectionsResults
	 * @param oInputData
	 * @param route
	 * @param timetable2
	 * @param year
	 * @param month
	 * @param day
	 * @param oFlight1
	 */
	private  void findInterconnections(InterconnectionsResults oInterconnectionsResults, InputData oInputData, 	Route route, Timetable timetable2, String year, String month, String day, Flight oFlight1) {

		LOGGER.info("findInterconnections(): init departure= {}, arrival={} , year= {}, month={}, day={}", oInputData.getDeparture(), oInputData.getArrival(), year, month, day);

		for(TimetableYear otimetable2 : timetable2.getTimetables()){
			String year2=otimetable2.getYear();
			for(TimetableMonth TimetableMonth2 : otimetable2.getMonths()){
				String month2=TimetableMonth2.getMonth();
				for(Day oDay2 : TimetableMonth2.getDays()){
					String day2=oDay2.getDay();
					for(Flight oFlight2 :oDay2.getFlights()){									

						Calendar arrival1 = getCalendarArrival(year, month, day, oFlight1);
						Calendar departure2 = getCalendarDeparture(year2, month2, day2, oFlight2);										

						arrival1.add(Calendar.HOUR_OF_DAY, Integer.parseInt(env.getProperty("app.hour.connection")));

						if(arrival1.before(departure2)){

							getStringDateDeparture(year, month, day, oFlight1);
							Leg oLeg1=new Leg( oInputData.getDeparture(),  route.getConnectingAirport(), getStringDateDeparture(year, month, day, oFlight1) ,  getStringDateArrival(year, month, day, oFlight1) );
							Leg oLeg2=new Leg( route.getConnectingAirport(),  oInputData.getArrival(), getStringDateDeparture(year2, month2, day2, oFlight2)  , getStringDateArrival(year2, month2, day2, oFlight2) );

							Interconnection oInterconnection=new Interconnection();
							oInterconnection.setStops(1);
							oInterconnection.addLeg(oLeg1);
							oInterconnection.addLeg(oLeg2);
							oInterconnectionsResults.addInterconnection(oInterconnection);				

						}		
					}
				}
			}
		}
	}



	
	/**
	 * Add to the Timetable object all timetables filtered by data in InputData object
	 * @param oInputData
	 * @return
	 * @throws GenericException
	 */
	private Timetable getTimetableFiltered(InputData oInputData) throws GenericException{

		try{
			LOGGER.info("getTimetableFiltered(): init oInputData= {} ", oInputData);

			Map<String, String> vars = new HashMap<>();
			vars.put("departure", oInputData.getDeparture().toUpperCase());
			vars.put("arrival", oInputData.getArrival().toUpperCase());

			Timetable oTimetable=new Timetable();

			if(isDifferentYearMonth(oInputData.getDepartureDateTime() ,oInputData.getArrivalDateTime()) ){

				int diffMonth = getMonthsBetweenDates(oInputData.getDepartureDateTime(), oInputData.getArrivalDateTime());

				Calendar calDepAux=oInputData.getDepartureDateTime();

				Calendar calDepartureAux=(Calendar)calDepAux.clone();				
				for(int i=0;i<diffMonth;i++){
					if(calDepartureAux.compareTo(oInputData.getArrivalDateTime())<0 && isDifferentYearMonth(calDepartureAux ,oInputData.getArrivalDateTime()) ){
						getTimeTableCalendarFiltered(calDepartureAux, vars, oTimetable, oInputData.getDepartureDateTime() ,oInputData.getArrivalDateTime());							
					}		
					calDepartureAux.add(Calendar.MONTH, 1);
				}
				getTimeTableCalendarFiltered(oInputData.getArrivalDateTime(), vars, oTimetable , oInputData.getDepartureDateTime(),  oInputData.getArrivalDateTime());					

			}else{
				getTimeTableCalendarFiltered(oInputData.getDepartureDateTime(), vars, oTimetable ,oInputData.getDepartureDateTime() ,oInputData.getArrivalDateTime());
			}
			return oTimetable;	

		}catch ( GenericException ex) {
			throw ex;		
		}catch ( Exception ex) {
			LOGGER.error("getTimetableFiltered(): Error getting timetable filtered, Exception :{} ",ex.getMessage());
			throw new GenericException(env.getProperty("app.message.error.general"), ex, HttpStatus.NOT_FOUND);
		}

	}

	
	/**
	 * Get the months between two Calendar dates
	 * @param calDeparture
	 * @param calArrival
	 * @return
	 */
	private int getMonthsBetweenDates(Calendar calDeparture, Calendar calArrival) {
		int diffYear = calArrival.get(Calendar.YEAR) - calDeparture.get(Calendar.YEAR);
		return diffYear * 12 + calArrival.get(Calendar.MONTH) - calDeparture.get(Calendar.MONTH);
	}

	
	/**
	 * Get if it different Year and month for two calendar dates
	 * @param cal1
	 * @param cal2
	 * @return
	 */
	private boolean isDifferentYearMonth(Calendar cal1, Calendar cal2){
		return (cal1.get(Calendar.YEAR)!=cal2.get(Calendar.YEAR)) || (cal1.get(Calendar.MONTH)!=cal2.get(Calendar.MONTH));	
	}

	
	/**
	 * Add to the Timetable object all timetables filtered by year an d month 
	 * @param cal
	 * @param vars
	 * @param oTimetable
	 * @param calDeparture
	 * @param calArrival
	 * @throws GenericException
	 */
	private void getTimeTableCalendarFiltered(Calendar cal,  Map<String, String> vars,	Timetable oTimetable, Calendar calDeparture, Calendar calArrival) throws GenericException {

		LOGGER.info("getTimeTableCalendarFiltered(): init vars= {} ",vars);
		try{
			
			vars.put("year", String.valueOf(cal.get(Calendar.YEAR)));	
			vars.put("month",  String.valueOf((cal.get(Calendar.MONTH))+1));

			ResponseEntity<TimetableMonth> responseTimetable=oTimetableService.getTimetable( vars);

			TimetableMonth oTimetableMonth=responseTimetable.getBody();

			TimetableMonth oTimetableMonthTemp=new TimetableMonth();
			oTimetableMonthTemp.setMonth(String.valueOf((cal.get(Calendar.MONTH))+1));

			for(Day oDay:oTimetableMonth.getDays()){
				Day dayTemp=new Day();
				dayTemp.setDay(oDay.getDay());

				int day=Integer.parseInt(oDay.getDay());
				for(Flight oFlight: oDay.getFlights()){

					int hourDep=Integer.parseInt(getHour(oFlight.getDepartureTime()));
					int minutesDep=Integer.parseInt(getMinutes(oFlight.getDepartureTime()));

					int hourArr=Integer.parseInt(getHour(oFlight.getArrivalTime()));
					int minutesArr=Integer.parseInt(getMinutes(oFlight.getArrivalTime()));

					Calendar calDep = Calendar.getInstance();
					calDep.clear();
					calDep.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), day, hourDep, minutesDep, 0);

					Calendar calArr = Calendar.getInstance();
					calArr.clear();
					calArr.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), day, hourArr, minutesArr, 0);

					if(calDep.compareTo(calDeparture)>=0  && calArr.compareTo(calArrival)<=0 ){

						dayTemp.addFlight(oFlight);
						oTimetableMonthTemp.addDay(dayTemp);			
						oTimetable.addTimetableMonth(vars.get("year"), oTimetableMonthTemp);						
					}
				}
			}		

		}catch ( GenericException ex) {
			LOGGER.error("getTimeTableCalendarFiltered(): Error getting timetable filtered, Exception :{} ",ex.getMessage());			
		}

	}

}
