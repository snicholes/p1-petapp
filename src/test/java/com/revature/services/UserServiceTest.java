package com.revature.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.beans.Person;
import com.revature.beans.Pet;
import com.revature.beans.Status;
import com.revature.data.PersonDAO;
import com.revature.data.PetDAO;
import com.revature.data.StatusDAO;
import com.revature.exceptions.AlreadyAdoptedException;
import com.revature.exceptions.IncorrectCredentialsException;
import com.revature.exceptions.UsernameAlreadyExistsException;

// tell JUnit that we're using Mockito
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	// tell Mockito which classes/interfaces that we'll be mocking
	@Mock
	private PetDAO petDao;
	
	@Mock
	private PersonDAO personDao;
	
	@Mock
	private StatusDAO statusDao;
	
	// tell Mockito to override the regular DAOs with our mock DAOs
	@InjectMocks
	private UserService userServ = new UserServiceImpl();
	
	private static Set<Pet> mockAvailablePets;
	
	@BeforeAll
	public static void mockAvailablePetsSetup() {
		mockAvailablePets = new HashSet<>();
		
		for (int i=1; i<=5; i++) {
			Pet pet = new Pet();
			pet.setId(i);
			if (i<3)
				pet.setSpecies("cat");
			mockAvailablePets.add(pet);
		}
	}
	
	@Test
	public void logInSuccessfully() throws IncorrectCredentialsException {
		// input setup
		String username="qwertyuiop";
		String password="pass";
		
		// set up the mocking
		Person mockPerson = new Person();
		mockPerson.setUsername(username);
		mockPerson.setPassword(password);
		when(personDao.getByUsername(username)).thenReturn(mockPerson);
		
		// call the method we're testing
		Person actualPerson = userServ.logIn(username, password);
		
		// assert the expected behavior/output
		assertEquals(mockPerson,actualPerson);
	}
	
	@Test
	public void logInIncorrectPassword() {
		String username="qwertyuiop";
		String password="12345";
		
		Person mockPerson = new Person();
		mockPerson.setUsername(username);
		mockPerson.setPassword("pass");
		when(personDao.getByUsername(username)).thenReturn(mockPerson);
		
		assertThrows(IncorrectCredentialsException.class, () -> {
			userServ.logIn(username, password);
		});
	}
	
	@Test
	public void logInUsernameDoesNotExist() {
		String username="asdfghjkl";
		String password="pass";
		
		when(personDao.getByUsername(username)).thenReturn(null);
		
		assertThrows(IncorrectCredentialsException.class, () -> {
			userServ.logIn(username, password);
		});
	}
	
	@Test
	public void registerPersonSuccessfully() throws UsernameAlreadyExistsException {
		Person person = new Person();
		
		when(personDao.create(person)).thenReturn(10);
		
		Person actualPerson = userServ.register(person);
		assertEquals(10, actualPerson.getId());
	}
	
	@Test
	public void registerPersonSomethingWrong() throws UsernameAlreadyExistsException {
		Person person = new Person();
		when(personDao.create(person)).thenReturn(0);
		Person actualPerson = userServ.register(person);
		assertNull(actualPerson);
	}
	
	@Test
	public void registerPersonUsernameAlreadyExists() {
		Person person = new Person();
		when(personDao.create(person)).thenReturn(-1);

		assertThrows(UsernameAlreadyExistsException.class, () -> {
			userServ.register(person);
		});
	}
	
	@Test
	public void searchBySpeciesExists() {
		String species = "cat";
		
		when(petDao.getByStatus("Available")).thenReturn(mockAvailablePets);
		
		Set<Pet> actualCats = userServ.searchAvailablePetsBySpecies(species);
		boolean onlyCats = true;
		for (Pet pet : actualCats) {
			if (!pet.getSpecies().equals(species))
				onlyCats = false;
		}
		
		assertTrue(onlyCats);
	}
	
	@Test
	public void searchBySpeciesDoesNotExist() {
		String species = "qwertyuiop";
		
		when(petDao.getByStatus("Available")).thenReturn(mockAvailablePets);
		
		Set<Pet> actualPets = userServ.searchAvailablePetsBySpecies(species);
		assertTrue(actualPets.isEmpty());
	}
	
	@Test
	public void adoptPetSuccessfully() throws AlreadyAdoptedException {
		int petId = 1;
		Person person = new Person();
		
		Pet mockPet = new Pet();
		mockPet.setId(1);
		when(petDao.getById(petId)).thenReturn(mockPet);
		
		Status adoptedStatus = new Status();
		adoptedStatus.setId(2);
		adoptedStatus.setName("Adopted");
		when(statusDao.getByName("Adopted")).thenReturn(adoptedStatus);
		
		// mock will do nothing when "update" gets called with any pet or person
		doNothing().when(petDao).update(Mockito.any(Pet.class));
		doNothing().when(personDao).update(Mockito.any(Person.class));
		
		Person newPerson = userServ.adoptPet(petId, person);
		
		// make sure that the method returned a person that has their
		// newly adopted pet there, and that pet has the correct status
		mockPet.setStatus(adoptedStatus);
		assertTrue(newPerson.getPets().contains(mockPet));
	}
	
	@Test
	public void adoptPetAlreadyAdopted() {
		int petId = 1;
		Person person = new Person();
		
		Pet mockPet = new Pet();
		mockPet.setId(1);
		
		Status adoptedStatus = new Status();
		adoptedStatus.setId(2);
		adoptedStatus.setName("Adopted");
		mockPet.setStatus(adoptedStatus);
		when(petDao.getById(petId)).thenReturn(mockPet);
		
		assertThrows(AlreadyAdoptedException.class, () -> {
			userServ.adoptPet(petId, person);
		});
		
		// these Mockito methods will verify that neither of these
		// update methods got called
		verify(petDao, times(0)).update(Mockito.any(Pet.class));
		verify(personDao, times(0)).update(Mockito.any(Person.class));
	}
	
	@Test
	public void updateSuccessfully() {
		Person mockPerson = new Person();
		mockPerson.setId(1);
		
		doNothing().when(personDao).update(Mockito.any(Person.class));
		when(personDao.getById(1)).thenReturn(mockPerson);
		
		Person person = new Person();
		person.setId(1);
		person.setUsername("qwertyuiop");
		Person updatedPerson = userServ.updateUser(person);
		assertNotEquals(person, updatedPerson);
	}
	
	@Test
	public void updateSomethingWrong() {
		Person mockPerson = new Person();
		mockPerson.setId(1);
		
		doNothing().when(personDao).update(Mockito.any(Person.class));
		when(personDao.getById(1)).thenReturn(mockPerson);
		
		Person person = new Person();
		person.setId(1);
		person.setUsername("qwertyuiop");
		Person updatedPerson = userServ.updateUser(person);
		assertNotEquals(person, updatedPerson);
	}
	
	@Test
	public void viewAvailablePets() {
		when(petDao.getByStatus("Available")).thenReturn(mockAvailablePets);
		
		Set<Pet> actualPets = userServ.viewAvailablePets();
		
		assertEquals(mockAvailablePets, actualPets);
	}
}
