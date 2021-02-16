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
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmailEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaSucursal;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGPlantillasEnum;
import com.vcw.falecpv.core.dao.impl.EstablecimientoDao;
import com.vcw.falecpv.core.modelo.dto.ConfEstablecimientoDto;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
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
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	

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
//			QueryBuilder q = new QueryBuilder(categoriaDao.getEntityManager());

//			if (q.select("e").from(Categoria.class, "e")
//					.equals("e.establecimiento.idestablecimiento", idestablecimiento).count() > 0) {
//				return true;
//			}
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
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public ConfEstablecimientoDto getConfiguracion(String idEstablecimiento)throws DaoException{
		try {
			
			ConfEstablecimientoDto conf = new ConfEstablecimientoDto();
			
			// autorizacion modificacion de configuracion
			conf.setPermisoModificacion(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PERMISO_MODIFICACION, TipoRetornoParametroGenerico.BOOLEAN, idEstablecimiento));
			
			// email testing
			conf.setEmailTesting(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmailEnum.SERVER_SMTP_TEST, TipoRetornoParametroGenerico.BOOLEAN, idEstablecimiento));
			conf.setEmailsEnvioTest(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmailEnum.SERVER_SMTP_TEST_EMAIL, TipoRetornoParametroGenerico.STRING, idEstablecimiento));
			
			// plantilla email comprobantes electronicos
			conf.setPlantillaEmailEstablecimiento(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmailEnum.EMAIL_CONTENIDO_ESTABLECIMIENTO, TipoRetornoParametroGenerico.BOOLEAN, idEstablecimiento));
			conf.setPlantillaEmail(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmailEnum.EMAIL_CONTENIDO_ESTABLECIMIENTO_PLANTILLA, TipoRetornoParametroGenerico.STRING, idEstablecimiento));
			
			// plantilla comprobantes electronicos
			conf.setPlantillaFactura(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PLANTILLA_FACTURA, TipoRetornoParametroGenerico.STRING, idEstablecimiento));
			conf.setPlantillaRetencion(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PLANTILLA_RETENCION, TipoRetornoParametroGenerico.STRING, idEstablecimiento));
			conf.setPlantillaNotaCredito(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PLANTILLA_NOTA_CREDITO, TipoRetornoParametroGenerico.STRING, idEstablecimiento));
			conf.setPlantillaNotaDebito(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PLANTILLA_NOTA_DEBITO, TipoRetornoParametroGenerico.STRING, idEstablecimiento));
			conf.setPlantillaGuiaRemision(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PLANTILLA_GUIA_REMISION, TipoRetornoParametroGenerico.STRING, idEstablecimiento));
			conf.setPlantillaLiqCompra(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.PLANTILLA_LIQ_COMPRA, TipoRetornoParametroGenerico.STRING, idEstablecimiento));
			
			return conf;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param confEstablecimientoDto
	 * @throws DaoException
	 */
	public void guardarTestCorreos(String idEstablecimiento,ConfEstablecimientoDto confEstablecimientoDto)throws DaoException{
		try {
			
			ParametroGenericoEmpresa parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmailEnum.SERVER_SMTP_TEST);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.isEmailTesting()?"S":"N");
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
			parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmailEnum.SERVER_SMTP_TEST_EMAIL);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.getEmailsEnvioTest());
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param confEstablecimientoDto
	 * @throws DaoException
	 */
	public void guardarPlantillaEmail(String idEstablecimiento,ConfEstablecimientoDto confEstablecimientoDto)throws DaoException{
		try {
			
			ParametroGenericoEmpresa parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmailEnum.EMAIL_CONTENIDO_ESTABLECIMIENTO);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.isPlantillaEmailEstablecimiento()?"S":"N");
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
			parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmailEnum.EMAIL_CONTENIDO_ESTABLECIMIENTO_PLANTILLA);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.getPlantillaEmail());
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param confEstablecimientoDto
	 * @throws DaoException
	 */
	public void guardarPlantillaComprobanteElectronico(String idEstablecimiento,ConfEstablecimientoDto confEstablecimientoDto)throws DaoException{
		try {
			
			ParametroGenericoEmpresa parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmpresaSucursal.PLANTILLA_FACTURA);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.getPlantillaFactura());
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
			parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmpresaSucursal.PLANTILLA_RETENCION);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.getPlantillaRetencion());
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
			parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmpresaSucursal.PLANTILLA_NOTA_CREDITO);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.getPlantillaNotaCredito());
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
			parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmpresaSucursal.PLANTILLA_NOTA_DEBITO);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.getPlantillaNotaDebito());
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
			parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmpresaSucursal.PLANTILLA_GUIA_REMISION);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.getPlantillaGuiaRemision());
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
			
			parametroGenericoEstablecimiento = parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByEstablecimiento(idEstablecimiento, PGEmpresaSucursal.PLANTILLA_LIQ_COMPRA);
			parametroGenericoEstablecimiento.setValor(confEstablecimientoDto.getPlantillaLiqCompra());
			parametroGenericoEmpresaServicio.actualizar(parametroGenericoEstablecimiento);
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	

}
