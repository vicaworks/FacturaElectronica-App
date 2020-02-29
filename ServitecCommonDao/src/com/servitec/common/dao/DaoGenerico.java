/**
 * 
 */
package com.servitec.common.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import com.servitec.common.dao.exception.DaoException;

/**
 * Interface de una clase Dao Generica con los metodos comunes de acceso a un determinado origen de datos
 * 
 * @author cvillarreal
 *
 */
public interface DaoGenerico<T extends Serializable, PK extends Serializable> {

	
    /**
     * Permite guardar un objeto en la base de datos
     *
     * @param Objeto tipo {@link T } sobre el cual se realizara la accion
     * @throws {@link DaoException} 
     */
    void guardar(T o) throws DaoException;

    /**
     * Permite recuperar un objeto a partir de un Objeto PK 
     *
     * @param id Clave primaria del objeto
     * @return Objeto tipo {@link T } sobre el cual se realizara la accion
     * @throws {@link DaoException}
     */
    T cargar(PK id) throws DaoException;

    /**
     * Permite actualizar  los cambios realizados a un registro de tipo {@link T} de la base de datos.
     *
     * @param Objeto tipo {@link T } sobre el cual se realizara la accion
     * @throws {@link DaoException}
     */
    void actualizar(T o) throws DaoException;

    /**
     * Eliminar logicamente un registro de tipo {@link T } de la base de datos, este metodo realiza una actualizacion al campo 
     * stsEstado, si este campo no existe el valor debe provenir establecido previamente desde el metodo que lo invoque
     *
     * @param Objeto tipo {@link T } sobre el cual se realizara la accion
     */
    void eliminar(T o) throws DaoException;
    
    /**
     * Eliminar fisicamente un registro de tipo {@link T } de la base de datos 
     *
     * @param Objeto tipo {@link T } sobre el cual se realizara la accion
     */
    void eliminacionFisica(T o) throws DaoException;

    /**
     * permite obtener el {@link  EntityManager} desde el contenedor
     * @return {@link  EntityManager} 
     */
    EntityManager getEntityManager();
    /**
     * Permite obtener el Datasource con el cual se trabajara
     *
     * @return {@link DataSource}
     */
    DataSource getDataSource();

    /**
     * Metodo Generico que realiza una busqueda por PK de una clase de entidad
     *
     * @param <T> Objecto sobre el que se raliza la operacion
     * @param primaryKey de la clase
     * @return <T> Entity encontrada
     */
    T buscar(PK primaryKey);
    
    /**
     * 
     * M&eacute;todo que refresca el objeto
     * 
     * @param t
     */
    void refrescar(T t);

	
}
