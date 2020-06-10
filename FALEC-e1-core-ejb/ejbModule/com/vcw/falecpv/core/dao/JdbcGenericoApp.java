/**
 * 
 */
package com.vcw.falecpv.core.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import com.servitec.common.dao.JdbcGenerico;


/**
 * @author cvillarreal
 *
 */
public abstract class JdbcGenericoApp extends JdbcGenerico {

	/**
	 * 
	 */
	public JdbcGenericoApp() {
	}

	@PersistenceContext(unitName = "falecPU")
	private EntityManager em;
	/**
	 * datasource inyectado, se obtiene del contenedor EJB
	 */
	@Resource(mappedName = "java:jboss/datasources/falecpvDS")
	private DataSource dataSource;
	
	private Map<String, Object> parametros;
	
	/* (non-Javadoc)
	 * @see ec.com.villaleda.commons.dao.JdbcGenerico#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return this.em;
	}

	/* (non-Javadoc)
	 * @see ec.com.villaleda.commons.dao.JdbcGenerico#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}

	@Override
	public Map<String, Object> parametros() {
		return parametros;
	}

	@Override
	public void limpiarParametros() {
		parametros = new HashMap<String, Object>();
	}

	@Override
	public void addParametro(String nombre, Object valor) {
		parametros.put(nombre, valor);
		
	}

	@Override
	public void eliminarParametro(String nombre) {
		parametros.remove(nombre);
		
	}

}
