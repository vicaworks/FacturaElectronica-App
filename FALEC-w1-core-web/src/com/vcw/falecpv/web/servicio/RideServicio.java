/**
 * 
 */
package com.vcw.falecpv.web.servicio;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.resource.ResourceException;
import javax.xml.bind.JAXBException;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.XmlCommonsUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaSucursal;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGPlantillasEnum;
import com.vcw.falecpv.core.exception.RideException;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.xml.XmlComprobanteRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlFactura;
import com.vcw.falecpv.core.modelo.xml.XmlGuiaRemision;
import com.vcw.falecpv.core.modelo.xml.XmlImpuestoRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlLiquidacionCompra;
import com.vcw.falecpv.core.modelo.xml.XmlNotaCredito;
import com.vcw.falecpv.core.modelo.xml.XmlNotaDebito;
import com.vcw.falecpv.core.modelo.xml.XmlPago;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ComprobanteUtilServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.core.servicio.sri.DocElectronicoProxy;
import com.vcw.falecpv.web.constante.ExportarFileEnum;
import com.vcw.falecpv.web.util.FileUtilApp;
import com.vcw.falecpv.web.util.MessageWebUtil;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RideServicio {
	
	@Inject
	private CabeceraServicio cabeceraServicio;
	
	@Inject
	private TipopagoServicio tipopagoServicio;
	
	@Inject
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@Inject
	private ComprobanteUtilServicio comprobanteUtilServicio;
	
	@Inject
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@Inject
	private DocElectronicoProxy docElectronicoProxy;
	
	@Inject
	private EstablecimientoServicio establecimientoServicio;
	
	@Inject
	private DetalleServicio detalleServicio;
	
	
	protected MessageWebUtil msg = new MessageWebUtil();

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param idCabecera
	 * @param tipoDocumentoIdentificador
	 * @return
	 * @throws RideException
	 * @throws DaoException
	 * @throws NoResultException
	 * @throws JAXBException
	 * @throws ResourceException
	 * @throws NumberFormatException
	 * @throws ParametroRequeridoException
	 * @throws IOException 
	 */
	public byte[] generarRideFacade(String idCabecera,String subtipo)throws RideException,DaoException,NoResultException, JAXBException, ResourceException, NumberFormatException, ParametroRequeridoException, IOException{
		
		String pathPlantilla = null;
		byte[] ride = null;
		
		Cabecera c = null;
		
		c = cabeceraServicio.consultarByPk(idCabecera);
		
		if(c==null) {
			throw new NoResultException("No existe el Comprobante " + msg.getString("label.electronico") + " ID. " + idCabecera);
		}
		
		// transformar ROM to XML SRI
		String xmlDocElectronico = docElectronicoProxy.getDocElectronicoFacade(idCabecera,
				c.getEstablecimiento().getIdestablecimiento(),
				GenTipoDocumentoEnum.getEnumByIdentificador(c.getTipocomprobante().getIdentificador()));
		
		// verifica si existe comprobante electronico
		if(xmlDocElectronico==null) {
			throw new NoResultException("No existe Comprobante " + msg.getString("label.electronico"));
		}
		
		Establecimiento e = establecimientoServicio.consultarByPk(c.getEstablecimiento().getIdestablecimiento());
		String identificadorTipoComprobante = c.getTipocomprobante().getIdentificador();
		if(subtipo != null) {
			switch (subtipo) {
			case "PUNTO_VENTA":
				identificadorTipoComprobante = "-" + identificadorTipoComprobante;
				break;

			default:
				break;
			}
		}
		pathPlantilla = getPathPlantilla(identificadorTipoComprobante,e);
		
		switch (GenTipoDocumentoEnum.getEnumByIdentificador(c.getTipocomprobante().getIdentificador())) {
		case FACTURA:
			// Factura		
			if(subtipo == null) {
				ride = getRideFactura(xmlDocElectronico, c, pathPlantilla,getPathLogoEstablecimiento(e));
			}else if(subtipo.equals("PUNTO_VENTA")) {
				ride = getRideFacturaPuntoVenta(xmlDocElectronico, c, pathPlantilla,getPathLogoEstablecimiento(e));
			}
			break;
		case RETENCION:
			// Retencion
			ride = getRideRetencion(xmlDocElectronico, c, pathPlantilla,getPathLogoEstablecimiento(e));
			break;
		case NOTA_CREDITO:
			// Nota Credito
			ride = getRideNotaCredito(xmlDocElectronico, c, pathPlantilla,getPathLogoEstablecimiento(e));
			break;
		case NOTA_DEBITO:
			// Nota Debito
			ride = getRideNotaDebito(xmlDocElectronico, c, pathPlantilla,getPathLogoEstablecimiento(e));
			break;
		case GUIA_REMISION:
			// Guia Remision
			ride = getRideGuiaRemision(xmlDocElectronico, c, pathPlantilla,getPathLogoEstablecimiento(e));
			break;
		case LIQUIDACION_COMPRA:
			// Liquidacion de compra
			ride = getRideLiqCompra(xmlDocElectronico, c, pathPlantilla,getPathLogoEstablecimiento(e));
			break;
		case RECIBO:
			ride = getRideFactura(xmlDocElectronico, c, pathPlantilla,getPathLogoEstablecimiento(e));
			break;
		default:
			throw new NoResultException("No existe tipo de cmprobante : " + c.getTipocomprobante().getIdentificador());
		}
		
		return ride;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param tipoDocumentoIdentificador
	 * @param idEstablecimiento
	 * @return
	 * @throws ResourceException
	 * @throws NumberFormatException
	 * @throws ParametroRequeridoException
	 * @throws IOException 
	 */
	private String getPathPlantilla(String tipoDocumentoIdentificador,Establecimiento establecimiento) throws  ResourceException, NumberFormatException, ParametroRequeridoException, IOException{
		
		String pathPlantilla = null;
		
		// consulta la plantilla del establecimiento
		try {
			pathPlantilla = parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.getEnumPlantillaByIdentificador(tipoDocumentoIdentificador), TipoRetornoParametroGenerico.STRING, establecimiento.getIdestablecimiento());
		} catch (DaoException | NullPointerException e) {
			pathPlantilla = null;
		} 
		
		// consulta la plantilla general
		if(pathPlantilla==null || pathPlantilla.trim().length()==0 || pathPlantilla.trim().equals("-")) {
			try {
				pathPlantilla = parametroGenericoServicio.consultarParametro(PGPlantillasEnum.getEnumPlantillaByIdentificador(tipoDocumentoIdentificador), com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING);
			} catch (DaoException | NullPointerException e) {
				pathPlantilla = null;
			}
		}
		
		// valida si existe la plantilla
		if(pathPlantilla==null) {
			throw new ResourceException("No existe el path de la plantilla del RIDE : " + GenTipoDocumentoEnum.getByIdentificador(tipoDocumentoIdentificador).toString());
		}
		
		return pathPlantilla;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param establecimiento
	 * @return
	 * @throws NumberFormatException
	 * @throws DaoException
	 * @throws ParametroRequeridoException
	 */
	private String getPathLogoEstablecimiento(Establecimiento establecimiento) throws NumberFormatException, DaoException, ParametroRequeridoException {
		
		if(establecimiento.getLogo()!=null) {
			String pathLogo = parametroGenericoServicio.consultarParametro(PGPlantillasEnum.PATH_LOGO, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING);
			File f = new File(pathLogo.concat(establecimiento.getNombreimagen()));
			if(f.exists()) {
				return pathLogo.concat(establecimiento.getNombreimagen());
			}
		}
		
		
		return parametroGenericoServicio.consultarParametro(PGPlantillasEnum.PATH_LOGO_BLANCO, com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico.STRING);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlRide
	 * @param cabecera
	 * @param pathPlantilla
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 * @throws DaoException
	 */
	private byte[] getRideFactura(String xmlRide,Cabecera cabecera, String pathPlantilla,String pathLogo) throws UnsupportedEncodingException, JAXBException, DaoException {
		XmlFactura f = XmlCommonsUtil.jaxbunmarshall(xmlRide, new XmlFactura(),"UTF-8");
		
		f.setFechaAutorizacion(cabecera.getFechaautorizacion()!=null?cabecera.getFechaautorizacion():cabecera.getFechaemision());
		f.setNumeroAutorizacion(cabecera.getNumeroautorizacion());
		f.setPathLogo(pathLogo);
		
		if(f.getInfoFactura().getPagoList()!=null) {
			for (XmlPago p : f.getInfoFactura().getPagoList()) {
				p.setDescripcion(tipopagoServicio.tipopagoSri(p.getFormaPago()));
			}
		}
		
		// totalizar el comprobante
		f.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesComprobanteFactura(f, cabecera.getEstablecimiento().getEmpresa().getIdempresa(), true));
		f.getInfoFactura().setTelefonocomprador(cabecera.getCliente().getTelefono()!=null?cabecera.getCliente().getTelefono():"-");
		
		// genera el reporte
		FileUtilApp fileUtilApp = new FileUtilApp();
		List<XmlFactura> lista = new ArrayList<>();
		lista.add(f);
		return fileUtilApp.getReportByReportPath(getNombreReporte(pathPlantilla), lista, ExportarFileEnum.PDF, getDirReporte(pathPlantilla));
	}
	
	/**
	 * @author cristianvillarreal
	 * @param xmlRide
	 * @param cabecera
	 * @param pathPlantilla
	 * @param pathLogo
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 * @throws DaoException
	 */
	private byte[] getRideFacturaPuntoVenta(String xmlRide,Cabecera cabecera, String pathPlantilla,String pathLogo) throws UnsupportedEncodingException, JAXBException, DaoException {
		XmlFactura f = XmlCommonsUtil.jaxbunmarshall(xmlRide, new XmlFactura(),"UTF-8");
		
		f.setFechaAutorizacion(cabecera.getFechaautorizacion()!=null?cabecera.getFechaautorizacion():cabecera.getFechaemision());
		f.setNumeroAutorizacion(cabecera.getNumeroautorizacion());
		f.setPathLogo(pathLogo);
		
		if(f.getInfoFactura().getPagoList()!=null) {
			for (XmlPago p : f.getInfoFactura().getPagoList()) {
				p.setDescripcion(tipopagoServicio.tipopagoSri(p.getFormaPago()));
			}
		}
		
		// totalizar el comprobante
		f.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesComprobanteFactura(f, cabecera.getEstablecimiento().getEmpresa().getIdempresa(), true));
		f.getInfoFactura().setTelefonocomprador(cabecera.getCliente().getTelefono()!=null?cabecera.getCliente().getTelefono():"-");
		
		// genera el reporte
		FileUtilApp fileUtilApp = new FileUtilApp();
		List<XmlFactura> lista = new ArrayList<>();
		lista.add(f);
		return fileUtilApp.getReportByReportPath(getNombreReporte(pathPlantilla), lista, ExportarFileEnum.PDF, getDirReporte(pathPlantilla));
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlRide
	 * @param cabecera
	 * @param pathPlantilla
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 * @throws DaoException
	 */
	private byte[] getRideRetencion(String xmlRide,Cabecera cabecera, String pathPlantilla,String pathLogo) throws UnsupportedEncodingException, JAXBException, DaoException {
		
		XmlComprobanteRetencion f = XmlCommonsUtil.jaxbunmarshall(xmlRide, new XmlComprobanteRetencion(),"UTF-8");
		
		for (XmlImpuestoRetencion c : f.getImpuestoretencionList()) {
			comprobanteUtilServicio.populateImpuestoRetencion(c);
		}
		
		f.setFechaAutorizacion(cabecera.getFechaautorizacion()!=null?cabecera.getFechaautorizacion():cabecera.getFechaemision());
		f.setNumeroAutorizacion(cabecera.getNumeroautorizacion());
		f.setPathLogo(pathLogo);
		f.setTotalretencion(cabecera.getTotalretencion());
		
		// genera el reporte
		FileUtilApp fileUtilApp = new FileUtilApp();
		List<XmlComprobanteRetencion> lista = new ArrayList<>();
		lista.add(f);
		return fileUtilApp.getReportByReportPath(getNombreReporte(pathPlantilla), lista, ExportarFileEnum.PDF, getDirReporte(pathPlantilla));
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlRide
	 * @param cabecera
	 * @param pathPlantilla
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 * @throws DaoException
	 */
	private byte[] getRideNotaCredito(String xmlRide,Cabecera cabecera, String pathPlantilla,String pathLogo) throws UnsupportedEncodingException, JAXBException, DaoException {
		XmlNotaCredito f = XmlCommonsUtil.jaxbunmarshall(xmlRide, new XmlNotaCredito(),"UTF-8");
		f.setFechaAutorizacion(cabecera.getFechaautorizacion()!=null?cabecera.getFechaautorizacion():cabecera.getFechaemision());
		f.setNumeroAutorizacion(cabecera.getNumeroautorizacion());
		f.setPathLogo(pathLogo);
		
		// tipo de comprobante
		f.getInfoNotaCredito().setComprobanteModificado("");
		if(GenTipoDocumentoEnum.getEnumByIdentificador(f.getInfoNotaCredito().getCodDocModificado())!=null) {
			f.getInfoNotaCredito().setComprobanteModificado(tipocomprobanteServicio
					.getByTipoDocumento(
							GenTipoDocumentoEnum.getEnumByIdentificador(f.getInfoNotaCredito().getCodDocModificado()))
					.getComprobante());
		}
		
		// totalizar el comprobante
		f.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesComprobantenotaCredito(f, cabecera.getEstablecimiento().getEmpresa().getIdempresa()));
		
		// genera el reporte
		FileUtilApp fileUtilApp = new FileUtilApp();
		List<XmlNotaCredito> lista = new ArrayList<>();
		lista.add(f);
		return fileUtilApp.getReportByReportPath(getNombreReporte(pathPlantilla), lista, ExportarFileEnum.PDF, getDirReporte(pathPlantilla));
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlRide
	 * @param cabecera
	 * @param pathPlantilla
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 * @throws DaoException
	 */
	private byte[] getRideNotaDebito(String xmlRide,Cabecera cabecera, String pathPlantilla,String pathLogo) throws UnsupportedEncodingException, JAXBException, DaoException {
		
		XmlNotaDebito f = XmlCommonsUtil.jaxbunmarshall(xmlRide, new XmlNotaDebito(),"UTF-8");
		f.setFechaAutorizacion(cabecera.getFechaautorizacion()!=null?cabecera.getFechaautorizacion():cabecera.getFechaemision());
		f.setNumeroAutorizacion(cabecera.getNumeroautorizacion());
		f.setPathLogo(pathLogo);
		
		// tipo de comprobante
		f.getInfoNotaDebito().setComprobanteModificado("");
		if(GenTipoDocumentoEnum.getEnumByIdentificador(f.getInfoNotaDebito().getCodDocModificado())!=null) {
			f.getInfoNotaDebito().setComprobanteModificado(tipocomprobanteServicio
					.getByTipoDocumento(
							GenTipoDocumentoEnum.getEnumByIdentificador(f.getInfoNotaDebito().getCodDocModificado()))
					.getComprobante());
		}
		if(f.getInfoNotaDebito().getPagoList()!=null) {
			for (XmlPago p : f.getInfoNotaDebito().getPagoList()) {
				p.setDescripcion(tipopagoServicio.tipopagoSri(p.getFormaPago()));
			}
		}
		
		// totalizar el comprobante
		f.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesComprobanteNotaDebito(f, cabecera.getEstablecimiento().getEmpresa().getIdempresa()));
		
		// genera el reporte
		FileUtilApp fileUtilApp = new FileUtilApp();
		List<XmlNotaDebito> lista = new ArrayList<>();
		lista.add(f);
		return fileUtilApp.getReportByReportPath(getNombreReporte(pathPlantilla), lista, ExportarFileEnum.PDF, getDirReporte(pathPlantilla));
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlRide
	 * @param cabecera
	 * @param pathPlantilla
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 * @throws DaoException
	 */
	private byte[] getRideGuiaRemision(String xmlRide,Cabecera cabecera, String pathPlantilla,String pathLogo) throws UnsupportedEncodingException, JAXBException, DaoException {
		
		XmlGuiaRemision f = XmlCommonsUtil.jaxbunmarshall(xmlRide, new XmlGuiaRemision(),"UTF-8");
		f.setFechaAutorizacion(cabecera.getFechaautorizacion());
		f.setNumeroAutorizacion(cabecera.getNumeroautorizacion());
		f.setPathLogo(pathLogo);
		
		comprobanteUtilServicio.populateGuiaRemision(f);
		
		// genera el reporte
		FileUtilApp fileUtilApp = new FileUtilApp();
		List<XmlGuiaRemision> lista = new ArrayList<>();
		lista.add(f);
		return fileUtilApp.getReportByReportPath(getNombreReporte(pathPlantilla), lista, ExportarFileEnum.PDF, getDirReporte(pathPlantilla));
		
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlRide
	 * @param cabecera
	 * @param pathPlantilla
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 * @throws DaoException
	 */
	private byte[] getRideLiqCompra(String xmlRide,Cabecera cabecera, String pathPlantilla,String pathLogo) throws UnsupportedEncodingException, JAXBException, DaoException {
		XmlLiquidacionCompra f = XmlCommonsUtil.jaxbunmarshall(xmlRide, new XmlLiquidacionCompra(),"UTF-8");
		f.setFechaAutorizacion(cabecera.getFechaautorizacion());
		f.setNumeroAutorizacion(cabecera.getNumeroautorizacion());
		f.setPathLogo(pathLogo);
		
		if(f.getInfoLiquidacionCompra().getPagoList()!=null) {
			for (XmlPago p : f.getInfoLiquidacionCompra().getPagoList()) {
				p.setDescripcion(tipopagoServicio.tipopagoSri(p.getFormaPago()));
			}
		}
		
		// totalizar el comprobante
		f.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesComprobanteLiqCompra(f, cabecera.getEstablecimiento().getEmpresa().getIdempresa()));
		
		// genera el reporte
		FileUtilApp fileUtilApp = new FileUtilApp();
		List<XmlLiquidacionCompra> lista = new ArrayList<>();
		lista.add(f);
		return fileUtilApp.getReportByReportPath(getNombreReporte(pathPlantilla), lista, ExportarFileEnum.PDF, getDirReporte(pathPlantilla));
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param pathPlantilla
	 * @return
	 */
	private String getDirReporte(String pathPlantilla) {
		
		int lp = pathPlantilla.lastIndexOf("/") + 1;
		
		return pathPlantilla.substring(0, lp);
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param pathPlantilla
	 * @return
	 */
	private String getNombreReporte(String pathPlantilla) {
		
		int lp = pathPlantilla.lastIndexOf("/") + 1;
		
		return pathPlantilla.substring(lp, pathPlantilla.length()).replace(".jasper", "");
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws RideException
	 * @throws DaoException
	 * @throws NoResultException
	 * @throws JAXBException
	 * @throws ResourceException
	 * @throws NumberFormatException
	 * @throws ParametroRequeridoException
	 * @throws IOException
	 */
	public byte[] generarCotizacionFacade(String idCabecera)throws RideException,DaoException,NoResultException, JAXBException, ResourceException, NumberFormatException, ParametroRequeridoException, IOException{
		
		String pathPlantilla = null;
		byte[] ride = null;
		
		Cabecera c = null;
		
		c = cabeceraServicio.consultarByPk(idCabecera);
		
		if(c==null) {
			throw new NoResultException("No existe la " + msg.getString("label.cotizacion.title") + " ID. " + idCabecera);
		}
		
		c.setDetalleList(detalleServicio.getDetalleDao().getByIdCabecera(c.getIdcabecera()));
		
		Establecimiento e = establecimientoServicio.consultarByPk(c.getEstablecimiento().getIdestablecimiento());
		pathPlantilla = getPathPlantilla(GenTipoDocumentoEnum.COTIZACION.getIdentificador(),e);
		String nombrePlatilla = pathPlantilla.substring(pathPlantilla.lastIndexOf("/")+1,pathPlantilla.lastIndexOf("."));
		FileUtilApp fileUtilApp = new FileUtilApp();
		c.setPathLogo(getPathLogoEstablecimiento(e));
		c.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesCotizacion(c, c.getEstablecimiento().getEmpresa().getIdempresa(), true));
		
		switch (nombrePlatilla) {
		case "CE-Cotizacion-V1":
			List<Cabecera> lista = new ArrayList<>();
			lista.add(c);
			ride = fileUtilApp.getReportByReportPath(getNombreReporte(pathPlantilla), lista, ExportarFileEnum.PDF, getDirReporte(pathPlantilla));
			break;

		default:
			throw new NoResultException("No existe plantilla  : " + msg.getString("label.cotizacion.title") + " " + c.getTipocomprobante().getIdentificador());
		}
		
		
		return ride;
	}
	
}
