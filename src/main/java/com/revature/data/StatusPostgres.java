package com.revature.petapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Species;
import com.revature.petapp.models.Status;
import com.revature.petapp.utils.ConnectionUtil;

public class StatusPostgres implements StatusDAO {
	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();

	@Override
	public Status create(Status t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Status> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Status t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Status t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Status findByName(String name) {
		Status status = null;
		
		// try-with-resources: sets up closing for closeable resources
		try (Connection conn = connUtil.getConnection()) {
			// set up the SQL statement that we want to execute
			String sql = "select id, name from status where name=?";

			// set up that statement with the database
			// preparedstatement is pre-processed to prevent sql injection
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name); // parameter indexes start at 1 (the first ?)

			// execute the statement
			ResultSet resultSet = stmt.executeQuery();

			// process the result set
			if (resultSet.next()) {
				status = new Status(resultSet.getInt("id"), resultSet.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return status;
	}

}
