/**
 * 
 */
package com.vcw.falecpv.core.servicio.sriimportarcomp;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

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
		c.setTotalbaseimponible(BigDecimal.ZERO);
		c.setTotalconimpuestos(BigDecimal.ZERO);
		c.setTotaldescuento(BigDecimal.ZERO);
		c.setTotalice(BigDecimal.ZERO);
		c.setTotaliva(BigDecimal.ZERO);
		c.setTotalretencion(BigDecimal.ZERO);
		c.setTotalsinimpuestos(BigDecimal.ZERO);
		c.setTotalrenta(BigDecimal.ZERO);
		c.setValorXml(xmlComprobante);
		c.setIdusuario(idusuario);
		c.setUpdated(new Date());
		// determinar valores del comprobante
		
		GenTipoDocumentoEnum td = GenTipoDocumentoEnum.getEnumByIdentificador(codDoc);
		switch (td) {
		case FACTURA:
			XmlFactura f = XmlCommonsUtil.jaxbunmarshall(xmlComprobante, new XmlFactura());
			c.setTotalsinimpuestos(BigDecimal.valueOf(f.getInfoFactura().getTotalSinImpuestos()));
			c.setTotaldescuento(BigDecimal.valueOf(f.getInfoFactura().getTotalDescuento()));
			if(f.getInfoFactura().getTotalImpuestoList()!=null) {
				
				for (XmlTotalImpuesto i : f.getInfoFactura().getTotalImpuestoList()) {
					if(i.getCodigo().equals("2")) {
						c.setTotaliva(BigDecimal.valueOf(i.getValor()));
					}else if(i.getCodigo().equals("3")) {
						c.setTotalice(BigDecimal.valueOf(i.getValor()));
					}
				}
				
			}
			c.setTotalconimpuestos(BigDecimal.valueOf(f.getInfoFactura().getImporteTotal()));
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
			
			c.setTotaliva(BigDecimal.ZERO);
			c.setTotalrenta(BigDecimal.ZERO);
			
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
		Comprobanterecibido comprobanterecibido = populateComprobanteRecibido(fileSriDto,idEmpresa,rc.getAutorizaciones().getAutorizacion().get(0).getComprobante(),idusuario);
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
		
	
	
	
}
