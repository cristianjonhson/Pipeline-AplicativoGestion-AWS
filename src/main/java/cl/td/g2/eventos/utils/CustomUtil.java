package cl.td.g2.eventos.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomUtil {
	
	/**
     * Formatear un objeto LocalDateTime a una cadena de texto de acuerdo a un formato
     *
     * @param fecha-hora a formatear
     * @param formato deseado
     * @return cadena de text con la fecha-hora formateada
     */
	public static String formatLocalDateTimeToString(LocalDateTime localDateTime, String format) {
		if (localDateTime == null) {
			localDateTime = LocalDateTime.now();
		}
        if (format == null || format.isEmpty()) {
            format = "dd/MM/yyyy HH:mm";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }
	
	/**
     * Convertir una cadena de texto a LocalDateTime de acuerdo a un formato
     *
     * @param cadena de texto con la fecha-hora a convertir
     * @param formato deseado
     * @return objeto LocalDateTime con la fecha-hora formateada
     */
	public static LocalDateTime formatStringToLocalDateTime(String dateTime, String format) {
		if (dateTime == null) {
			dateTime = LocalDateTime.now().toString();
		}
        if (format == null || format.isEmpty()) {
            format = "dd/MM/yyyy HH:mm";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTime, formatter);
    }
	
	/**
     * Setear en mayúscula la primera letra de una cadena de texto
     *
     * @param cadena de texto
     * @return la cadena con su primera letra en mayúscula
     * @example String name = CustomUtil.capitalizeFirstLetter("john doe");
     */
	public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
