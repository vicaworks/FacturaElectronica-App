package com.servitec.common.dao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.servitec.common.util.AppConfiguracion;

/**
 * @author wen
 * 
 */
public abstract class DBUtilGenerico  {

	protected Object[] parametros;
	protected SimpleDateFormat simpleDateFormat;
	protected SimpleDateFormat simpleDateFormat2;

	/**
	 * @return
	 */
	protected abstract DataSource getDataSource();

	/**
	 * Constructor que inicializa con el formato de la fecha en un archivo de
	 * configuraci&oacute;n
	 * 
	 */
	public DBUtilGenerico() {
		simpleDateFormat = new SimpleDateFormat(AppConfiguracion.getString("formato.fecha.basedatos"));
		simpleDateFormat2 = new SimpleDateFormat(AppConfiguracion.getString("formato.fecha.basedatos2"));
	}

	/**
	 * Es necesario ejecutar limpiarParámetros antes de asignar los parámetros a
	 * la sentencia sql
	 */
	public void limpiarParametros() {
		parametros = null;
	}

	/**
	 * Agrega el listado de objetos a la variable parametros para ser utilizada
	 * en la ejecucion de query
	 * 
	 * @param objects
	 */
	public void addParameters(Object[] objects) {
		this.parametros = objects;
	}

	/**
	 * Retorna un objeto unico de la clase especificada como parámetro Al
	 * colocar el dataSource como parámetro del QueryRunner no es necesario
	 * cerrar la conexión
	 * 
	 * @param sql
	 *            sql = Sentencia sql a ejecutarse
	 * @param T
	 *            T= Cualquiera clase Bean o No a la que se transformará el
	 *            objeto de retorno
	 * @param parametros
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public <T> T singleResult(String sql, Class<T> T,boolean soloWhere) throws Exception {
		QueryRunner run = new QueryRunner(getDataSource());

		try {
			
			
			ResultSetHandler<T> h = new BeanHandler<T>(T);
			sql = soloWhere?getCamposClase(T) + sql:sql;
			T t = run.query( sql, h, parametros);
			
			limpiarParametros();
			
			return t;
			
		} catch (Exception e) {
			throw new SQLException(e);
		}finally {
			try {
				run.getDataSource().getConnection().close();
			} catch (Exception e2) {
			}
		}

	}

	/**
	 * Retorna una lista de objetos de una clase especificada como parámetro Al
	 * colocar el dataSource como parámetro del QueryRunner no es necesario
	 * cerrar la conexión
	 * 
	 * @param sql
	 *            sql = Sentencia sql a ejecutarse
	 * @param T
	 *            T = Cualquiera clase Bean o No a la que se transformará el
	 *            objeto de retorno
	 * @param parametros
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> resultList(String sql, Class<T> T,boolean soloWhere) throws SQLException {
		QueryRunner run = new QueryRunner(getDataSource());
		try {
			
			ResultSetHandler<List<T>> h = new BeanListHandler<T>(T);
			
			sql = soloWhere?getCamposClase(T) + sql:sql;
			
			List<T> list = run.query(sql, h, parametros);
			
			limpiarParametros();
			
			return list;
		} catch (Exception e) {
			throw new SQLException(e);
		}finally {
			try {
				run.getDataSource().getConnection().close();
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * @param T
	 * @return
	 */
	private <T> String getCamposClase(Class<T> T) {
		if (T == null) {
			return "";
		}

		Field[] datos = T.getDeclaredFields();
		String campos = " ";

		for (Field field : datos) {
			if (!field.getName().equals("serialVersionUID") || !field.getName().equals("_TR"))
				campos += field.getName() + ",";
		}

		campos = campos.substring(0, campos.length() - 1);

		return "Select " + campos;
	}

	/**
	 * Método que permite actualizar o insertar o eliminar datos en la BDD El
	 * insertar debe mandar obligatoriamente los datos a insertar como
	 * parametros NO inserta si se envía la sentencia directamente El update y
	 * delete funcionan con o sin parametros
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public Integer execute(String sql) throws SQLException {
		QueryRunner run = new QueryRunner(getDataSource());
		try {
			
			Integer result = run.update(sql, parametros);
			return result;
			
		} catch (Exception e) {
			throw new SQLException(e);
		}finally {
			try {
				run.getDataSource().getConnection().close();
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * 
	 * Aplica el formato de la fecha en la cual trabaja la base de datos de
	 * forma yyyy-MM-dd
	 * 
	 * @param fecha
	 * @return el formato de la fecha de la base de datos utilizada
	 * 
	 */
	protected String formatoFecha(Date fecha) {

		if (fecha == null)
			return "";

		return simpleDateFormat.format(fecha);

	}

	/**
	 * 
	 * Aplica el formato de la fecha en la cual trabaja la base de datos de
	 * forma dd/MM/yyyy
	 * 
	 * @param fecha
	 * @return el formato de la fecha de la base de datos utilizada
	 * 
	 */
	protected String formatoFecha2(Date fecha) {

		if (fecha == null)
			return "";

		return simpleDateFormat2.format(fecha);

	}

	/**
	 * 
	 * Retorna un unico objeto del tipo especificado como parámetro Al colocar
	 * el dataSource como parámetro del QueryRunner no es necesario cerrar la
	 * conexión
	 * 
	 * @param sql
	 * @param T,
	 *            puede ser tipo String.class, Date.class, etc...
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public <T> T singleObjectResult(String sql,Class<T> T) throws SQLException {
		QueryRunner run = null;
		try {
			run = new QueryRunner(getDataSource());
			T o = null ;
			List<Object[]> list = run.query(sql, new ArrayListHandler(),parametros);
			
			if (list != null && list.size() > 0)
				o = (T)list.get(0)[0];
			
			
			
			return o;
		} catch (Exception e) {
			throw new SQLException(e);
		}finally {
			try {
				run.getDataSource().getConnection().close();
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * 
	 * Ejecuta una conulta sirecta a una lista de tipo Map
	 * 
	 * @param sql
	 *            cadena de consulta
	 * @return {@link List<Map<String,Object>>}
	 * @throws SQLException
	 *             en caso de error de la consulta
	 */
	public List<Map<String, Object>> resultListMap(String sql)throws SQLException{
		
		QueryRunner run = null;
		try {
			run = new QueryRunner(getDataSource());
			List<Map<String, Object>> result = run.query(sql, new MapListHandler(),parametros);
			
			return result;
			
		} catch (Exception e) {
			throw new SQLException(e);
		}finally {
			try {
				run.getDataSource().getConnection().close();
			} catch (Exception e2) {
			}
		}
		
	}

	/**
	 * 
	 * Retorna una consula simplr a un mapa
	 * 
	 * @param sql
	 *            cadena de consulta
	 * @return {@link Map} < {@link String}, {@link Object}>
	 * @throws SQLException
	 */
	public Map<String, Object> singleResultMap(String sql)throws SQLException{
		QueryRunner run = new QueryRunner(getDataSource());
		try {
			
			Map<String, Object> result = run.query(sql, new MapHandler(),parametros);
			
			return result;
			
		} catch (Exception e) {
			throw new SQLException(e);
		}finally {
			try {
				run.getDataSource().getConnection().close();
			} catch (Exception e2) {
			}
		}
		

	}

}
