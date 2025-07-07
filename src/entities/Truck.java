package entities;

public class Truck {
	
	private String plate;
	private Integer id;
	private String brand;
	
	private Driver driver;
	
	public Truck(String plate, Integer id, String brand) {
		this.plate = plate;
		this.id = id;
		this.brand = brand;
	}

	public Truck(String plate, Integer id, String brand, Driver driver) {
		this.plate = plate;
		this.id = id;
		this.brand = brand;
		this.driver = driver;
	}

	public Truck() {
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		
		String driverName = (driver != null && driver.getName() != null) ? driver.getName() : "Sem motorista atribuido!";
		
		return "Matricula: " + plate + " - Id: " + id + " Marca: " + brand +" --> " + " Motorista: " + driverName + "\n";
	}
	
	
	
	
	

	
	
}
