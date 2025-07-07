package model.dao;

import db.DB;
import model.dao.impl.DriverDaoJDBC;
import model.dao.impl.OutsideDaoJDBC;
import model.dao.impl.TruckDaoJDBC;

public class DaoFactory {
	
	public static DriverDao createDriver() {
		return new DriverDaoJDBC(DB.getConnection());
	}
	
	public static TruckDao createTruck() {
		return new TruckDaoJDBC(DB.getConnection());
	}
	
	public static OutsideDao createOutside() {
		return new OutsideDaoJDBC(DB.getConnection());
	}
}
