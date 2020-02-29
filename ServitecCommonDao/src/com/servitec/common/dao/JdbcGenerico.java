/**
 * 
 */
package com.servitec.common.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;

import com.servitec.common.dao.datamodel.Dato;
import com.servitec.common.dao.datamodel.FilaConsulta;

/**
 * @author cvillarreal
 * 
 */
public abstract class JdbcGenerico {

	protected Query query;

	/**
	 * 
	 */
	public JdbcGenerico() {
	}

	/**
	 * @return permite obtener el {@link EntityManager} desde el contenedor
	 */
	public abstract EntityManager getEntityManager();

	/**
	 * Permite recupera el datasorce asociado, se lo recupera desde el
	 * contenedor
	 * 
	 * @return {@link DataSource}
	 */
	public abstract DataSource getDataSource();

	/**
	 * 
	 * Permite recuperar un mapa de parametros para la consulta
	 * 
	 */
	public abstract Map<String, Object> parametros();

	/**
     * 
     */
	public abstract void limpiarParametros();

	/**
	 * @param nombre
	 * @param valor
	 */
	public abstract void addParametro(String nombre, Object valor);

	/**
	 * @param nombre
	 */
	public abstract void eliminarParametro(String nombre);

	/**
	 * Permite ejecutar una consulta nativa y retornar todos los datos que
	 * provienen de la misma
	 * 
	 * @return
	 */
	public List<FilaConsulta> ejecutarConsultaNativa(String consulta,
			Map<String, Object> parametros, boolean columnaUnitaria) {

		List<FilaConsulta> resultado = new ArrayList<FilaConsulta>();
		query = this.getEntityManager().createNativeQuery(consulta);

		if (parametros != null) {

			for (Entry<String, Object> entry : parametros.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}

		}

		if (columnaUnitaria) {
			@SuppressWarnings("unchecked")
			List<Object> datos = query.getResultList();
			if (datos == null || datos.isEmpty()) {
				return resultado;
			}

			for (Object row : datos) {
				System.out.println(row);
				FilaConsulta fila = new FilaConsulta();
				fila.add(new Dato(
						row == null ? null : row.getClass().getName(), row));
				resultado.add(fila);
			}
			return resultado;
		}

		@SuppressWarnings("unchecked")
		List<Object[]> datos = query.getResultList();
		if (datos == null || datos.isEmpty()) {
			return resultado;
		}

		for (Object[] row : datos) {
			FilaConsulta fila = new FilaConsulta();
			for (Object val : row) {
				fila.add(new Dato(
						val == null ? null : val.getClass().getName(), val));
			}
			resultado.add(fila);
		}
		return resultado;

	}

}
