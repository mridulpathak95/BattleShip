package application.Exception;

public class NegativeScore extends RuntimeException{
	 String exception;
		public NegativeScore(String exception) {
			 this.exception = exception;
		   } 
		   @Override
		   public String toString() { 
		      return ("Unmanaged Exception Handled : " + exception);
		   }
	}

