package model.dao;

import java.util.List;

import entities.Driver;
import entities.Outside;



public interface DriverDao {
	
	void insert(Driver obj);
	void update(Driver obj);
	void deteleByid(Integer id);
	Driver findByid(Integer id);
	List<Driver> findAll();
	List<Driver> findByOutside(Integer id);
	



}
