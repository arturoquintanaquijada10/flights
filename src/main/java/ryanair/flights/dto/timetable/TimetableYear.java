package ryanair.flights.dto.timetable;

import java.util.Arrays;

public class TimetableYear {

	private String year;
	private TimetableMonth[] months=new TimetableMonth[0];
	private String departure;
	private String arrival;
	private String connection;

	public TimetableYear(String year, TimetableMonth[] months){
		this.year=year;
		this.months=months;		
	}

	public TimetableYear(){}
	
	
	/**
	 * Add a TimetableMonth object to the TimetableMonth array if not exists, or update the TimetableMonth if exist
	 * @param inTimetableMonth
	 */
	public void addTimetableMonth(TimetableMonth inTimetableMonth){
		int i=0;
		boolean bHasMonth=false;
		for(TimetableMonth iMonth : months){
			if(iMonth.getMonth().equals(inTimetableMonth.getMonth())){
				months[i]=inTimetableMonth;
				bHasMonth=true;
				break;
			}
			i++;
		}
		if(!bHasMonth){
			months = Arrays.copyOf(months, months.length + 1);
			months[months.length-1]=inTimetableMonth;
		}
	}
	

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}	

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public TimetableMonth[] getMonths() {
		return months;
	}

	public void setMonths(TimetableMonth[] months) {
		this.months = months;
	}


}