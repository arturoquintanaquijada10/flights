package ryanair.flights.exceptions;

public class ResponseError {
	private String code;
	 private String message;
   
 
    public ResponseError( GenericException ge) {
        this.message = ge.getMessage();
        this.code = ge.getCode();
    }
    
    public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
    
	
}
