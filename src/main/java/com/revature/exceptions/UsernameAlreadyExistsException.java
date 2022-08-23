package com.revature.exceptions;

public class UsernameAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1712843686624302051L;

	public UsernameAlreadyExistsException() {
		super("The chosen username is taken by another user.");
	}
}
