package ryanair.flights.dto.timetable;

import java.util.Arrays;

public class Timetable {	

	private TimetableYear[] timetables=new TimetableYear[0]; 
	public Timetable(){}
	
	
	
	public Timetable(String year, TimetableMonth[] aTimetablesMonth ){}
	
	public TimetableYear[] getTimetables() {
		return timetables;
	}
	public void setTimetables(TimetableYear[] timetables) {
		this.timetables = timetables;
	}	
	
	
	/**
	 * Add a TimetableYear object to the TimetableYear array if not exists, or update the TimetableYear if exist
	 * @param inTimetableYear
	 */
	public void addTimetableYear(TimetableYear inTimetableYear){
		int i=0;
		boolean bHasYear=false;		
		for(TimetableYear iTimetableYear : timetables){
			if(iTimetableYear.getYear().equals(inTimetableYear.getYear())){
				timetables[i]=inTimetableYear;
				bHasYear=true;
				break;
			}
			i++;
		}
		if(!bHasYear){			
			timetables = Arrays.copyOf(timetables, timetables.length + 1);
			timetables[timetables.length-1]=inTimetableYear;
		}
	}
	
	
	/**
	 * Add a TimetableMonth object to the TimetableMonth array if not exists, or update the TimetableMonth if exist
	 * @param year
	 * @param inTimetableMonth
	 */
	public void addTimetableMonth(String year, TimetableMonth inTimetableMonth){
		int i=0;
		boolean bExits=false;
		for(TimetableYear iTimetableYear : timetables){
			if(iTimetableYear.getYear().equals(year)){				
				iTimetableYear.addTimetableMonth( inTimetableMonth);					
				timetables[i]=iTimetableYear;
				bExits=true;
				break;
			}
			i++;
		}
		if(!bExits){
			TimetableMonth[] aTimetableMonth=new TimetableMonth[1];
			aTimetableMonth[0]=inTimetableMonth;
			TimetableYear oTimetableYear=new TimetableYear(year, aTimetableMonth);
			this.addTimetableYear(oTimetableYear);
		}
			
	}	
	

}