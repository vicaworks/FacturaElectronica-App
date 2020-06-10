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
import com.vcw.falecpv.core.constante.contadores.TCGuiaRemision;
import com.vcw.falecpv.core.dao.impl.TransportistaDao;
import com.vcw.falecpv.core.modelo.persistencia.Transportista;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransportistaServicio extends AppGenericService<Transportista, String> {
	
	@Inject
	private TransportistaDao transportistaDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;

	@Override
	public List<Transportista> consultarActivos() {
		return null;
	}

	@Override
	public List<Transportista> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Transportista, String> getDao() {
		return transportistaDao;
	}

	/**
	 * @return the transportistaDao
	 */
	public TransportistaDao getTransportistaDao() {
		return transportistaDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param id
	 * @param idEmpresa
	 * @param tipo
	 * @param valor
	 * @return
	 * @throws DaoException
	 */
	public boolean existeCampo(String id,String idEmpresa,String tipo,String valor)throws DaoException{
		
		try {
			
			QueryBuilder q = new QueryBuilder(transportistaDao.getEntityManager());
			
			switch (tipo) {
			case "RS":
				return q.select("p")
					.from(Transportista.class,"p")
					.equals("p.razonsocial",valor)
					.notEquals("p.idtransportista", id==null?"-":id).count()>0;
					
			case "ID":
				
				return q.select("p")
						.from(Transportista.class,"p")
						.equals("p.identificacion",valor)
						.notEquals("p.idtransportista", id==null?"-":id).count()>0;
			default:
				break;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param transportista
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public Transportista guardar(Transportista transportista,String idEstablecimiento)throws DaoException{
		try {
			
			if(transportista.getIdtransportista()==null) {
				transportista.setIdtransportista(contadorPkServicio.generarContadorTabla(TCGuiaRemision.TRANSPORTISTA, idEstablecimiento));
				crear(transportista);
			}else {
				actualizar(transportista);
			}
			
			return transportista;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
