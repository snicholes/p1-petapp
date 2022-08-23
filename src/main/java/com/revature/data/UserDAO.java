package com.revature.petapp.data;

import com.revature.petapp.models.User;

public interface UserDAO extends DataAccessObject<User> {
	public User findByUsername(String username);
}
