/**
 * 
 */
package com.servitec.common.service;

import java.io.Serializable;
import java.util.List;

import com.servitec.common.dao.exception.DaoException;

/**
 * @author cvillarreal
 * 
 * Base CRUD servicio
 * 
 * @version 1.0
 *
 */
public interface GenericService<T extends Serializable,PK extends Serializable> {

	/**
	 * 
	 * @author cvillarreal
	 * 
	 * Consulta todos los registros activo
	 * 
	 * @return lista de consulta
	 */
	public List<T> consultarActivos();
	
	/**
	 * 
	 * @author cvillarreal
	 * 
	 * Consulta todos los registros inactivos
	 * 
	 * @return lista de consulta
	 */
	public List<T> consultarInactivos();
	
	/**
	 * 
	 * @author cvillarreal
	 * 
	 * Consulta un registro por la clave primaria
	 * 
	 * @param pk identificador del registro
	 * @return null si no existe caso contrario el objeto
	 */
	public T consultarByPk(PK pk)throws DaoException;
	
	/**
	 * @author cvilarreal
	 * 
	 * Inserta un nuevo registro
	 * 
	 * @param o objeto persistente
	 * @return el objeto almacenado
	 */
	public T crear(T o)throws DaoException;
	
	/**
	 * @author cvillarreal
	 * 
	 * Actualiza los datos del objeto persistente
	 * 
	 * @param o objeto de modelo
	 * @return objeto de modelo actualizado
	 */
	public T actualizar(T o) throws DaoException ;
	
	/**
	 * @author cvillarreal
	 * Elimina el registro f&iacute;sicamente de la base de datos
	 * @param o objeto a eliminar
	 * @throws DaoException 
	 */
	public void eliminar(T o) throws DaoException ;
	
}
