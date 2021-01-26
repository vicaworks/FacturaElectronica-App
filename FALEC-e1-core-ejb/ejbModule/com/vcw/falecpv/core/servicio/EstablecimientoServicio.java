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
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.contadores.TCEstablecimiento;
import com.vcw.falecpv.core.dao.impl.CategoriaDao;
import com.vcw.falecpv.core.dao.impl.EstablecimientoDao;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.xpert.persistence.query.QueryBuilder;


/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EstablecimientoServicio extends AppGenericService<Establecimiento, String> {
	
	@Inject
	private EstablecimientoDao establecimientoDao;
	
	@Inject
	private CategoriaDao categoriaDao;

    public final static String formato="%09d";
    
	public void setEstablecimientoDao(EstablecimientoDao establecimientoDao) {
		this.establecimientoDao = establecimientoDao;
	}

	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public EstablecimientoServicio() {
	}

	@Override
	public List<Establecimiento> consultarActivos() {
		return null;
	}

	@Override
	public List<Establecimiento> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Establecimiento, String> getDao() {
		return establecimientoDao;
	}

	/**
	 * @return the establecimientoDao
	 */
	public EstablecimientoDao getEstablecimientoDao() {
		return establecimientoDao;
	}
	
	/**
	 * @author Isabel Lobato
	 * 
	 * @param establecimiento
	 * @return
	 * @throws DaoException
	 */
	public Establecimiento guardar(Establecimiento establecimiento)throws DaoException{
		try {
			
				
			if (establecimiento.getIdestablecimiento()==null) {
				establecimiento.setIdestablecimiento(contadorPkServicio.generarContadorTabla(TCEstablecimiento.ESTABLECIMIENTO, null));
				establecimiento.setCodigoestablecimiento(TextoUtil.rellenarCadenaConCeros("%03d", Integer.valueOf(establecimiento.getIdestablecimiento())));
				establecimiento.setPuntoemision(TextoUtil.rellenarCadenaConCeros("%03d", Integer.valueOf(establecimiento.getPuntoemision())));
				crear(establecimiento);
			}else {
					
				actualizar(establecimiento);
			}
			return establecimiento;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author Isabel Lobato
	 * @param idestablecimiento
	 * @return
	 * @throws DaoException
	 */
	public boolean tieneDependenciasEst(String idestablecimiento)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(categoriaDao.getEntityManager());

			if (q.select("e").from(Categoria.class, "e")
					.equals("e.establecimiento.idestablecimiento", idestablecimiento).count() > 0) {
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
	 * @param geTipoDocumentoEnum
	 * @param idEStablecimiento
	 * @return
	 * @throws DaoException
	 */
	public String generarNumeroDocumento(GenTipoDocumentoEnum geTipoDocumentoEnum,String idEStablecimiento)throws DaoException{
		
		try {
			
			Establecimiento e = consultarByPk(idEStablecimiento);
			
			int cantidadZero = 9;
			String numDoc =  "";
			
			switch (geTipoDocumentoEnum) {
			case FACTURA:
				e.setSecuencialfactura(e.getSecuencialfactura()+1);
				numDoc += TextoUtil.leftPadTexto(e.getSecuencialfactura()+"", cantidadZero, "0");
				break;
			case RECIBO:
				e.setSecuencialrecibo(e.getSecuencialrecibo()+1);
				numDoc += TextoUtil.leftPadTexto(e.getSecuencialrecibo()+"", cantidadZero, "0");
				break;
			case NOTA_CREDITO:
				e.setSecuencialNotaCredito(e.getSecuencialNotaCredito()+1);
				numDoc += TextoUtil.leftPadTexto(e.getSecuencialNotaCredito()+"", cantidadZero, "0");
				break;
			case NOTA_DEBITO:
				e.setSecuencialnotadebito(e.getSecuencialnotadebito()+1);
				numDoc += TextoUtil.leftPadTexto(e.getSecuencialnotadebito()+"", cantidadZero, "0");
				break;
			case RETENCION:
				e.setSecuencialretencion(e.getSecuencialretencion()+1);
				numDoc += TextoUtil.leftPadTexto(e.getSecuencialretencion()+"", cantidadZero, "0");
				break;
			case GUIA_REMISION:
				e.setSecuencialguiaremision(e.getSecuencialguiaremision()+1);
				numDoc += TextoUtil.leftPadTexto(e.getSecuencialguiaremision()+"", cantidadZero, "0");
				break;
			case LIQUIDACION_COMPRA:
				e.setSecuencialliquidacioncompra(e.getSecuencialliquidacioncompra()+1);
				numDoc += TextoUtil.leftPadTexto(e.getSecuencialliquidacioncompra()+"", cantidadZero, "0");
				break;	
			case COTIZACION:
				e.setSecuencialcotizacion(e.getSecuencialcotizacion()+1);
				numDoc += TextoUtil.leftPadTexto(e.getSecuencialcotizacion()+"", cantidadZero, "0");
				break;
			default:
				break;
			}
			
			actualizar(e);
			
			return numDoc;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEStablecimiento
	 * @return
	 * @throws DaoException
	 */
	public String generarNumeroDocumentoBorrador(String idEStablecimiento)throws DaoException{
		
		try {
			
			Establecimiento e = consultarByPk(idEStablecimiento);
			
			int cantidadZero = 9;
			String numDoc =  "";
			e.setSecuencialborrador(e.getSecuencialborrador()+1);
			numDoc += TextoUtil.leftPadTexto(e.getSecuencialborrador()+"", cantidadZero, "0");
			
			actualizar(e);
			
			return "2"+ numDoc.substring(1, 9);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public List<Establecimiento> getByEmpresa(String idEmpresa)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(establecimientoDao.getEntityManager());
			
			return qb.select("e")
					.from(Establecimiento.class,"e")
					.equals("e.empresa.idempresa", idEmpresa)
					.orderBy("e.codigoestablecimiento").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
