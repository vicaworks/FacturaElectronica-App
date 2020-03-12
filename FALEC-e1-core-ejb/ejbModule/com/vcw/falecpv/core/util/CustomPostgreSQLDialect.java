/**
 * 
 */
package com.vcw.falecpv.core.util;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL9Dialect;

/**
 * @author cristianvillarreal
 *
 */
public class CustomPostgreSQLDialect extends PostgreSQL9Dialect {

	/**
	 * 
	 */
	public CustomPostgreSQLDialect() {
		super();
        registerColumnType(Types.BLOB, "bytea");
	}

}
