package ryanair.flights.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ryanair.flights.dto.timetable.TimetableMonth;
import ryanair.flights.exceptions.GenericException;

@Service("timetableService")
public class TimetableService implements ITimetableService {

	@Autowired
	Environment env;

	private static final Logger LOGGER = LoggerFactory.getLogger(TimetableService.class);

	@Override
	public ResponseEntity<TimetableMonth> getTimetable( Map<String, String> vars) throws GenericException {

		String urlEndPointTimetable=env.getProperty("app.url.endpoint.timetable");
		LOGGER.info("getRoutes() :        >>  URL service : {} ", urlEndPointTimetable);	
		RestTemplate restTemplate = new RestTemplate();	
		try {
			long startTime = System.nanoTime();
			ResponseEntity<TimetableMonth> oResponseEntity= restTemplate.getForEntity(urlEndPointTimetable, TimetableMonth.class, vars);
			long endTime = System.nanoTime();
			long duration = (endTime - startTime)/1000000;			
			LOGGER.info("getRoutes() :        >>  getTimetable TIME SPEND: {} milliseconds",duration);			
			return oResponseEntity;

		}catch(HttpClientErrorException  e){
			LOGGER.error("getTimetable(): Error getting timetable, HttpClientErrorException message :{}  statusCode:{} ",e.getMessage(), e.getStatusCode());			
			throw new GenericException(e.getMessage(), e, e.getStatusCode());
		}catch(Exception  e){
			LOGGER.error("getTimetable(): Error getting timetable, Exception :{} ",e.getMessage());
			throw new GenericException(env.getProperty("app.message.error.notfound"), e);
		}
	}


}
