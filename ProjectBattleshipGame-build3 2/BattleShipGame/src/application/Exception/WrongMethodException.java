package application.Exception;

public class WrongMethodException extends Exception {
	 String exception;
		public WrongMethodException(String exception) {
			 this.exception = exception;
		   } 
		   @Override
		   public String toString() { 
		      return ("Managed exception handled : " + exception);
		   }
}
