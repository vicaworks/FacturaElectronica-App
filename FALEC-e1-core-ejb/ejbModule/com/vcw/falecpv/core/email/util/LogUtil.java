package com.vcw.falecpv.core.email.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;

/**
 * Utilitario para la escritura en el archivo de Logs que se defina
 */
public class LogUtil {

	/**
	 * Escribe en el archivo correspondiente al logPath el mensaje y la causa de
	 * la {@link Exception} que se pase por parametro
	 * 
	 * @param logPath
	 * @param message
	 */
	public static void escribirLog(String logPath, Exception message) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(logPath, true));
			writer.newLine();
			writer.write("ERROR: " + Calendar.getInstance().getTime().toString() + " DESCRIPCION: " + message.getMessage() + ": " + message.getCause());

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
