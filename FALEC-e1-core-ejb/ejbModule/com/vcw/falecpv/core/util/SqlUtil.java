/**
 * 
 */
package com.vcw.falecpv.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cristianvillarreal
 *
 */
public class SqlUtil {

	/**
	 * 
	 */
	public SqlUtil() {
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fecha
	 * @return
	 */
	public static String formatPostgresDate(Date fecha) {
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		return sf.format(fecha);
	}

}
