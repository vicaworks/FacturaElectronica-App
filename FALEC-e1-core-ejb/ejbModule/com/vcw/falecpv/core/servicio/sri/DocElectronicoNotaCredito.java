/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.XmlCommonsUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlImpuesto;
import com.vcw.falecpv.core.modelo.xml.XmlNotaCredito;
import com.vcw.falecpv.core.modelo.xml.XmlNotaCreditoDetalle;
import com.vcw.falecpv.core.modelo.xml.XmlinfoNotaCredito;

/**
 * @author cristianvillarreal
 *
 */
public class DocElectronicoNotaCredito extends GenerarDocumentoElectronico {

	/**
	 * 
	 */
	public DocElectronicoNotaCredito() {
	}

	@Override
	public String generarFacade(String idDocumento, String idEstablecimeinto, GenTipoDocumentoEnum tipoDocumento)
			throws DaoException, JAXBException {
		
		XmlNotaCredito notCredito = new XmlNotaCredito();
		Cabecera cabecera = getCabeceraServicio().consultarByPk(idDocumento);
		notCredito.setInfoTributaria(getXmlInfoTributaria(cabecera));
		notCredito.setInfoNotaCredito(getInfoNotaCredito(cabecera, totalimpuestoServicio.getTotalimpuestoDao().getByIdCabecera(cabecera.getIdcabecera())));
		// detalle
		List<Detalle> detalleList = detalleServicio.getDetalleDao().getByIdCabecera(idDocumento);
		// detalle impuesto
		List<Detalleimpuesto> detalleimpuestoList = detalleimpuestoServicio.getDetalleimpuestoDao().getByIdCabecera(idDocumento);
		detalleList.stream().forEach(x->{
			x.setDetalleimpuestoList(detalleimpuestoList.stream().filter(y->y.getDetalle().getIddetalle().equals(x.getIddetalle())).collect(Collectors.toList()));
		});
		notCredito.setNotaCreditoDetalleList(getDetalle(cabecera, detalleList));
		
		// infoadicional
		notCredito.setCampoAdicionalList(getInfoAdicinal(infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idDocumento)));
		
		return XmlCommonsUtil.jaxbMarshall(notCredito, true, false,encode);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @param totalimpuestoList
	 * @return
	 */
	protected XmlinfoNotaCredito getInfoNotaCredito(Cabecera cabecera,List<Totalimpuesto> totalimpuestoList) {
		
		XmlinfoNotaCredito info = new XmlinfoNotaCredito();
		
		info.setFechaEmision(cabecera.getFechaemision());
		info.setDirEstablecimiento(cabecera.getEstablecimiento().getDireccionestablecimiento());
		info.setContribuyenteEspecial(cabecera.getContribuyenteespecial());
		info.setObligadoContabilidad(cabecera.getEstablecimiento().getEmpresa().getObligadocontablidad());
		info.setTipoIdentificacionComprador(cabecera.getCliente().getTipoIdentificacion().getCodigo());
		info.setRazonSocialComprador(cabecera.getCliente().getRazonsocial());
		info.setIdentificacionComprador(cabecera.getCliente().getIdentificacion());
		info.setCodDocModificado(cabecera.getTipodocasociado());
		info.setNumDocModificado(ComprobanteHelper.formatNumDocumento(cabecera.getNumdocasociado()));
		info.setFechaEmisionDocSustento(cabecera.getFechaemisiondocasociado());
		info.setValorModificacion(cabecera.getTotalconimpuestos().doubleValue());
		info.setTotalSinImpuestos(cabecera.getTotalsinimpuestos().doubleValue());
		info.setTotalImpuestoList(getTotalImpuesto(totalimpuestoList,false));
		info.setMotivo(cabecera.getMotivo());
		
		return info;
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @param detalleList
	 * @return
	 */
	protected List<XmlNotaCreditoDetalle> getDetalle(Cabecera cabecera,List<Detalle> detalleList){
		List<XmlNotaCreditoDetalle> lista = new ArrayList<>();
		
		for (Detalle d : detalleList) {
			XmlNotaCreditoDetalle det = new XmlNotaCreditoDetalle();
			if(d.getProducto()!=null) {
				det.setCodigoInterno(d.getProducto().getCodigoprincipal());
				det.setCodigoAdicional(d.getProducto().getCodigoauxiliar());
			}else {
				det.setCodigoInterno(d.getIddetalle());
				det.setCodigoAdicional(d.getIddetalle());
			}
			det.setDescripcion(d.getDescripcion());
			det.setCantidad(d.getCantidad().doubleValue());
			det.setPrecioUnitario(d.getPreciounitario().doubleValue());
			det.setDescuento(d.getDescuento().doubleValue());
			det.setPrecioTotalSinImpuesto(d.getPreciototalsinimpuesto().doubleValue());
			det.setImpuestoList(new ArrayList<>());
			// detalle del impuesto
			for (Detalleimpuesto i : d.getDetalleimpuestoList()) {
				XmlImpuesto impuesto = new XmlImpuesto();
				if(i.getIva()!=null) {
					impuesto.setCodigo(i.getIva().getCodigoIva());
				}
				if(i.getIce()!=null) {
					impuesto.setCodigo(i.getIce().getCodigoIce());
				}
				impuesto.setCodigoPorcentaje(impuesto.getCodigo());
				impuesto.setTarifa(i.getTarifa().doubleValue());
				impuesto.setBaseImponible(i.getBaseimponible().doubleValue());
				impuesto.setValor(i.getValor().doubleValue());
				det.getImpuestoList().add(impuesto);
			}
			lista.add(det);
		}
		
		return lista;
	}

}
