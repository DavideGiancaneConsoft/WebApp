package com.webapp.exceptions;

public class DatabaseFailureException extends Exception{
	public DatabaseFailureException() {
		super();
	}
	
	public DatabaseFailureException(String message) {
		super(message);
	}

}
