package com.revature.data;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.revature.beans.Person;
import com.revature.data.postgres.PersonPostgres;

// this imports the static methods from Assertions so that
// we can type "assertEquals" rather than "Assertions.assertEquals"
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

public class PersonDAOTest {
	private PersonDAO personDao = new PersonPostgres();//DAOFactory.getPersonDAO();
	
	// made by vanquish
	@Test
	public void getAllNotNull() {
		Set<Person> actual = personDao.getAll();
		assertNotEquals(null, actual);
	}
	
	// made by vanquish w/ some edits by sierra
	@Test
	public void getValidPersonById()
	{
		String expectedUsername = "snicholes";
		Person actual = personDao.getById(1);
		assertEquals(expectedUsername, actual.getUsername());
	}

	// made by alchemy
	@Test
	public void testUpdate() {
		Person personUp = personDao.getById(1);
		personUp.setFullName("ricky");
		personDao.update(personUp);
		assertEquals("ricky",personDao.getById(1).getFullName());	
	}
	
	// made by alchemy with edit by sierra
	@Test
	public void testGetIDNoID() {
		Person personOutput= personDao.getById(10000);
		assertNull(personOutput);
	}
	
	// made by synergy
	@Test
	@Disabled
	public void createTest() {
		Person create = new Person();
		create.setUsername("test4");
		assertNotEquals(0, personDao.create(create));
		// use person dao to test that create method is not null
	}
	
	@Test
	public void getByUsernameWhenUsernameExists() {
		// setup
		String usernameInput = "snicholes";
		// call the method we're testing
		Person personOutput = personDao.getByUsername(usernameInput);
		// assert that it did what we expected
		assertEquals("snicholes", personOutput.getUsername());
	}
	
	@Test
	public void getByUsernameButUsernameDoesNotExist() {
		String usernameInput = "qwertyuiop";
		Person personOutput = personDao.getByUsername(usernameInput);
		assertNull(personOutput); // assertEquals(null, personOutput)
	}
}
