/**
 * 
 */
package com.vcw.falecpv.core.util;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

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
	
	@Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
            return BinaryTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }
}
