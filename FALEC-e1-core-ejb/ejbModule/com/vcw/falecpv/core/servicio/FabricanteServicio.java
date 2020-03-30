/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCFabricante;
import com.vcw.falecpv.core.dao.impl.FabricanteDao;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class FabricanteServicio extends AppGenericService<Fabricante, String> {

	@Inject
	private FabricanteDao fabricanteDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public FabricanteServicio() {
	}

	@Override
	public List<Fabricante> consultarActivos() {
		return null;
	}

	@Override
	public List<Fabricante> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Fabricante, String> getDao() {
		return fabricanteDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idcategoria
	 * @return
	 * @throws DaoException
	 */
	public boolean tieneDependencias(String idfabricante,String idEstablecimiento)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(fabricanteDao.getEntityManager());
			
			if(q.select("p")
				.from(Producto.class,"p")
				.equals("p.establecimiento.idestablecimiento", idEstablecimiento)
				.equals("p.fabricante.idfabricante",idfabricante).count()>0) {
				
				return true;
				
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fabricante
	 * @return
	 * @throws DaoException
	 */
	public Fabricante guardar(Fabricante fabricante,String idEstablecimiento)throws DaoException{
		
		try {
			
			if(fabricante.getIdfabricante()==null) {
				fabricante.setIdfabricante(contadorPkServicio.generarContadorTabla(TCFabricante.FABRICANTE, idEstablecimiento));
				crear(fabricante);
			}else {
				actualizar(fabricante);
			}
			
			return fabricante;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	/**
	 * @return the fabricanteDao
	 */
	public FabricanteDao getFabricanteDao() {
		return fabricanteDao;
	}
	

}
