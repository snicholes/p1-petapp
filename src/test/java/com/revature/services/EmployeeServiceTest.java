package com.revature.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.beans.Pet;
import com.revature.data.PetDAO;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	@Mock
	private PetDAO petDao;
	
	@InjectMocks
	private EmployeeService empServ = new EmployeeServiceImpl();
	
	@Test
	public void addNewPetSuccessfully() {
		Pet pet = new Pet();
		
		when(petDao.create(pet)).thenReturn(10);
		
		int newId = empServ.addNewPet(pet);
		
		assertNotEquals(0, newId);
	}
	
	@Test
	public void addNewPetSomethingWrong() {
		Pet pet = new Pet();
		
		when(petDao.create(pet)).thenReturn(0);
		
		int newId = empServ.addNewPet(pet);
		
		assertEquals(0,newId);
	}
	
	@Test
	public void editPetSuccessfully() {
		Pet editedPet = new Pet();
		editedPet.setId(2);
		editedPet.setAge(10);
		
		when(petDao.getById(2)).thenReturn(editedPet);
		doNothing().when(petDao).update(Mockito.any(Pet.class));
		
		Pet actualPet = empServ.editPet(editedPet);
		
		assertEquals(editedPet, actualPet);
	}
	
	@Test
	public void editPetSomethingWrong() {
		Pet mockPet = new Pet();
		mockPet.setId(2);
		
		when(petDao.getById(2)).thenReturn(mockPet);
		doNothing().when(petDao).update(Mockito.any(Pet.class));
		
		Pet editedPet = new Pet();
		editedPet.setId(2);
		editedPet.setAge(10);
		
		Pet actualPet = empServ.editPet(editedPet);
		
		assertNotEquals(editedPet, actualPet);
	}
	
	@Test
	public void editPetDoesNotExist() {
		when(petDao.getById(2)).thenReturn(null);
		
		Pet editedPet = new Pet();
		editedPet.setId(2);
		editedPet.setAge(10);
		
		Pet actualPet = empServ.editPet(editedPet);
		
		assertNull(actualPet);
		verify(petDao, times(0)).update(Mockito.any(Pet.class));
	}
	
	@Test
	public void getByIdPetExists() {
		Pet pet = new Pet();
		pet.setId(2);
		
		when(petDao.getById(2)).thenReturn(pet);
		
		Pet actualPet = empServ.getPetById(2);
		assertEquals(pet, actualPet);
	}
	
	@Test
	public void getByIdPetDoesNotExist() {
		when(petDao.getById(2)).thenReturn(null);
		
		Pet actualPet = empServ.getPetById(2);
		assertNull(actualPet);
	}
}
