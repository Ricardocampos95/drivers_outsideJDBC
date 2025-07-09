package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validate {
	
	private Validate() {
		
	}
	
	public static void name(String name) {
		String regex = "^[A-Za-zÀ-ÿ]+(\\s[A-Za-zÀ-ÿ]+)*$";
		if (!name.matches(regex)) {
			throw new IllegalArgumentException("O nome deve conter apenas letras e espaços (sem números ou símbolos).");
		}	
	}
	
	public static LocalDate date(String date, DateTimeFormatter fmt) {
		if (date == null || date.trim().isEmpty()) {
			throw new IllegalArgumentException("A data não pode estar vazia.");
		}
		
		try {
			return LocalDate.parse(date, fmt);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("A data deve estar no formato válido: dd/mm/aaaa.");
		}
	}
	
	public static void plate(String plate) {
		
		if (!plate.matches("[A-Z0-9]{6}")) {
		    throw new IllegalArgumentException("A matrícula deve conter exatamente 6 letras/números (sem espaços ou símbolos).");
		}
	}

}
