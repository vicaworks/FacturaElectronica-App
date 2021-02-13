/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCEmpresa;
import com.vcw.falecpv.core.dao.impl.EmpresaDao;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EmpresaServicio extends AppGenericService<Empresa, String> {
		
	@Inject
	private EmpresaDao empresaDao;
	
	@Inject
	private EstablecimientoServicio establecimientoServicio;
	
	@Inject
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public EmpresaServicio() {
	}

	@Override
	public List<Empresa> consultarActivos() {		
		return null;
	}

	@Override
	public List<Empresa> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Empresa, String> getDao() {
		return empresaDao;
	}

	/**
	 * @return the empresaDao
	 */
	public EmpresaDao getEmpresaDao() {
		return empresaDao;
	}

	public Empresa guardar(Empresa empresa)throws DaoException{
		try {
			if (empresa.getIdempresa()==null) { // si no existe la empresa
				empresa.setIdempresa(contadorPkServicio.generarContadorTabla(TCEmpresa.EMPRESA, null));
//				empresa.setClavefirmaelectronica(UtilMd5.hash(empresa.getClavefirmaelectronica()));
				crear(empresa);
			}
			else { // si ya existe la empresa
//				empresa.setClavefirmaelectronica(UtilMd5.hash(empresa.getClavefirmaelectronica()));
				actualizar(empresa);
			}
			return empresa;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @throws DaoException
	 */
	public void parametrosGenericosFacade(String idEmpresa)throws DaoException{
		try {
			
			List<ParametroGenericoEmpresa> parametroList = new ArrayList<>();
			
			// de la empresa
			parametroList.add(new ParametroGenericoEmpresa("10", idEmpresa, null, "SMTP EMPRESA", "N", "FLAG SI UTILIZA SERVIDOR SMTP PROPIO"));
			parametroList.add(new ParametroGenericoEmpresa("13", idEmpresa, null, "SMTP_SERVER", "-", ""));
			parametroList.add(new ParametroGenericoEmpresa("14", idEmpresa, null, "SMTP_PORT", "587", ""));
			parametroList.add(new ParametroGenericoEmpresa("15", idEmpresa, null, "SMTP_USER", "-", ""));
			parametroList.add(new ParametroGenericoEmpresa("16", idEmpresa, null, "SMTP_PASSWORD", "-", ""));
			parametroList.add(new ParametroGenericoEmpresa("17", idEmpresa, null, "SMTP_AUTH", "true", ""));
			parametroList.add(new ParametroGenericoEmpresa("18", idEmpresa, null, "SMTP_SSL", "false", ""));
			parametroList.add(new ParametroGenericoEmpresa("19", idEmpresa, null, "SMTP_START_TLS", "true", ""));
			
			for (ParametroGenericoEmpresa p : parametroList) {
				if(!parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByIdAndEmpresa(idEmpresa, p.getIdparametroempresa())) {
					parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getEntityManager().detach(p);
					p.setId(contadorPkServicio.generarContadorTabla(TCEmpresa.PARAMETRO_EMPRESA, null, false));
					parametroGenericoEmpresaServicio.crear(p);
				}
			}
			
			
			// de cada establecimiento
			parametroList = new ArrayList<>();
			parametroList.add(new ParametroGenericoEmpresa("1", null, "idestablecimiento", "GENERAR CON SUCURSAL", "S", "SI GENERA LOS CONTADORES CON SUCURSAL"));
			parametroList.add(new ParametroGenericoEmpresa("2", null, "idestablecimiento", "CODIGO SUCURSAL", "A", "CODIGO DE LA SUCURSAL PARA LOS CONTADORES"));
			parametroList.add(new ParametroGenericoEmpresa("3", null, "idestablecimiento", "DEFAULT BORRADOR COMPROBANTES", "S", "BANDERA DE DEFAULT ESTADO BORRADOR EN LOS COMPROBANTES"));
			parametroList.add(new ParametroGenericoEmpresa("4", null, "idestablecimiento", "PLANTILLA FACTURA", "-", "PLANTILLA COMPROBANTE ELECTRONICO"));
			parametroList.add(new ParametroGenericoEmpresa("5", null, "idestablecimiento", "PLANTILLA RETENCION", "-", "PLANTILLA COMPROBANTE ELECTRONICO"));
			parametroList.add(new ParametroGenericoEmpresa("6", null, "idestablecimiento", "PLANTILLA NOTA CREDITO", "-", "PLANTILLA COMPROBANTE ELECTRONICO"));
			parametroList.add(new ParametroGenericoEmpresa("7", null, "idestablecimiento", "PLANTILLA NOTA DEBITO", "-", "PLANTILLA COMPROBANTE ELECTRONICO"));
			parametroList.add(new ParametroGenericoEmpresa("8", null, "idestablecimiento", "PLANTILLA GUIA REMISION", "-", "PLANTILLA COMPROBANTE ELECTRONICO"));
			parametroList.add(new ParametroGenericoEmpresa("9", null, "idestablecimiento", "PLANTILLA LIQ COMPRA", "-", "PLANTILLA COMPROBANTE ELECTRONICO"));
			parametroList.add(new ParametroGenericoEmpresa("11", null, "idestablecimiento", "SMTP ENVIO TEST", "N", "SI EL EMAIL ES TEST"));
			parametroList.add(new ParametroGenericoEmpresa("12", null, "idestablecimiento", "EMAIL TEST", "info@empresa.com", "EL EMAIL TO CUANDO ESTA EN TEST"));
			parametroList.add(new ParametroGenericoEmpresa("20", null, "idestablecimiento", "EMAIL PLANTILLA ESTABLECIMIENTO", "N", "SI LA EMPRESA TIENE UNA PLANTILLA ESPECIFICA"));
			parametroList.add(new ParametroGenericoEmpresa("21", null, "idestablecimiento", "EMAIL PLANTILLA COMPROBANTE ELECTRONICO", "-", "NOMBRE DE LA PLATILLA DE CONTENIDO EMAIL COMPROBANTE"));
			
			for (Establecimiento e : establecimientoServicio.getByEmpresa(idEmpresa)) {
				
				for (ParametroGenericoEmpresa p : parametroList) {
					if(!parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getByIdAndEstablecimiento(e.getIdestablecimiento(), p.getIdparametroempresa())) {
						parametroGenericoEmpresaServicio.getParametroGenericoEmpresaDao().getEntityManager().detach(p);
						p.setIdestablecimiento(e.getIdestablecimiento());
						p.setId(contadorPkServicio.generarContadorTabla(TCEmpresa.PARAMETRO_EMPRESA, null, false));
						parametroGenericoEmpresaServicio.crear(p);
					}
				}
				
			}
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
