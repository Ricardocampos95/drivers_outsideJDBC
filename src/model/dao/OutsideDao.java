package model.dao;

import java.time.LocalDate;
import java.util.List;

import entities.Outside;



public interface OutsideDao {
	
	void insert(Outside obj);
	void update(Outside obj);
	void deteleByid(Integer id);
	List<Outside> findAll();
	List<Outside> findByOutside(LocalDate date);

}
