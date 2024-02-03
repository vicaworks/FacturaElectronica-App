/**
 * 
 */
package com.vcw.falecpv.core.servicio.sriimportarcomp;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.ws.WebServiceException;

import org.dom4j.DocumentException;
import org.dom4j.Node;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.XmlCommonsUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.ImportComprobanteEnum;
import com.vcw.falecpv.core.constante.contadores.TCComprobanteRecibido;
import com.vcw.falecpv.core.modelo.dto.FileSriDto;
import com.vcw.falecpv.core.modelo.dto.SriAccesoDto;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;
import com.vcw.falecpv.core.modelo.xml.XmlComprobanteRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlFactura;
import com.vcw.falecpv.core.modelo.xml.XmlImpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlImpuestoRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlLiquidacionCompra;
import com.vcw.falecpv.core.modelo.xml.XmlNotaCredito;
import com.vcw.falecpv.core.modelo.xml.XmlNotaDebito;
import com.vcw.falecpv.core.modelo.xml.XmlTotalImpuesto;
import com.vcw.falecpv.core.servicio.ComprobanteUtilServicio;
import com.vcw.falecpv.core.servicio.ComprobanterecibidoServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.sri.ws.ClienteWsSriServicio;
import com.vcw.sri.ws.autorizacion.Mensaje;
import com.vcw.sri.ws.exception.AccesoWsdlSriException;

/**
 * @author cristianvillarreal
 *
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class SriImportarComprobantesServicio {

	
	@Inject
	private TipocomprobanteServicio tipocomprobanteServicio;
	@Inject
	private EmpresaServicio empresaServicio;
	@Inject
	private ComprobanterecibidoServicio comprobanterecibidoServicio;
	@Inject
	private ContadorPkServicio contadorPkServicio;
	@Inject
	private ComprobanteUtilServicio comprobanteUtilServicio;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fileSriDto
	 * @param idEmpresa
	 * @param xmlComprobante
	 * @param idusuario
	 * @return
	 * @throws DocumentException
	 * @throws DaoException
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 */
	private Comprobanterecibido populateComprobanteRecibido(FileSriDto fileSriDto,String idEmpresa,String xmlComprobante,String idusuario) throws DocumentException, DaoException, UnsupportedEncodingException, JAXBException {
		
		String codDoc = XmlCommonsUtil.valorXpath(xmlComprobante, "//codDoc");
		
		Comprobanterecibido c = new Comprobanterecibido();
		c.setClaveAcceso(fileSriDto.getClaveAcceso());
		c.setComprobante(fileSriDto.getComprobante());
		c.setEmpresa(empresaServicio.consultarByPk(idEmpresa));
		c.setFechaAutorizacion(fileSriDto.getFechaAutorizacion());
		c.setFechaEmision(fileSriDto.getFechaEmision());
		c.setIdentificacionReceptor(fileSriDto.getIdentificacionReceptor());
		c.setImporteTotal(BigDecimal.ZERO);
		c.setNumeroAutorizacion(fileSriDto.getNumeroAutorizacion());
		
		String razonSocial = XmlCommonsUtil.valorXpath(xmlComprobante, "//infoTributaria/razonSocial");
		c.setRazonSocialEmisor(razonSocial!=null?razonSocial:fileSriDto.getEmisor());
		
		c.setRucEmisor(fileSriDto.getRucEmisor());
		c.setSerieComprobante(fileSriDto.getSerieComprobante().replace("-", ""));
		c.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(GenTipoDocumentoEnum.getEnumByIdentificador(codDoc)));
		c.setTipoEmision(fileSriDto.getTipoEmision());
		c.setValorXml(xmlComprobante);
		c.setIdusuario(idusuario);
		c.setUpdated(new Date());
		// determinar valores del comprobante
		
		GenTipoDocumentoEnum td = GenTipoDocumentoEnum.getEnumByIdentificador(codDoc);
		switch (td) {
		case FACTURA:
			XmlFactura f = XmlCommonsUtil.jaxbunmarshall(xmlComprobante, new XmlFactura());
			f.setTotalComprobanteList(comprobanteUtilServicio.populateTotalesComprobanteFactura(f, idEmpresa, true));
			
			c.setTotalsinimpuestos(BigDecimal.valueOf(f.getInfoFactura().getTotalSinImpuestos()));
			c.setTotaldescuento(BigDecimal.valueOf(f.getInfoFactura().getTotalDescuento()));
			c.setTotalconimpuestos(f.getTotalComprobanteList().get(9).getValor());
//			c.setTotaliva(f.getTotalComprobanteList().stream().filter(x->x.getLabel().contains("IVA")).findFirst().orElse(null).getValor());
//			c.setTotalice(f.getTotalComprobanteList().stream().filter(x->x.getLabel().contains("ICE")).findFirst().orElse(null).getValor());
			c.setTotaliva(f.getTotalComprobanteList().get(7).getValor());
			c.setTotalice(f.getTotalComprobanteList().get(6).getValor());
			c.setSubtotaliva(f.getTotalComprobanteList().get(0).getValor());
			c.setSubtotalivacero(f.getTotalComprobanteList().get(1).getValor());
			c.setSubtotalexcentoiva(f.getTotalComprobanteList().get(2).getValor());
			c.setSubtotalnoobjiva(f.getTotalComprobanteList().get(3).getValor());
			
			break;
		case LIQUIDACION_COMPRA:
			XmlLiquidacionCompra lc = XmlCommonsUtil.jaxbunmarshall(xmlComprobante, new XmlLiquidacionCompra());
			c.setTotalsinimpuestos(BigDecimal.valueOf(lc.getInfoLiquidacionCompra().getTotalSinImpuestos()));
			c.setTotaldescuento(BigDecimal.valueOf(lc.getInfoLiquidacionCompra().getTotalDescuento()));
			if(lc.getInfoLiquidacionCompra().getTotalImpuestoList()!=null) {
				
				for (XmlTotalImpuesto i : lc.getInfoLiquidacionCompra().getTotalImpuestoList()) {
					if(i.getCodigo().equals("2")) {
						c.setTotaliva(BigDecimal.valueOf(i.getValor()));
					}else if(i.getCodigo().equals("3")) {
						c.setTotalice(BigDecimal.valueOf(i.getValor()));
					}
				}
				
			}
			c.setTotalconimpuestos(BigDecimal.valueOf(lc.getInfoLiquidacionCompra().getImporteTotal()));
			break;
		case NOTA_CREDITO:
			XmlNotaCredito nc = XmlCommonsUtil.jaxbunmarshall(xmlComprobante, new XmlNotaCredito());
			c.setTotalsinimpuestos(BigDecimal.valueOf(nc.getInfoNotaCredito().getTotalSinImpuestos()));
			c.setTotalconimpuestos(BigDecimal.valueOf(nc.getInfoNotaCredito().getValorModificacion()));
			if(nc.getInfoNotaCredito().getTotalImpuestoList()!=null) {
				
				for (XmlTotalImpuesto i : nc.getInfoNotaCredito().getTotalImpuestoList()) {
					if(i.getCodigo().equals("2")) {
						c.setTotaliva(BigDecimal.valueOf(i.getValor()));
					}else if(i.getCodigo().equals("3")) {
						c.setTotalice(BigDecimal.valueOf(i.getValor()));
					}
				}
				
			}
			c.setMotivo(nc.getInfoNotaCredito().getMotivo());
			break;
		case NOTA_DEBITO:
			XmlNotaDebito nd = XmlCommonsUtil.jaxbunmarshall(xmlComprobante, new XmlNotaDebito());
			c.setTotalsinimpuestos(BigDecimal.valueOf(nd.getInfoNotaDebito().getTotalSinImpuestos()));
			c.setTotalconimpuestos(BigDecimal.valueOf(nd.getInfoNotaDebito().getValorTotal()));
			if(nd.getInfoNotaDebito().getImpuestoList()!=null) {
				
				for (XmlImpuesto i : nd.getInfoNotaDebito().getImpuestoList()) {
					if(i.getCodigo().equals("2")) {
						c.setTotaliva(BigDecimal.valueOf(i.getValor()));
					}else if(i.getCodigo().equals("3")) {
						c.setTotalice(BigDecimal.valueOf(i.getValor()));
					}
				}
				
			}
			break;
		case RETENCION:
			XmlComprobanteRetencion rt = XmlCommonsUtil.jaxbunmarshall(xmlComprobante, new XmlComprobanteRetencion());
			
			if(rt.getDocSustentoList()!=null) {
				// convierte v2 a v1
				comprobanterecibidoServicio.conerterV2ToV1(rt);				
			}
			
			c.setTotaliva(BigDecimal.ZERO);
			c.setTotalrenta(BigDecimal.ZERO);
			
			if(rt.getImpuestoretencionList()==null) rt.setImpuestoretencionList(new ArrayList<>());
			
			for (XmlImpuestoRetencion i : rt.getImpuestoretencionList()) {
				//renta
				if(i.getCodigo().equals("1")) {
					c.setTotalrenta(c.getTotalrenta().add(BigDecimal.valueOf(i.getValorRetenido())));
				}
				//iva
				if(i.getCodigo().equals("2")) {
					c.setTotaliva(c.getTotaliva().add(BigDecimal.valueOf(i.getValorRetenido())));
				}
				
			}
			
			c.setTotalretencion(c.getTotaliva().add(c.getTotalrenta()).setScale(2, RoundingMode.HALF_UP));
			
			break;
		default:
			break;
		}
		
		return c;
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fileSriDto
	 * @param sriAccesoDto
	 * @param idEmpresa
	 * @param idEstablecimiento
	 * @param idusuario
	 * @throws WebServiceException
	 * @throws AccesoWsdlSriException
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 * @throws DaoException
	 * @throws JAXBException
	 * @throws ParametroRequeridoException
	 */
	@Lock(LockType.WRITE)
	public void importarComprobanteSriFacade(FileSriDto fileSriDto, 
			SriAccesoDto sriAccesoDto, 
			String idEmpresa,
			String idEstablecimiento, 
			String idusuario) throws WebServiceException, AccesoWsdlSriException, UnsupportedEncodingException, DocumentException, DaoException, JAXBException, ParametroRequeridoException {
		
		// 1. acceso al ws
		ClienteWsSriServicio wsAutorizacion = new ClienteWsSriServicio(sriAccesoDto.getWsdl());
		
		com.vcw.sri.ws.autorizacion.RespuestaComprobante rc = wsAutorizacion.autorizacionComprobanteFacade(fileSriDto.getClaveAcceso());
		// analizar la consulta
		// si no existe comprobante
		if(rc.getNumeroComprobantes()!=null) {
			if(Integer.valueOf(rc.getNumeroComprobantes())>0) {
				// si no esta autorizado
				if(!rc.getAutorizaciones().getAutorizacion().get(0).getEstado().equals("AUTORIZADO")) {
					fileSriDto.setRegistrado(false);
					fileSriDto.setEstado(ImportComprobanteEnum.NO_AUTORIZADO);
					if(rc.getAutorizaciones().getAutorizacion().get(0).getMensajes()!=null && !rc.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje().isEmpty()) {
						Mensaje msj  = rc.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje().get(0);
						StringBuffer msg = new StringBuffer();
						msg.append("COD ERROR: ");
						msg.append(msj.getIdentificador());
						msg.append(" INFO : ");
						msg.append(msj.getMensaje());
						fileSriDto.setMensaje(msg.toString());
					}
					return;
				}
			}else {
				fileSriDto.setRegistrado(false);
				fileSriDto.setEstado(ImportComprobanteEnum.NO_EXISTE);
				fileSriDto.setMensaje("NO EXISTE COMPROBANTE EN EL SISTEMA DEL SRI");
				return;
			}
		}else if(rc.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje()!=null && rc.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje().size()>0) {
			Mensaje msj  = rc.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje().get(0);
			System.out.println("Cor error: " + msj.getIdentificador());
			System.out.println("Mensaje: " + msj.getMensaje());
			return;
		}
		
		// populate comprobante
		Comprobanterecibido comprobanterecibido = populateComprobanteRecibido(
				fileSriDto,
				idEmpresa,
				rc.getAutorizaciones().getAutorizacion().get(0).getComprobante(),
				idusuario);
		// comprobar si existe comprobante
		Comprobanterecibido temp = comprobanterecibidoServicio.getByComprobanteEmpresa(idEmpresa,
				GenTipoDocumentoEnum
						.getEnumByIdentificador(comprobanterecibido.getTipocomprobante().getIdentificador()),
				comprobanterecibido.getClaveAcceso(), comprobanterecibido.getSerieComprobante());
		
		fileSriDto.setRegistrado(true);
		if(temp!=null) {
			// actualizar
			comprobanterecibido.setIdcomprobanterecibido(temp.getIdcomprobanterecibido());
			fileSriDto.setEstado(ImportComprobanteEnum.ACTUALIZADO);
			comprobanterecibidoServicio.actualizar(comprobanterecibido);
		}else {
			// nuevo
			comprobanterecibido.setIdcomprobanterecibido(contadorPkServicio.generarContadorTabla(TCComprobanteRecibido.COMPROBANTERECIBIDO, idEstablecimiento));
			fileSriDto.setEstado(ImportComprobanteEnum.REGISTRADO);
			comprobanterecibidoServicio.crear(comprobanterecibido);
		}
		
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fileSriDto
	 * @param idEmpresa
	 * @param idEstablecimiento
	 * @param idusuario
	 * @throws WebServiceException
	 * @throws AccesoWsdlSriException
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 * @throws DaoException
	 * @throws JAXBException
	 * @throws ParametroRequeridoException
	 */
	@Lock(LockType.WRITE)
	public void importarComprobanteSriFacade(FileSriDto fileSriDto,
			String idEmpresa,
			String idEstablecimiento, 
			String idusuario) throws WebServiceException, AccesoWsdlSriException, UnsupportedEncodingException, DocumentException, DaoException, JAXBException, ParametroRequeridoException {
		
		String xmlAutorizacion = new String(fileSriDto.getXml(), StandardCharsets.UTF_8);
		String xmlComprobante = XmlCommonsUtil.valorXpath(xmlAutorizacion, "//comprobante");
		
		// validar que este autorizado
		if(!XmlCommonsUtil.valorXpath(xmlAutorizacion, "//estado").equals("AUTORIZADO")) {
			fileSriDto.setRegistrado(false);
			fileSriDto.setEstado(ImportComprobanteEnum.NO_AUTORIZADO);
			fileSriDto.setMensaje("EL COMPROBANTE NO ESTA AUTORIZADO");
			return;
		}
		
		// revsiar si existen mensajes de error
		List<Node> mensajeNodos = XmlCommonsUtil.aplicarXpath(
				XmlCommonsUtil.stringToDocument(xmlAutorizacion),
				"//mensaje");
		
		if(mensajeNodos != null && !mensajeNodos.isEmpty()) {			
			fileSriDto.setRegistrado(false);
			fileSriDto.setEstado(ImportComprobanteEnum.ERROR);
			fileSriDto.setMensaje(mensajeNodos.get(0).getStringValue());
			return;
		}
		
		// populate comprobante
		Comprobanterecibido comprobanterecibido = populateComprobanteRecibido(
				fileSriDto,
				idEmpresa,
				xmlComprobante,
				idusuario);
		// comprobar si existe comprobante
		Comprobanterecibido temp = comprobanterecibidoServicio.getByComprobanteEmpresa(idEmpresa,
				GenTipoDocumentoEnum
						.getEnumByIdentificador(comprobanterecibido.getTipocomprobante().getIdentificador()),
				comprobanterecibido.getClaveAcceso(), comprobanterecibido.getSerieComprobante());
		
		fileSriDto.setRegistrado(true);
		if(temp!=null) {
			// actualizar
			comprobanterecibido.setIdcomprobanterecibido(temp.getIdcomprobanterecibido());
			fileSriDto.setEstado(ImportComprobanteEnum.ACTUALIZADO);
			comprobanterecibidoServicio.actualizar(comprobanterecibido);
		}else {
			// nuevo
			comprobanterecibido.setIdcomprobanterecibido(contadorPkServicio.generarContadorTabla(TCComprobanteRecibido.COMPROBANTERECIBIDO, idEstablecimiento));
			fileSriDto.setEstado(ImportComprobanteEnum.REGISTRADO);
			comprobanterecibidoServicio.crear(comprobanterecibido);
		}
		
	}
	
		
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException 
	 * @throws ParseException 
	 */
	public FileSriDto xmlToFileSriDto(byte[] xml, Integer id,String xmlNombre) {
		
		FileSriDto fileSriDto = new FileSriDto();
		fileSriDto.setXml(xml);
		fileSriDto.setXmlNombre(xmlNombre);
		try {
			String xmlAutorizacion = new String(xml, StandardCharsets.UTF_8);
			String xmlComprobante = XmlCommonsUtil.valorXpath(xmlAutorizacion, "//comprobante");
			// limpiar comprobante 
			xmlComprobante = xmlComprobante.replace("<![CDATA[<?xml", "<?xml");
			xmlComprobante = xmlComprobante.replace(">]]", ">");
			
			fileSriDto.setEstado(ImportComprobanteEnum.PENDIENTE);
			fileSriDto.setId(id);
			fileSriDto.setComprobante(getNombreComprobante(xmlComprobante));
			fileSriDto.setSerieComprobante(
					XmlCommonsUtil.valorXpath(xmlComprobante, "//infoTributaria/estab")
					+ "-"
					+ XmlCommonsUtil.valorXpath(xmlComprobante, "//infoTributaria/ptoEmi")
					+ "-"
					+ XmlCommonsUtil.valorXpath(xmlComprobante, "//infoTributaria/secuencial")
					);
			fileSriDto.setRucEmisor(XmlCommonsUtil.valorXpath(xmlComprobante, "//infoTributaria/ruc"));
			fileSriDto.setEmisor(XmlCommonsUtil.valorXpath(xmlComprobante, "//infoTributaria/razonSocial"));
			SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
			fileSriDto.setFechaEmision(sf.parse(XmlCommonsUtil.valorXpath(xmlComprobante, "//fechaEmision")));
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String fechaAut = XmlCommonsUtil.valorXpath(xmlAutorizacion, "//fechaAutorizacion");
			fechaAut = fechaAut.substring(0,10) + " " + fechaAut.substring(11,16); 
			fileSriDto.setFechaAutorizacion(sf.parse(fechaAut));
			fileSriDto.setTipoEmision("NORMAL");
			fileSriDto.setIdentificacionReceptor(getIdentificacionReceptor(xmlComprobante));
			fileSriDto.setClaveAcceso(XmlCommonsUtil.valorXpath(xmlComprobante, "//infoTributaria/claveAcceso"));
			fileSriDto.setNumeroAutorizacion(XmlCommonsUtil.valorXpath(xmlAutorizacion, "//numeroAutorizacion"));
			fileSriDto.setImporteTotal(getImporteTotal(xmlComprobante));
			
			
		} catch (Exception e) {
			fileSriDto.setEstado(ImportComprobanteEnum.ERROR);
			fileSriDto.setMensaje("Error formato XML");
		}
		
		return fileSriDto;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xml
	 * @return
	 */
	private String getNombreComprobante(String xml) {
		
		if(xml.contains("<factura")) {
			return "Factura";
		}else if(xml.contains("<comprobanteRetencion")) {
			return "Comprobante retenci\u00F3n";
		}else if(xml.contains("<notaCredito")) {
			return "Nota de cr\u00E9dito";
		}else if(xml.contains("<notaDebito")) {
			return "Nota de d\u00E9bito";
		}else {
			return "Gu\u00EDa de remisi\u00F3n";
		}		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	private String getIdentificacionReceptor(String xml) throws DocumentException {
		
		if(xml.contains("<factura")) {
			return XmlCommonsUtil.valorXpath(xml, "//infoFactura/identificacionComprador");
		}else if(xml.contains("<comprobanteRetencion")) {
			return XmlCommonsUtil.valorXpath(xml, "//infoCompRetencion/identificacionSujetoRetenido");
		}else if(xml.contains("<notaCredito")) {
			return XmlCommonsUtil.valorXpath(xml, "//infoNotaCredito/identificacionComprador");
		}else if(xml.contains("<notaDebito")) {
			return XmlCommonsUtil.valorXpath(xml, "//infoNotaDebito/identificacionComprador");
		}else {
			return XmlCommonsUtil.valorXpath(xml, "//infoGuiaRemision/rucTransportista");
		}		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xml
	 * @return
	 * @throws NumberFormatException
	 * @throws DocumentException
	 */
	private BigDecimal getImporteTotal(String xml) throws NumberFormatException, DocumentException {
		
		if(xml.contains("<factura")) {
			return BigDecimal.valueOf(Double.parseDouble(
					XmlCommonsUtil.valorXpath(xml, "//infoFactura/importeTotal")
					)
				);
		}else if(xml.contains("<comprobanteRetencion")) {
			return BigDecimal.ZERO;
		}else if(xml.contains("<notaCredito")) {
			return BigDecimal.valueOf(Double.parseDouble(
					XmlCommonsUtil.valorXpath(xml, "//infoNotaCredito/valorModificacion")
					)
				);
		}else if(xml.contains("<notaDebito")) {
			return BigDecimal.valueOf(Double.parseDouble(
					XmlCommonsUtil.valorXpath(xml, "//notaDebito//valorTotal")
					)
				);
		}else {
			return BigDecimal.ZERO;
		}
		
	}
	
}
