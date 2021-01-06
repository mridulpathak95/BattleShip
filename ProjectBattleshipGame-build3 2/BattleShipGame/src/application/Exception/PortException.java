package application.Exception;

public class PortException extends Exception{
	 String exception;
		public PortException(String exception) {
			 this.exception = exception;
		   } 
		   @Override
		   public String toString() { 
		      return ("Managed exception handled : " + exception);
		   }
}
