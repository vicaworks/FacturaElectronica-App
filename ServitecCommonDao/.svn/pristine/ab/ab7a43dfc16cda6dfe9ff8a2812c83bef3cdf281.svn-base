/**
 * 
 */
package com.servitec.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.datamodel.Dato;
import com.servitec.common.dao.datamodel.FilaConsulta;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;

/**
 * @author cvillarreal
 *
 */
public abstract class DaoGenericoEjb<T extends Serializable, PK extends Serializable> implements DaoGenerico<T, PK> {

	
    //protected static final String ELIMINADO = AppConfiguracion.getString("estado.eliminado");
    //protected static final String ACTIVO = AppConfiguracion.getString("estado.activo");
    //protected static final String INACTIVO = AppConfiguracion.getString("estado.inactivo");
    //private static final String VARIABLE_ESTADO = AppConfiguracion.getString("variable.estado");
	
    private final Class<T> type;
    protected Query query;
    
    /**
     * Constructor, mediante este se indica el tipo de clase por el cual se realizara el acceso a datos
     *
     * @param type tipo de objeto del DAO.
     */
    public DaoGenericoEjb(Class<T> type) {
        this.type = type;
    }

    /**
     * Permite recuperar el objeto de consulta
     *
     * @return
     */
    public Query getQuery() {
        return query;
    }

    /**
     *
     * @param query
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    
    @Override
    public void guardar(T o) throws DaoException {
        this.getEntityManager().persist(o);

    }

    @Override
    public T cargar(PK id) throws DaoException {
        T dato = (T) this.getEntityManager().find(type, id);
        return dato;
    }

    @Override
    public void actualizar(T o) throws DaoException {
        this.getEntityManager().merge(o);
    }
    
    @Override
    public void refrescar(T arg0) {
		this.getEntityManager().refresh(arg0);
	}

    @Override
    public void eliminar(T o) throws DaoException {
        String metodo = "get" + StringUtils.capitalize(AppConfiguracion.getString("variable.estado"));
        try {
            Method get = o.getClass().getMethod(metodo);
            get.invoke(o, AppConfiguracion.getString("estado.eliminado"));
        } catch (NoSuchMethodException ex) {
            throw new DaoException(String.format(AppConfiguracion.getString("error.metodo.no.encontrado"), metodo));
        } catch (SecurityException ex) {
            throw new DaoException(String.format(AppConfiguracion.getString("error.acceso.ilegal.metodo"), metodo));
        } catch (IllegalAccessException ex) {
            throw new DaoException(String.format(AppConfiguracion.getString("error.acceso.ilegal.metodo"), metodo));
        } catch (IllegalArgumentException ex) {
            throw new DaoException(String.format(AppConfiguracion.getString("error.argumento.ilegal.metodo"), metodo));
        } catch (InvocationTargetException ex) {
            throw new DaoException(String.format(AppConfiguracion.getString("error.jecucion.metodo"), metodo));
        }
        this.getEntityManager().merge(o);
    }

    @Override
    public void eliminacionFisica(T o) throws DaoException {
        this.getEntityManager().remove(this.getEntityManager().merge(o));
    }

    /**
     * Permite crear una cadena de consulta de todos los registros, si existe el campo de estado lo usara en la consulta
     *
     * @return {@link String} con la cadena de consulta JPA
     */
    protected String buscarTodos(String condicion,Map<String, String> sortFields) {
        String sql = "select p from ".concat(type.getSimpleName()).concat(" p ");
        try {
            if (condicion != null) {
                sql = sql.concat(" WHERE ").concat(condicion);
            }
            if (sortFields!=null){
            	sql = sql.concat(crearOrdenamiento("p", sortFields));
            }
        } catch (Exception e) {
        }
        return sql;
    }

    
    /**
     * @return permite obtener el {@link  EntityManager} desde el contenedor
     */
    @Override
    public abstract EntityManager getEntityManager();

    /**
     * Permite recupera el datasorce asociado, se lo recupera desde el contenedor
     *
     * @return {@link DataSource}
     */
    @Override
    public abstract DataSource getDataSource();

    /**
     * Crea la sentencia de orden para el sql
     *
     * @param entity
     * @param sortFields
     * @return
     */
    protected String crearOrdenamiento(String entity,
            Map<String, String> sortFields) {
        if (sortFields == null || sortFields.entrySet().isEmpty()) {
            return "";
        }
        Set<Map.Entry<String, String>> entrySet = sortFields.entrySet();
        StringBuilder result = new StringBuilder(" order by ");

        int i = 1;
        for (Map.Entry<String, String> e : entrySet) {
            result.append(entity);
            result.append(".");
            result.append(e.getKey());
            result.append(" ");
            result.append(e.getValue());
            if (entrySet.size() > i++) {
                result.append(", ");
            }
        }
        return result.toString();
    }
    
    /**
     * Metodo Generico que realiza un find by PK de una clase de Entity
     *
     * @param <T> Objecto sobre el que se raliza la operacion
     * @param primaryKey de la clase
     * @return <T> Entity encontrada
     */
    @Override
    public T buscar(PK primaryKey) {
        T entity = this.getEntityManager().find(type, primaryKey);
        return entity;
    }
    
    /**
     * Permite ejecutar una consulta nativa y retornar todos los datos que provienen de la misma
     *
     * @return
     */
    public List<FilaConsulta> ejecutarConsultaNativa(String consulta) {
        List<FilaConsulta> resultado = new ArrayList<FilaConsulta>();
        query = this.getEntityManager().createNativeQuery(consulta);
        @SuppressWarnings("unchecked")
		List<Object[]> datos = query.getResultList();
        if (datos == null || datos.isEmpty()) {
            return resultado;
        }
        for (Object[] row : datos) {
            FilaConsulta fila = new FilaConsulta();
            for (Object val : row) {
                fila.add(new Dato(val == null ? null : val.getClass().getName(), val));
            }
            resultado.add(fila);
        }
        return resultado;
    }
	
}
