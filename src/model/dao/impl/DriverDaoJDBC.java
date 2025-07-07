package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import entities.Driver;
import entities.Outside;
import model.dao.DriverDao;

public class DriverDaoJDBC implements DriverDao {

	private Connection conn;

	public DriverDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Driver obj) {
		ResultSet rs = null;
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("INSERT INTO driver " + "(name )" + "VALUES (?)",
					Statement.RETURN_GENERATED_KEYS);

			if (!obj.getName().isEmpty()) {
				st.setString(1, obj.getName());
			}
			else {
				throw new DbException("Name can't be empty!");
			}
			

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				System.out.println("Success! Driver inserted!");
			} else {
				throw new DbException("Unexpected error: No rows affected!");
			}
		}

		catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Driver obj) {
		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE driver " + "SET name = ? " + "WHERE id = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Driver Update Successfully!");
			} else {
				throw new DbException("Unexpected error: No rows affected!");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public void deteleByid(Integer id) {
		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM driver WHERE id = ?");

			st.setInt(1, id);

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Successefuly deleted!");
			} else {
				throw new DbException("Delete error: Invalid id!");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public Driver findByid(Integer id) {
		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"SELECT " 
			    + "d.*, " 
				+ "t.id AS truck_id, "
		       	+ "t.brand AS truck_brand, " 
				+ "t.plate AS truck_plate "
				+ "FROM driver d "
				+ "LEFT JOIN truck t ON d.truck_id = t.id " 
				+ "WHERE d.id = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Driver driver = instantiateDriver(rs);
				return driver;

			}
			return null;

		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public List<Driver> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Driver> list = new ArrayList<>();

		try {
			st = conn.prepareStatement("SELECT * FROM driver");

			rs = st.executeQuery();
			while (rs.next()) {
				Driver driver = instantiateDriver(rs);
				list.add(driver);
			}

			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public List<Driver> findByOutside(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<Integer, Driver> map = new HashMap<>();

		try {
			st = conn.prepareStatement(
					"SELECT " + " o.id, " + " o.date, " + " o.local, " + " o.driver_id, " + " d.name AS driver_name "
							+ "FROM outside o " + "JOIN driver d ON o.driver_id = d.id " + "WHERE o.driver_id = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			while (rs.next()) {
				int driverId = rs.getInt("driver_id");

				Driver dv = map.get(driverId);

				if (dv == null) {
					dv = new Driver();
					dv.setId(rs.getInt("driver_id"));
					dv.setName(rs.getString("driver_name"));

					map.put(driverId, dv);
				}

				String local = rs.getString("local");
				LocalDate date = rs.getDate("date").toLocalDate();

				Outside outside = new Outside(date, local);
				outside.setDriver(dv);
				dv.addOutside(outside);
			}

			return new ArrayList<>(map.values());

		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	private Driver instantiateDriver(ResultSet rs) throws SQLException {
		Driver dv = new Driver();

		dv.setId(rs.getInt("id"));
		dv.setName(rs.getString("name"));
		return dv;
	}

}
