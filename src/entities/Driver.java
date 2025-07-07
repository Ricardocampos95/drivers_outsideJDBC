package entities;

import java.util.ArrayList;
import java.util.List;

public class Driver {
	
	private String name;
	private Integer id;
	
	private Truck truck;
	List<Outside> outsideList = new ArrayList<>();

	public Driver(String name, Integer id) {
		this.name = name;
		this.id = id;
	}

	public Driver() {
	}
	
	public void printOutsides(){
		for (Outside outside : outsideList) {
			System.out.print(outside);
		}
	}
	
	public void addOutside(Outside outside) {
		outsideList.add(outside);
	}
	
	public void removeOutside(Outside outside) {
		outsideList.remove(outside);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	@Override
	public String toString() {
		return "Id: " + id + "\n" + "Nome: " + name + "\n";
	 
	}
	
	
	
	
	
}
