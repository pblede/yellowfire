package za.co.yellowfire.event.exception;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

@Singleton
public class ExceptionHandler {
	
	public void handle(@Observes @Handles Throwable e) {
		e.printStackTrace();
	}
}
