package com.revature.petapp.data;

import com.revature.petapp.models.Status;

public interface StatusDAO extends DataAccessObject<Status> {
	public Status findByName(String name);
}
