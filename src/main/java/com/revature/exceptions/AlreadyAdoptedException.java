package com.revature.exceptions;

public class AlreadyAdoptedException extends Exception {
	private static final long serialVersionUID = -5731127698387455412L;

	public AlreadyAdoptedException() {
		super("The pet you tried to adopt already has an adopted status.");
	}
}
