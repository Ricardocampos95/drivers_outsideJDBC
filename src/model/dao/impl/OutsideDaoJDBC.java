package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import entities.Driver;
import entities.Outside;
import model.dao.OutsideDao;

public class OutsideDaoJDBC implements OutsideDao {

	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private Connection conn;

	public OutsideDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Outside obj) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("INSERT INTO outside (date, local, driver_id) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, java.sql.Date.valueOf(obj.getDate()));
			st.setString(2, obj.getLocal());
			

			if (obj.getDriver() != null && obj.getDriver().getId() != null) {
				st.setInt(3, obj.getDriver().getId());
			}
			else {
				throw new DbException("Invalid id!");
			}

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
					System.out.println("Success! Inserted with ID: " + id);
				} else {
					throw new DbException("Unexpected error: No rows affected!");
				}

			}
		}

		catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void update(Outside obj) {

		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE outside " + "SET date = ?, local = ?, driver_id = ? " + "WHERE id = ?");

			st.setDate(1, java.sql.Date.valueOf(obj.getDate()));
			st.setString(2, obj.getLocal());
			st.setInt(3, obj.getDriver().getId());
			st.setInt(4, obj.getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Success! Rows Affected: " + rowsAffected);
			} else {
				throw new DbException("Unexpected error: No rows affectd!");
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
			st = conn.prepareStatement("DELETE FROM outside WHERE id = ?");

			st.setInt(1, id);

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Success!! Rows deleted: " + rowsAffected);
			} else {
				throw new DbException("Unexpected error: Invalid id!");

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}


	@Override
	public List<Outside> findAll() {
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Outside> list = new ArrayList<>();
		
		try {
			st = conn.prepareStatement("SELECT outside.id, "
					+ "outside.date, "
					+ "outside.local, "
					+ "outside.driver_id, "
					+ "driver.name AS name "
					+ "FROM outside "
					+ "JOIN driver ON outside.driver_id = driver.id "
					+ "ORDER BY outside.date ASC");
					
			rs = st.executeQuery();
			
			while (rs.next()) {
				Outside out = instantiateOutside(rs);
				list.add(out);
			}
			
	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return list;
	
	}

	@Override
	public List<Outside> findByOutside(LocalDate date) {
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Outside> list = new ArrayList<>();
		
		try {
			st = conn.prepareStatement("SELECT outside.id, "
					+ "outside.date, "
					+ "outside.local, "
					+ "outside.driver_id, "
					+ "driver.name AS name "
					+ "FROM outside "
					+ "JOIN driver ON outside.driver_id = driver.id "
					+ "WHERE outside.date = ?");
					
				st.setDate(1, java.sql.Date.valueOf(date));
				rs = st.executeQuery();
				
				boolean hasResult = false;
				
				while (rs.next()) {
					hasResult = true;
					Outside out = instantiateOutside(rs);
					list.add(out);
				}
				
				if (!hasResult) {
					throw new DbException("Invalid date: no records found for " + date);
				}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return list;
	}

	private Outside instantiateOutside(ResultSet rs) throws SQLException {
		Outside out = new Outside();

		out.setId(rs.getInt("id"));
		out.setDate(rs.getDate("date").toLocalDate());
		out.setLocal(rs.getString("local"));
		out.setDriver(instantiateDriver(rs));
		return out;
	}

	private Driver instantiateDriver(ResultSet rs) throws SQLException {
		Driver dv = new Driver();

		dv.setId(rs.getInt("id"));
		dv.setName(rs.getString("name"));
		return dv;
	}

}
