package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import entities.Driver;
import entities.Truck;
import model.dao.TruckDao;

public class TruckDaoJDBC implements TruckDao {
	
	private Connection conn;
	
	public TruckDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Truck obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("INSERT INTO truck (plate, brand, driver_id) VALUES (?, ?, ?)");
			
			st.setString(1, obj.getPlate());
			st.setString(2, obj.getBrand());
			
			if (obj.getDriver() != null && obj.getDriver().getId() != null) {
				st.setInt(3, obj.getDriver().getId());
			}
			else {
				st.setNull(3, java.sql.Types.INTEGER);
			}
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				System.out.println("Success! Truck Inserted!");
			}
			else {
				throw new DbException("Unexpected error: Invalid insert!");
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
	public void update(Truck obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE truck SET plate = ?, brand = ? WHERE id = ?");
			
			st.setString(1, obj.getPlate());
			st.setString(2, obj.getBrand());
			st.setInt(3, obj.getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0 ) {
				System.out.println("Truck updated successfully!");
			}
			else {
				throw new DbException("Unexpected error: Invalid truck id!");
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deteleByid(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM truck WHERE id = ?");
			
			st.setInt(1, id);
			
			int rowsAffected = st.executeUpdate();
			
			
			if (rowsAffected > 0) {
				System.out.println("truck deleted successfully!");
			}
			else {
				throw new DbException("Error: This id does't exist!");
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Truck findByid(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT "
				+ "t.*, " 
			    + "t.id AS truck_id, "
	           	+ "t.brand AS truck_brand, " 
			    + "t.plate AS truck_plate "
			    + "FROM truck t "
			    + "WHERE t.id = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				Truck truck = instantiateTruck(rs);
				return truck;
			}
			return null;
			
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
	}
		return null;
		}

	@Override
	public List<Truck> findAll() {
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Truck> list = new ArrayList<>();
		
		try {
			st = conn.prepareStatement(
		            "SELECT t.id AS id, t.plate, t.brand, d.id AS driver_id, d.name AS name FROM truck t LEFT JOIN driver d ON t.driver_id = d.id");

			rs = st.executeQuery();
			
			while (rs.next()) {
				Truck truck = instantiateTruck(rs);
				Driver driver = instantiateDriver(rs);
				truck.setDriver(driver);
				list.add(truck);
				
			}
			return list;
			
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return list;
	}

	@Override
	public void setDriver(Truck obj, Integer id) {
		PreparedStatement st1 = null;
		PreparedStatement st2 = null;
		PreparedStatement check = null;
		ResultSet rs = null;
		
		try {
			conn.setAutoCommit(false);
			
			check = conn.prepareStatement("SELECT driver_id FROM truck WHERE id = ?");
			
			check.setInt(1, obj.getId());
			rs = check.executeQuery();
			
			if (rs.next()) {
				int currentDriverId = rs.getInt("driver_id");
				if (currentDriverId != 0) {
					throw new DbException("Truck has already driver assigned.");
				}
			}
			
			
			st1 = conn.prepareStatement("UPDATE truck SET driver_id = ? WHERE id = ?");
			
			st1.setInt(1, id);
			st1.setInt(2, obj.getId());
			
			st1.executeUpdate();
			
	
			st2 = conn.prepareStatement("UPDATE driver SET truck_id = ? WHERE id = ?");
			
			
			
			st2.setInt(1, obj.getId());
			st2.setInt(2, id);
			st2.executeUpdate();
			
			conn.commit();
	        System.out.println("Driver assigned to truck successfully!");
			
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException rollbackEx) {
				throw new DbException("Rollback error: " + rollbackEx.getMessage());
			}
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st1);
			DB.closeStatement(st2);
			try {
				conn.setAutoCommit(true);
				
			} catch (SQLException e) {
				throw new DbException("Failed to reset auto-commit: " + e.getMessage());
			}
		}
		
	}
	
	private Truck instantiateTruck(ResultSet rs) throws SQLException {
		
		Truck truck = new Truck();
		truck.setBrand(rs.getString("brand"));
		truck.setId(rs.getInt("id"));
		truck.setPlate(rs.getString("plate"));
		
		return truck;
	}
	
	private Driver instantiateDriver(ResultSet rs) throws SQLException {
		Driver dv = new Driver();

		dv.setId(rs.getInt("id"));
		dv.setName(rs.getString("name"));
		return dv;
	}
	
}
