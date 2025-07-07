package model.dao;

import java.util.List;
import entities.Truck;

public interface TruckDao {

	
	void setDriver(Truck obj, Integer id);
	void insert(Truck obj);
	void update(Truck obj);
	void deteleByid(Integer id);
	Truck findByid(Integer id);
	List<Truck> findAll();
	
	
}
