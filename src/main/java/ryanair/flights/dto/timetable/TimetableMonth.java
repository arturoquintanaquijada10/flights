package ryanair.flights.dto.timetable;

import java.util.Arrays;

public class TimetableMonth {		
	
	private String month;
	private Day[] days=new Day[0]; 
	
	private String code;
	private String message;
	
	
	public TimetableMonth(String month, Day day){
		setMonth( month);
		addDay(day);
	}
	
	public TimetableMonth(){}
	
	/**
	 * Add a Day object to the Days array if not exists, or update the Day if exist
	 * @param dayIn
	 */
	public void addDay(Day dayIn){
		int i=0;
		boolean bHasDay=false;

		for(Day iDay: getDays()){	

			if(iDay.getDay().equals(dayIn.getDay())){
				days[i]=dayIn;
				bHasDay=true;
				break;
			}			
			i++;
		}
		if(!bHasDay){
			days = Arrays.copyOf(days, days.length + 1);
			days[days.length-1]=dayIn;
		}
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Day[] getDays() {
		return days;
	}

	public void setDays(Day[] days) {
		this.days = days;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}