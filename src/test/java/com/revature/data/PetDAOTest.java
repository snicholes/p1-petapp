package com.revature.data;

import org.junit.jupiter.api.Test;

import com.revature.beans.Pet;
import com.revature.data.postgres.PetPostgres;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

public class PetDAOTest {
	private PetDAO petDao = new PetPostgres();
	
	// made by synergy w/ small edit by sierra
	@Test // Test 
	public void getByIdWhenIdExists() {
		// setup
		int idInput = 1;
		// call the method we're testing
		Pet idOutput = petDao.getById(idInput);
		// assert that it did what we expected
		assertEquals(1, idOutput.getId());
	}
	
	// made by amplifire
	@Test
	public void getByIdWhenIdDoesNotExists() {
		int idInput = -1;
		Pet petOutput = petDao.getById(idInput);
		assertNull(petOutput);
	}
	
	// made by amplifire with edits by sierra
	@Test
	public void getAll() {
		Set<Pet> givenOutput = petDao.getAll();
		assertNotNull(givenOutput);
	}
	
	@Test
	public void addNewPet() {
		Pet newPet = new Pet();
		System.out.println(newPet);
		
		int generatedId = petDao.create(newPet);
		
		assertNotEquals(0, generatedId);
		System.out.println(generatedId);
	}
}
