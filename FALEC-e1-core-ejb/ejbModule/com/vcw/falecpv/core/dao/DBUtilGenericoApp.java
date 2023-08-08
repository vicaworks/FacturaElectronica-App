/**
 * 
 */
package com.vcw.falecpv.core.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.servitec.common.dao.DBUtilGenerico;

/**
 * @author cvillarreal
 *
 */
public class DBUtilGenericoApp extends DBUtilGenerico {

	
	@Resource(mappedName = "java:jboss/datasources/makoDS")
	protected DataSource dataSourceJndi;
	
	/**
	 * 
	 */
	public DBUtilGenericoApp() {
	}

	/* (non-Javadoc)
	 * @see com.servitec.common.dao.DBUtilGenerico#getDataSource()
	 */
	@Override
	protected DataSource getDataSource() {
		return dataSourceJndi;
	}

}
