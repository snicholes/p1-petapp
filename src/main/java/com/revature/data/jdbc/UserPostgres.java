package com.revature.petapp.data.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.revature.petapp.data.UserDAO;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Species;
import com.revature.petapp.models.Status;
import com.revature.petapp.models.User;
import com.revature.petapp.utils.ConnectionUtil;

public class UserPostgres implements UserDAO {
	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();

	@Override
	public User create(User user) throws SQLException {
		int generatedId = 0;
		String sql = "insert into person (id,full_name,username,passwd,role_id) "
				+ "values (default, ?, ?, ?, ?)";
		String[] keys = {"id"};
		
		try (Connection conn = connUtil.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql, keys)) {
			conn.setAutoCommit(false);
			
			pStmt.setString(1, user.getFullName()); // question mark index starts at 1
			pStmt.setString(2, user.getUsername());
			pStmt.setString(3, user.getPassword());
			pStmt.setInt(4, user.getRole().getId());
			
			pStmt.executeUpdate();
			ResultSet resultSet = pStmt.getGeneratedKeys();
			
			if (resultSet.next()) { // "next" goes to the next row in the result set (or the first row)
				generatedId = resultSet.getInt("id");
				user.setId(generatedId);
				conn.commit(); // running the TCL commit statement
			} else {
				conn.rollback();
			}
			
		} catch (SQLException e) {
			if (e.getMessage().contains("unique constraint")) {
				throw e;
			}
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public User findById(int id) {
		User user = null;
		// set up the SQL statement that we want to execute
		String sql = "select person.id,full_name,username,passwd,"
				+ " role_id,user_role.name as role_name"
				+ " from person join user_role"
				+ " on person.role_id=user_role.id where person.id=?";
		
		// try-with-resources: sets up closing for closeable resources
		try (Connection conn = connUtil.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {

			// set up that statement with the database
			// preparedstatement is pre-processed to prevent sql injection
			pStmt.setInt(1, id);
			
			// execute the statement
			ResultSet resultSet = pStmt.executeQuery();
			
			// process the result set
			if (resultSet.next()) {
				user = new User();
				user.setId(id);
				user.setFullName(resultSet.getString("full_name"));
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("passwd"));
				Role role = new Role();
				role.setId(resultSet.getInt("role_id"));
				role.setName(resultSet.getString("role_name"));
				user.setRole(role);

				user.setPets(getPetsByOwner(conn, person.getId()));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		String sql = "select person.id,full_name,username,passwd,"
				+ " role_id,user_role.name as role_name"
				+ " from person join user_role on person.role_id=user_role.id";
		
		try (Connection conn = connUtil.getConnection();
				Statement stmt = conn.createStatement()) {

			// execute the statement
			ResultSet resultSet = stmt.executeQuery(sql);

			// process the result set
			while (resultSet.next()) {
				User user = new User();
				// pull the data from each row in the result set
				// and put it into the java object so that we can use it here
				user.setId(resultSet.getInt("id"));
				user.setFullName(resultSet.getString("full_name"));
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("passwd"));
				Role role = new Role();
				role.setId(resultSet.getInt("role_id"));
				role.setName(resultSet.getString("role_name"));
				user.setRole(role);
				user.setPets(getPetsByUser(conn, user.getId()));

				users.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public void update(User t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(User t) {
		// TODO Auto-generated method stub

	}

	@Override
	public User findByUsername(String username) {
		User user = null;
		String sql = "select person.id,full_name,username,passwd,"
				+ " role_id,user_role.name as role_name"
				+ " from person join user_role"
				+ " on person.role_id=user_role.id where username=?";
		
		try (Connection conn = connUtil.getConnection();
			PreparedStatement pStmt = conn.prepareStatement(sql)) {

			pStmt.setString(1, username);
			
			ResultSet resultSet = pStmt.executeQuery();
			
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getInt("id"));
				user.setFullName(resultSet.getString("full_name"));
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("passwd"));
				Role role = new Role();
				role.setId(resultSet.getInt("role_id"));
				role.setName(resultSet.getString("role_name"));
				user.setRole(role);

				user.setPets(getPetsByUser(conn, user.getId()));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	private List<Pet> getPetsByUser(User user, Connection conn) throws SQLException {
		List<Pet> pets = new ArrayList<>();
		
		String sql = "select pet.id, " 
				+ "pet.name, " 
				+ "age, " 
				+ "pet.description, "
				+ "status.id as status_id, "
				+ "status.name as status_name, "
				+ "species.id as species_id, " 
				+ "species.name as species_name, "
				+ "species.description as species_description " 
				+ "from pet "
				+ "join species on pet.species_id = species.id " 
				+ "join status on pet.status_id = status.id "
				+ "where owner_id=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, user.getId());
		ResultSet resultSet = stmt.executeQuery();
		
		while (resultSet.next()) {
			Pet pet = new Pet();
			String name = resultSet.getString("name");
			int age = resultSet.getInt("age");
			String description = resultSet.getString("description");
			
			Status status = new Status(resultSet.getInt("status_id"),
					resultSet.getString("status_name"));
			
			Species species = new Species(
					resultSet.getInt("species_id"),
					resultSet.getString("species_name"),
					resultSet.getString("species_description"));

			pet = new Pet(name, age, species, description);
			pet.setId(resultSet.getInt("id"));
			pet.setStatus(status);
			
			pets.add(pet);
		}
		
		return pets;
	}
	
	/**
	 * 
	 * @param user
	 * @param conn
	 * @return true if successful, false if transaction should be rolled back
	 * @throws SQLException
	 */
	private boolean addPetsToUser(User user, Connection conn) throws SQLException {
		String sql = "select id from pet where owner_id=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, user.getId());
		
		ResultSet resultSet = stmt.executeQuery();
		List<Integer> existingIds = new ArrayList<>();
		while (resultSet.next()) {
			existingIds.add(resultSet.getInt("id"));
		}
		
		for (Pet pet : user.getPets()) {
			if (existingIds.contains(pet.getId())) {
				sql = "update pet set owner_id=? where id=?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, user.getId());
				stmt.setInt(2, pet.getId());
				
				int rowsUpdated = stmt.executeUpdate();
				if (rowsUpdated!=1) {
					return false;
				}
			}
		}
		return true;
	}

}
