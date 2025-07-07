package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Outside {
	
	private LocalDate date;
	private String local;
	private Integer id;
	
	private Driver driver;

	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	List<Outside> list = new ArrayList<>();

	public Outside() {
	}

	public Outside(LocalDate date, String local, Driver driver) {
		this.date = date;
		this.local = local;
		this.driver = driver;
	}
	
	public Outside(LocalDate date, String local) {
		this.date = date;
		this.local = local;
	}

	public Outside(LocalDate date, String local, Integer id, Driver driver) {
		this.date = date;
		this.local = local;
		this.id = id;
		this.driver = driver;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	public List<Outside> getList() {
		return list;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Id Saída: " + getId() + " " + driver.getName() + " - " + "Data: " +  fmt.format(date) + " - " + "Local: " +  local + "\n";
	}
	
	
	
	

}
