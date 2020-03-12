/**
 * 
 */
package com.vcw.falecpv.core.dao;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import com.servitec.common.dao.impl.DaoGenericoEjb;

/**
 * 
 * 
 * @author cvillarreal
 *
 */
public class AppGenericDao<T extends Serializable,PK extends Serializable> extends DaoGenericoEjb<T, PK> {

	/**
	 * Unidad de persistencia
	 */
	@PersistenceContext(unitName = "falecPU")
	private EntityManager em;
	
	/**
	 * datasource inyectado, se obtiene del contenedor EJB
	 */
	@Resource(mappedName = "java:jboss/datasources/falecDS")
	private DataSource dataSource;
	
	public AppGenericDao(Class<T> type) {
		super(type);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

}
