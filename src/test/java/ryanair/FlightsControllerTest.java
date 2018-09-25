package ryanair;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ryanair.flights.config.Application;
import ryanair.flights.config.MVCWebInitializer;
import ryanair.flights.config.RootConfig;
import ryanair.flights.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, Application.class, MVCWebInitializer.class})
@WebAppConfiguration
public class FlightsControllerTest {

	@Autowired
	Environment env;

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		this.mockMvc = builder.build();
	}

	private String departure="MAD";
	private String arrival="DUB";

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightsControllerTest.class);

	/**
	 * Test result OK for direct flights
	 * @throws Exception
	 */
	@Test
	public void test01_GetFlightsOK() throws Exception {
		LOGGER.info("*********************** test01_GetFlightsOK INIT ********************************");

		departure="MAD";
		arrival="DUB";

		ResultMatcher ok = MockMvcResultMatchers.status().isOk();

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(getUrlResquest(arrival, departure, getDateValid(2), getDateValid(3)));
		this.mockMvc.perform(builder)
		.andExpect(ok);	                   

		LOGGER.info("*********************** test01_GetFlightsOK FIN ********************************");		
	}



	/**
	 * Test error when the airport code do not exist
	 * @throws Exception
	 */
	@Test
	public void test02_GetFlightsAirportNotExist() throws Exception {
		LOGGER.info("*********************** test02_GetFlightsAirportNotExist INIT ********************************");

		departure="XXX";
		arrival="DUB";

		ResultMatcher badReq = MockMvcResultMatchers.status().isBadRequest();

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(getUrlResquest(arrival, departure, getDateValid(2), getDateValid(3)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);


		this.mockMvc.perform(builder)
		.andExpect(badReq)         
		.andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_ERROR))		
		.andExpect(jsonPath("$.message").value(env.getProperty("app.message.error.notfound")));		


		LOGGER.info("*********************** test02_GetFlightsAirportNotExist FIN ********************************");		
	}

	/**
	 * Test the error when the airport code format is not correct
	 * @throws Exception
	 */
	@Test
	public void test03_GetFlightsAirportNotValid() throws Exception {
		LOGGER.info("*********************** test03_GetFlightsAirportNotValid INIT ********************************");

		departure="MADR";
		arrival="DUB";

		ResultMatcher badReq = MockMvcResultMatchers.status().isBadRequest();

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(getUrlResquest(arrival, departure, getDateValid(2), getDateValid(3)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);


		this.mockMvc.perform(builder)
		.andExpect(badReq)         
		.andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_ERROR))
		.andExpect(jsonPath("$.message").value(env.getProperty("app.message.error.parameter.airportcode.invalid")));	

		LOGGER.info("*********************** test03_GetFlightsAirportNotValid FIN ********************************");		
	}


	/**
	 * Test the error when the date is not correct
	 * @throws Exception
	 */
	@Test
	public void test04_GetFlightsDateNotValid() throws Exception {
		LOGGER.info("*********************** test04_GetFlightsDateNotValid INIT ********************************");

		departure="MAD";
		arrival="DUB";
		String departureDate="2019-02-40T20:00";

		ResultMatcher badReq = MockMvcResultMatchers.status().isBadRequest();

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(getUrlResquest(arrival, departure, departureDate, getDateValid(3)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		this.mockMvc.perform(builder)
		.andExpect(badReq)         
		.andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_ERROR))
		.andExpect(jsonPath("$.message").value(env.getProperty("app.message.error.argument.exception")));	

		LOGGER.info("*********************** test04_GetFlightsDateNotValid FIN ********************************");		
	}

	/**
	 * Test result OK for interconnections flights
	 * @throws Exception
	 */
	@Test
	public void test05_GetFlightsWithConnectionOK() throws Exception {
		LOGGER.info("*********************** test05_GetFlightsWithConnectionOK INIT ********************************");

		departure="ATH";
		arrival="CTA";

		ResultMatcher ok = MockMvcResultMatchers.status().isOk();

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(getUrlResquest(arrival, departure, getDateValid(2), getDateValid(3)));
		this.mockMvc.perform(builder)
		.andExpect(ok);	                   

		LOGGER.info("*********************** test05_GetFlightsWithConnectionOK FIN ********************************");		
	}


	/**
	 * Get a valid actual date with the format specified
	 * @param monthIcrement
	 * @return
	 */
	private String getDateValid(int monthIcrement){

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.INPUT_DATE_FORMAT);
		sdf.setCalendar(cal);
		cal.add(Calendar.MONTH, monthIcrement);
		return  sdf.format(cal.getTime());
	}


	/**
	 * Get the URL to call the service 
	 * @param departure
	 * @param arrival
	 * @param dateDeparture
	 * @param dateArrival
	 * @return
	 */
	private String getUrlResquest(String departure, String arrival, String dateDeparture, String dateArrival){

		return "/interconnections?departure="+departure+"&arrival="+arrival+"&departureDateTime="+dateDeparture+"&arrivalDateTime="+dateArrival;

	}	


}
