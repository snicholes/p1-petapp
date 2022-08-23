package com.revature.petapp.data;

import com.revature.petapp.data.orm.PetORM;
import com.revature.petapp.data.orm.SpeciesORM;
import com.revature.petapp.data.orm.StatusORM;
import com.revature.petapp.data.orm.UserORM;

public class DAOFactory {
	public static PetDAO getPetDAO() {
		return new PetORM();
	}

	public static UserDAO getUserDAO() {
		return new UserORM();
	}

	public static SpeciesDAO getSpeciesDAO() {
		return new SpeciesORM();
	}
	
	public static StatusDAO getStatusDAO() {
		return new StatusORM();
	}
}
