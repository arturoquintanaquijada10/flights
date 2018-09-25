package ryanair.flights.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ryanair.flights.dto.routes.Route;
import ryanair.flights.dto.routes.Routes;
import ryanair.flights.exceptions.GenericException;

@Service("routesService")
public class RoutesService implements IRoutesService{

	@Autowired
	Environment env;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoutesService.class);

	@Override
	public  Routes getRoutes( )  throws GenericException {

		try{
			String urlEndPointRoutes=env.getProperty("app.url.endpoint.routes");

			RestTemplate restTemplate = new RestTemplate();	
			long startTime = System.nanoTime();
			ResponseEntity<Route[]> responseRoutes=  restTemplate.getForEntity(urlEndPointRoutes, Route[].class);
			long endTime = System.nanoTime();
			long duration = (endTime - startTime)/1000000;
			
			Routes routes=new Routes();
			routes.setAlRoutes(responseRoutes.getBody());
			LOGGER.info("getRoutes() :        >>  getRoutes TIME SPEND: {} milliseconds",duration);
			LOGGER.info("getRoutes(): return routes length= {} ",routes.getAlRoutes().length);
			return routes;
		}catch(HttpClientErrorException  e){
			LOGGER.error("getRoutes(): Error getting routes, HttpClientErrorException message :{}  statusCode:{} ",e.getMessage(), e.getStatusCode());			
			throw new GenericException(e.getMessage(), e, e.getStatusCode());
		}catch ( Exception ex) {
			LOGGER.error("getRoutes(): Error getting routes, exception :{} ",ex.getMessage());
			throw new GenericException(env.getProperty("app.message.error.general"), ex);
		}
	}


}
