package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import entities.Driver;
import entities.Outside;
import entities.Truck;
import model.dao.DaoFactory;
import model.dao.DriverDao;
import model.dao.OutsideDao;
import model.dao.TruckDao;

public class Program {

	public static void main(String[] args) {

		TruckDao truckDao = DaoFactory.createTruck();
		DriverDao driverDao = DaoFactory.createDriver();
		OutsideDao outsideDao = DaoFactory.createOutside();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		Scanner sc = new Scanner(System.in);
		int resp;

		while (true) {
			System.out.println("|_______________________________________|");
			System.out.println("|                                       |");
			System.out.println("|------------Outside Control------------|");
			System.out.println();
			System.out.println();
			System.out.println("|------------Registar Saída--------[1]--|");
			System.out.println("|------------Registar Motorista----[2]--|");
			System.out.println("|------------Registar Camião-------[3]--|");
			System.out.println("|------------Consultar Saídas------[4]--|");
			System.out.println("|------------Consultar Motoristas--[5]--|");
			System.out.println("|------------Consultar Camiões-----[6]--|");
			System.out.println("|------------Remover Saída---------[7]--|");
			System.out.println("|------------Remover Motorista-----[8]--|");
			System.out.println("|------------Remover Camião--------[9]--|");
			System.out.println("|------Atribuir camião a motorista-[10]-|");
			System.out.println("|------Consultar Saídas por data---[11]-|");
			System.out.println("|-----------------Sair-------------[12]-|");
			System.out.println("|_______________________________________|");

			System.out.println();
			System.out.print("Menu -> ");
			resp = sc.nextInt();
			sc.nextLine();
			System.out.println();
			System.out.println();

			switch (resp) {
			case 1: {
				System.out.print("Data[dd/mm/aaaa]: ");
				String dateStr = sc.next();
				sc.nextLine();
				System.out.print("Local: ");
				String local = sc.nextLine();
				System.out.print("Id do motorista: ");
				int id = sc.nextInt();
				
				Driver dv = driverDao.findByid(id);
				
				LocalDate date = LocalDate.parse(dateStr, fmt);
				Outside out = new Outside(date, local, dv);
				outsideDao.insert(out);
				
				break;
			}
			case 2: {
			
				System.out.println();
				System.out.print("Nome: ");
				String name = sc.nextLine();
				
				Driver dv = new Driver(name, null);
				driverDao.insert(dv);

				break;
			}
			case 3: {

				System.out.println();
				System.out.print("Matricula: ");
				String plate = sc.next();
				sc.nextLine();
				System.out.print("Marca: ");
				String brand = sc.nextLine();
				System.out.print("Deseja atribuir motorista? [S/N]: ");
				char respDriver = sc.next().toUpperCase().charAt(0);
			
				if (respDriver == 'S') {
					System.out.println("Id do motorista: ");
					int id = sc.nextInt();
					Driver driver = driverDao.findByid(id);
					Truck truck = new Truck(plate, null, brand, driver);
					truckDao.insert(truck);
				}
				else {
					Truck truck = new Truck(plate, null, brand);
					truckDao.insert(truck);
				}

				break;
			}
			case 4: {
				List<Outside> list = outsideDao.findAll();
				System.out.println();
				
				for (Outside out : list) {
					System.out.println(out);
				}
				
				break;
			}
			case 5: {
				List<Driver> list = driverDao.findAll();
				System.out.println();
				
				for (Driver dv : list) {
					System.out.println(dv);
				}

				break;
			}
			case 6: {
				List<Truck> list = truckDao.findAll();
				System.out.println();
				
				for (Truck t : list) {
					System.out.println(t);
				}

				break;
			}
			case 7: {
				
				System.out.print("Id da saída que pretende remover? ");
				int id = sc.nextInt();
				
				outsideDao.deteleByid(id);
				
				break;
			}

			case 8: {
				System.out.print("Id do motorista que pretende remover? ");
				int id = sc.nextInt();
				
				driverDao.deteleByid(id);
				
				break;
			}
			
			case 9: {
				System.out.print("Id do camião que pretende remover? ");
				int id = sc.nextInt();
				
				truckDao.deteleByid(id);
				
				break;
			}
			
			case 10: {
			
				System.out.print("Insira o id do camião: ");
				int truckId = sc.nextInt();
				
				System.out.print("Insira o id do motorista que pretende atribuir o camião: ");
				int driverId = sc.nextInt();
				
				Truck truck = truckDao.findByid(truckId);
				
				truckDao.setDriver(truck, driverId);
				
				break;
			}
			
			case 11: {
			
				
				System.out.print("Insira data[dd/mm/aaaa]: ");
				String date = sc.next();
				System.out.println();
				LocalDate outDate = LocalDate.parse(date, fmt);
				List<Outside> list = outsideDao.findByOutside(outDate);
				
				for (Outside out : list) {
					System.out.println(out);
				}
			
				break;
			}
			
			case 12: {
				System.out.println("Ending....");
				sc.close();
				return;
			}

			default:
				System.out.print("Invalid option, try again: ");
			}
		}

	}

}
