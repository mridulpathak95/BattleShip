package application.Exception;

public class LocationHitException extends RuntimeException {
	 String exception;
	public LocationHitException(String exception) {
		 this.exception = exception;
	   } 
	   @Override
	   public String toString() { 
	      return ("Unmanaged exception handled : " + exception);
	   }
}
