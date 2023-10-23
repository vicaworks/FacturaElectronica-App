/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.modelo.xml.XmlTotalComprobante;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "cabecera")
public class Cabecera implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 443837286313315046L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idcabecera", nullable = false, length = 40)
    private String idcabecera;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tipoemision", nullable = false, length = 1)
    private String tipoemision;
    @Size(max = 49)
    @Column(name = "claveacceso", length = 49)
    private String claveacceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaemision", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaemision;
    @Size(max = 13)
    @Column(name = "contribuyenteespecial", length = 13)
    private String contribuyenteespecial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalsinimpuestos", precision = 12, scale = 2)
    private BigDecimal totalsinimpuestos = BigDecimal.ZERO;
    @Column(name = "totaldescuento", precision = 12, scale = 2)
    private BigDecimal totaldescuento = BigDecimal.ZERO;
    @Column(name = "totalconimpuestos", precision = 12, scale = 2)
    private BigDecimal totalconimpuestos = BigDecimal.ZERO;
    @Column(name = "importetotal", precision = 12, scale = 2)
    private BigDecimal importetotal = BigDecimal.ZERO;
    @Size(max = 15)
    @Column(name = "moneda", length = 15)
    private String moneda;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "secuencial", nullable = false, length = 9)
    private String secuencial;
    @Column(name = "totaliva", precision = 12, scale = 2)
    private BigDecimal totaliva = BigDecimal.ZERO;
    @Column(name = "totalice", precision = 12, scale = 2)
    private BigDecimal totalice = BigDecimal.ZERO;
    @Column(name = "propina", precision = 12, scale = 2)
    private BigDecimal propina = BigDecimal.ZERO;
    @Size(max = 49)
    @Column(name = "numeroautorizacion", length = 49)
    private String numeroautorizacion;
    @Size(max = 2)
    @Column(name = "tipodocasociado", length = 2)
    private String tipodocasociado;
    @Column(name = "fechaemisiondocasociado")
    @Temporal(TemporalType.DATE)
    private Date fechaemisiondocasociado;
    @Column(name = "valordocasociado", precision = 12, scale = 2)
    private BigDecimal valordocasociado = BigDecimal.ZERO;
    @Column(name = "fechainiciotransporte")
    @Temporal(TemporalType.DATE)
    private Date fechainiciotransporte;
    @Column(name = "fechafintransporte")
    @Temporal(TemporalType.DATE)
    private Date fechafintransporte;
    @Size(max = 300)
    @Column(name = "direccionpartida", length = 300)
    private String direccionpartida;
    @Size(max = 7)
    @Column(name = "periodofiscal", length = 7)
    private String periodofiscal;
    @Size(max = 10)
    @Column(name = "placa", length = 10)
    private String placa;
    @Size(max = 2)
    @Column(name = "estadoautorizacion", length = 2)
    private String estadoautorizacion;
    @Column(name = "fechaautorizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaautorizacion;
    @Size(max = 300)
    @Column(name = "errorautorizacion", length = 300)
    private String errorautorizacion;
    @Size(max = 300)
    @Column(name = "motivo", length = 300)
    private String motivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idusuario", nullable = false, length = 40)
    private String idusuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
    
    @Basic(optional = true)
    @Size(min = 1, max = 15)
    @Column(name = "numfactura", nullable = true, length = 15)
    private String numfactura;
    
    @Basic(optional = true)
    @Size(min = 1, max = 15)
    @Column(name = "numdocumento", nullable = true, length = 15)
    private String numdocumento;
    
    @Basic(optional = true)
    @Size(min = 1, max = 40)
    @Column(name = "idguiaremision", nullable = true, length = 40)
    private String idguiaremision;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idcliente", referencedColumnName = "idcliente", nullable = true)
    private Cliente cliente;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "idestablecimiento", nullable = false)
    private Establecimiento establecimiento;
    
    @JoinColumn(name = "idtipocomprobante", referencedColumnName = "idtipocomprobante", nullable = false)
    @ManyToOne(optional = false)
    private Tipocomprobante tipocomprobante;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idadquisicion", referencedColumnName = "idadquisicion", nullable = true)
    private Adquisicion adquisicion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalbaseimponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalbaseimponible = BigDecimal.ZERO;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalretencion", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalretencion = BigDecimal.ZERO;
    
    @Basic(optional = true)
    @Size(min = 1, max = 17	)
    @Column(name = "numdocasociado", nullable = true, length = 17)
    private String numdocasociado;
    
    @Basic(optional = true)
    @Size(min = 1, max = 40	)
    @Column(name = "idcabecerapadre", nullable = true, length = 40)
    private String idcabecerapadre;
    
    @Column(name = "valorretenidoiva", precision = 12, scale = 2)
    private BigDecimal valorretenidoiva = BigDecimal.ZERO;
    
    @Column(name = "valorretenidorenta", precision = 12, scale = 2)
    private BigDecimal valorretenidorenta = BigDecimal.ZERO;
    
    @Column(name = "valorretenido", precision = 12, scale = 2)
    private BigDecimal valorretenido = BigDecimal.ZERO;
    
    @Column(name = "envioemail")
    private Integer envioemail = 0;
    
    @Size(min = 1, max = 100)
    @Column(name = "resumenpago", nullable = false, length = 100)
    private String resumenpago = "EFECTIVO";
    
    @Column(name = "valorapagar", precision = 12, scale = 2)
    private BigDecimal valorapagar = BigDecimal.ZERO;
    
    @Size(max = 2147483647)
    @Column(name = "contenido1", length = 2147483647)
    private String contenido1;
    
    @Size(max = 2147483647)
    @Column(name = "contenido2", length = 2147483647)
    private String contenido2;
    
    @Size(max = 2147483647)
    @Column(name = "contenido3", length = 2147483647)
    private String contenido3;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "fechavencimiento")
    private Date fechaVencimiento;
    
    @Column(name = "autorizacion",nullable = true)
    private Integer autorizacion=0;
    
    @Column(name = "idusuarioautorizacion")
    private String idusuarioautorizacion;
    
    @Size(max = 400)
    @Column(name = "resumen", length = 400)
    private String resumen;
    
    @Size(max = 3)
    @Column(name = "secuencialcaja", length = 3)
    private String secuencialCaja;
    
    @Column(name = "regimenrimpe")
    private Integer regimenrimpe = 0;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idtipocomprobanteretencion", referencedColumnName = "idtipocomprobante", nullable = true)
    private Tipocomprobante tipocomprobanteretencion;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idtransportista", referencedColumnName = "idtransportista", nullable = true)
    private Transportista transportista;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario",insertable = false,updatable = false,nullable = true)
    private Usuario usuarioconsulta;
    
//    @Transient
    @JoinColumn(name = "idetiqueta", referencedColumnName = "idetiqueta", nullable = true)
    @ManyToOne(optional = true)
    private Etiqueta etiqueta;
    
    @Transient
    private List<Motivo> motivoList;
    
    @Transient
    private List<Totalimpuesto> totalimpuestoList;
    
    @Transient
    private List<Destinatario> destinatarioList;
    
    @Transient
    private List<Detalle> detalleList;
    
    @Transient
    private List<Infoadicional> infoadicionalList;
    
    @Transient
    private List<Pago> pagoList;
    
    @Transient
    private List<Detalle> detalleEliminarList;
    
    @Transient
    private List<Impuestoretencion> impuestoretencionList;
    
    @Transient
    private BigDecimal total = BigDecimal.ZERO;
    
    @Transient
    private BigDecimal otrosPagos = BigDecimal.ZERO;
    
    @Transient
    private int cantidadDestinatario = 0;
    
    @Transient
    private boolean editarSecuencial;
    
    @Transient
    private String secuencialEstablecimiento;
    
    @Transient
    private String secuencialNumero;
    
    @Transient
    private GenTipoDocumentoEnum genTipoDocumentoEnum;
    
    @Transient
    private BigDecimal totalpagar = BigDecimal.ZERO;
    
    @Transient
    private boolean envioEmailBol=false;
    
    @Transient
    private boolean autorizacionBol=false;
    
    @Transient
    private boolean borrador=false;
    
    @Transient
    private String idUsurioTransaccion;
    
    @Transient
    private String usuario;
    
    @Transient
    private boolean seleccion=false;
    
    @Transient
	private String pathLogo;
    
    @Transient
	private List<XmlTotalComprobante> totalComprobanteList;
    
    @Transient
	private String clienteCompleto;
    
    
    @Transient
    private String contenidoEmail = null;
    
    @Transient
    private BigDecimal totalCredito = BigDecimal.ZERO;
    
    @Transient
    private String filterComprobanteTransportista;
    
    
    /**
	 * 
	 */
	public Cabecera() {
		
	}

	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idcabecera != null ? idcabecera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cabecera)) {
            return false;
        }
        Cabecera other = (Cabecera) object;
        if ((this.idcabecera == null && other.idcabecera != null) || (this.idcabecera != null && !this.idcabecera.equals(other.idcabecera))) {
            return false;
        }
        return true;
    }
    
    public boolean isEstadoBorrador() {
    	return idcabecera==null || estado.equals(ComprobanteEstadoEnum.BORRADOR.toString());
    }

	/**
	 * @return the idcabecera
	 */
	public String getIdcabecera() {
		return idcabecera;
	}

	/**
	 * @param idcabecera the idcabecera to set
	 */
	public void setIdcabecera(String idcabecera) {
		this.idcabecera = idcabecera;
	}

	/**
	 * @return the tipoemision
	 */
	public String getTipoemision() {
		return tipoemision;
	}

	/**
	 * @param tipoemision the tipoemision to set
	 */
	public void setTipoemision(String tipoemision) {
		this.tipoemision = tipoemision;
	}

	/**
	 * @return the claveacceso
	 */
	public String getClaveacceso() {
		return claveacceso;
	}

	/**
	 * @param claveacceso the claveacceso to set
	 */
	public void setClaveacceso(String claveacceso) {
		this.claveacceso = claveacceso;
	}

	/**
	 * @return the fechaemision
	 */
	public Date getFechaemision() {
		return fechaemision;
	}

	/**
	 * @param fechaemision the fechaemision to set
	 */
	public void setFechaemision(Date fechaemision) {
		this.fechaemision = fechaemision;
	}

	/**
	 * @return the contribuyenteespecial
	 */
	public String getContribuyenteespecial() {
		return contribuyenteespecial;
	}

	/**
	 * @param contribuyenteespecial the contribuyenteespecial to set
	 */
	public void setContribuyenteespecial(String contribuyenteespecial) {
		this.contribuyenteespecial = contribuyenteespecial;
	}

	/**
	 * @return the totalsinimpuestos
	 */
	public BigDecimal getTotalsinimpuestos() {
		return totalsinimpuestos;
	}

	/**
	 * @param totalsinimpuestos the totalsinimpuestos to set
	 */
	public void setTotalsinimpuestos(BigDecimal totalsinimpuestos) {
		this.totalsinimpuestos = totalsinimpuestos;
	}

	/**
	 * @return the totaldescuento
	 */
	public BigDecimal getTotaldescuento() {
		return totaldescuento;
	}

	/**
	 * @param totaldescuento the totaldescuento to set
	 */
	public void setTotaldescuento(BigDecimal totaldescuento) {
		this.totaldescuento = totaldescuento;
	}

	/**
	 * @return the totalconimpuestos
	 */
	public BigDecimal getTotalconimpuestos() {
		return totalconimpuestos;
	}

	/**
	 * @param totalconimpuestos the totalconimpuestos to set
	 */
	public void setTotalconimpuestos(BigDecimal totalconimpuestos) {
		this.totalconimpuestos = totalconimpuestos;
	}

	/**
	 * @return the importetotal
	 */
	public BigDecimal getImportetotal() {
		return importetotal;
	}

	/**
	 * @param importetotal the importetotal to set
	 */
	public void setImportetotal(BigDecimal importetotal) {
		this.importetotal = importetotal;
	}

	/**
	 * @return the moneda
	 */
	public String getMoneda() {
		return moneda;
	}

	/**
	 * @param moneda the moneda to set
	 */
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	/**
	 * @return the secuencial
	 */
	public String getSecuencial() {
		return secuencial;
	}

	/**
	 * @param secuencial the secuencial to set
	 */
	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}

	/**
	 * @return the totaliva
	 */
	public BigDecimal getTotaliva() {
		return totaliva;
	}

	/**
	 * @param totaliva the totaliva to set
	 */
	public void setTotaliva(BigDecimal totaliva) {
		this.totaliva = totaliva;
	}

	/**
	 * @return the totalice
	 */
	public BigDecimal getTotalice() {
		return totalice;
	}

	/**
	 * @param totalice the totalice to set
	 */
	public void setTotalice(BigDecimal totalice) {
		this.totalice = totalice;
	}

	/**
	 * @return the propina
	 */
	public BigDecimal getPropina() {
		return propina;
	}

	/**
	 * @param propina the propina to set
	 */
	public void setPropina(BigDecimal propina) {
		this.propina = propina;
	}

	/**
	 * @return the numeroautorizacion
	 */
	public String getNumeroautorizacion() {
		return numeroautorizacion;
	}

	/**
	 * @param numeroautorizacion the numeroautorizacion to set
	 */
	public void setNumeroautorizacion(String numeroautorizacion) {
		this.numeroautorizacion = numeroautorizacion;
	}

	/**
	 * @return the tipodocasociado
	 */
	public String getTipodocasociado() {
		return tipodocasociado;
	}

	/**
	 * @param tipodocasociado the tipodocasociado to set
	 */
	public void setTipodocasociado(String tipodocasociado) {
		this.tipodocasociado = tipodocasociado;
	}

	/**
	 * @return the fechaemisiondocasociado
	 */
	public Date getFechaemisiondocasociado() {
		return fechaemisiondocasociado;
	}

	/**
	 * @param fechaemisiondocasociado the fechaemisiondocasociado to set
	 */
	public void setFechaemisiondocasociado(Date fechaemisiondocasociado) {
		this.fechaemisiondocasociado = fechaemisiondocasociado;
	}

	/**
	 * @return the valordocasociado
	 */
	public BigDecimal getValordocasociado() {
		return valordocasociado;
	}

	/**
	 * @param valordocasociado the valordocasociado to set
	 */
	public void setValordocasociado(BigDecimal valordocasociado) {
		this.valordocasociado = valordocasociado;
	}

	/**
	 * @return the fechainiciotransporte
	 */
	public Date getFechainiciotransporte() {
		return fechainiciotransporte;
	}

	/**
	 * @param fechainiciotransporte the fechainiciotransporte to set
	 */
	public void setFechainiciotransporte(Date fechainiciotransporte) {
		this.fechainiciotransporte = fechainiciotransporte;
	}

	/**
	 * @return the fechafintransporte
	 */
	public Date getFechafintransporte() {
		return fechafintransporte;
	}

	/**
	 * @param fechafintransporte the fechafintransporte to set
	 */
	public void setFechafintransporte(Date fechafintransporte) {
		this.fechafintransporte = fechafintransporte;
	}

	/**
	 * @return the direccionpartida
	 */
	public String getDireccionpartida() {
		return direccionpartida;
	}

	/**
	 * @param direccionpartida the direccionpartida to set
	 */
	public void setDireccionpartida(String direccionpartida) {
		this.direccionpartida = direccionpartida;
	}

	/**
	 * @return the periodofiscal
	 */
	public String getPeriodofiscal() {
		return periodofiscal;
	}

	/**
	 * @param periodofiscal the periodofiscal to set
	 */
	public void setPeriodofiscal(String periodofiscal) {
		this.periodofiscal = periodofiscal;
	}

	/**
	 * @return the placa
	 */
	public String getPlaca() {
		return placa;
	}

	/**
	 * @param placa the placa to set
	 */
	public void setPlaca(String placa) {
		this.placa = placa;
	}

	/**
	 * @return the estadoautorizacion
	 */
	public String getEstadoautorizacion() {
		return estadoautorizacion;
	}

	/**
	 * @param estadoautorizacion the estadoautorizacion to set
	 */
	public void setEstadoautorizacion(String estadoautorizacion) {
		this.estadoautorizacion = estadoautorizacion;
	}

	/**
	 * @return the fechaautorizacion
	 */
	public Date getFechaautorizacion() {
		return fechaautorizacion;
	}

	/**
	 * @param fechaautorizacion the fechaautorizacion to set
	 */
	public void setFechaautorizacion(Date fechaautorizacion) {
		this.fechaautorizacion = fechaautorizacion;
	}

	/**
	 * @return the errorautorizacion
	 */
	public String getErrorautorizacion() {
		return errorautorizacion;
	}

	/**
	 * @param errorautorizacion the errorautorizacion to set
	 */
	public void setErrorautorizacion(String errorautorizacion) {
		this.errorautorizacion = errorautorizacion;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the idusuario
	 */
	public String getIdusuario() {
		return idusuario;
	}

	/**
	 * @param idusuario the idusuario to set
	 */
	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the establecimiento
	 */
	public Establecimiento getEstablecimiento() {
		return establecimiento;
	}

	/**
	 * @param establecimiento the establecimiento to set
	 */
	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	/**
	 * @return the tipocomprobante
	 */
	public Tipocomprobante getTipocomprobante() {
		return tipocomprobante;
	}

	/**
	 * @param tipocomprobante the tipocomprobante to set
	 */
	public void setTipocomprobante(Tipocomprobante tipocomprobante) {
		this.tipocomprobante = tipocomprobante;
	}

	/**
	 * @return the motivoList
	 */
	public List<Motivo> getMotivoList() {
		return motivoList;
	}

	/**
	 * @param motivoList the motivoList to set
	 */
	public void setMotivoList(List<Motivo> motivoList) {
		this.motivoList = motivoList;
	}

	/**
	 * @return the totalimpuestoList
	 */
	public List<Totalimpuesto> getTotalimpuestoList() {
		return totalimpuestoList;
	}

	/**
	 * @param totalimpuestoList the totalimpuestoList to set
	 */
	public void setTotalimpuestoList(List<Totalimpuesto> totalimpuestoList) {
		this.totalimpuestoList = totalimpuestoList;
	}

	/**
	 * @return the destinatarioList
	 */
	public List<Destinatario> getDestinatarioList() {
		return destinatarioList;
	}

	/**
	 * @param destinatarioList the destinatarioList to set
	 */
	public void setDestinatarioList(List<Destinatario> destinatarioList) {
		this.destinatarioList = destinatarioList;
	}

	/**
	 * @return the detalleList
	 */
	public List<Detalle> getDetalleList() {
		return detalleList;
	}

	/**
	 * @param detalleList the detalleList to set
	 */
	public void setDetalleList(List<Detalle> detalleList) {
		this.detalleList = detalleList;
	}

	/**
	 * @return the infoadicionalList
	 */
	public List<Infoadicional> getInfoadicionalList() {
		return infoadicionalList;
	}

	/**
	 * @param infoadicionalList the infoadicionalList to set
	 */
	public void setInfoadicionalList(List<Infoadicional> infoadicionalList) {
		this.infoadicionalList = infoadicionalList;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * @return
	 */
	public String getEstadoStyle() {
		
		return ComprobanteEstadoEnum.getStyleEstado(estado);
		
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the pagoList
	 */
	public List<Pago> getPagoList() {
		return pagoList;
	}

	/**
	 * @param pagoList the pagoList to set
	 */
	public void setPagoList(List<Pago> pagoList) {
		this.pagoList = pagoList;
	}

	/**
	 * @return the detalleEliminarList
	 */
	public List<Detalle> getDetalleEliminarList() {
		return detalleEliminarList;
	}

	/**
	 * @param detalleEliminarList the detalleEliminarList to set
	 */
	public void setDetalleEliminarList(List<Detalle> detalleEliminarList) {
		this.detalleEliminarList = detalleEliminarList;
	}

	/**
	 * @return the adquisicion
	 */
	public Adquisicion getAdquisicion() {
		return adquisicion;
	}

	/**
	 * @param adquisicion the adquisicion to set
	 */
	public void setAdquisicion(Adquisicion adquisicion) {
		this.adquisicion = adquisicion;
	}

	/**
	 * @return the numfactura
	 */
	public String getNumfactura() {
		return numfactura;
	}

	/**
	 * @param numfactura the numfactura to set
	 */
	public void setNumfactura(String numfactura) {
		this.numfactura = numfactura;
	}

	/**
	 * @return the numdocumento
	 */
	public String getNumdocumento() {
		return numdocumento;
	}

	/**
	 * @param numdocumento the numdocumento to set
	 */
	public void setNumdocumento(String numdocumento) {
		this.numdocumento = numdocumento;
	}

	/**
	 * @return the totalbaseimponible
	 */
	public BigDecimal getTotalbaseimponible() {
		return totalbaseimponible;
	}

	/**
	 * @param totalbaseimponible the totalbaseimponible to set
	 */
	public void setTotalbaseimponible(BigDecimal totalbaseimponible) {
		this.totalbaseimponible = totalbaseimponible;
	}

	/**
	 * @return the totalretencion
	 */
	public BigDecimal getTotalretencion() {
		return totalretencion;
	}

	/**
	 * @param totalretencion the totalretencion to set
	 */
	public void setTotalretencion(BigDecimal totalretencion) {
		this.totalretencion = totalretencion;
	}

	/**
	 * @return the impuestoretencionList
	 */
	public List<Impuestoretencion> getImpuestoretencionList() {
		return impuestoretencionList;
	}

	/**
	 * @param impuestoretencionList the impuestoretencionList to set
	 */
	public void setImpuestoretencionList(List<Impuestoretencion> impuestoretencionList) {
		this.impuestoretencionList = impuestoretencionList;
	}

	/**
	 * @return the tipocomprobanteretencion
	 */
	public Tipocomprobante getTipocomprobanteretencion() {
		return tipocomprobanteretencion;
	}

	/**
	 * @param tipocomprobanteretencion the tipocomprobanteretencion to set
	 */
	public void setTipocomprobanteretencion(Tipocomprobante tipocomprobanteretencion) {
		this.tipocomprobanteretencion = tipocomprobanteretencion;
	}

	/**
	 * @return the numdocasociado
	 */
	public String getNumdocasociado() {
		return numdocasociado;
	}

	/**
	 * @param numdocasociado the numdocasociado to set
	 */
	public void setNumdocasociado(String numdocasociado) {
		this.numdocasociado = numdocasociado;
	}

	/**
	 * @return the idcabecerapadre
	 */
	public String getIdcabecerapadre() {
		return idcabecerapadre;
	}

	/**
	 * @param idcabecerapadre the idcabecerapadre to set
	 */
	public void setIdcabecerapadre(String idcabecerapadre) {
		this.idcabecerapadre = idcabecerapadre;
	}

	/**
	 * @return the transportista
	 */
	public Transportista getTransportista() {
		return transportista;
	}

	/**
	 * @param transportista the transportista to set
	 */
	public void setTransportista(Transportista transportista) {
		this.transportista = transportista;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the idguiaremision
	 */
	public String getIdguiaremision() {
		return idguiaremision;
	}

	/**
	 * @param idguiaremision the idguiaremision to set
	 */
	public void setIdguiaremision(String idguiaremision) {
		this.idguiaremision = idguiaremision;
	}

	/**
	 * @return the secuencialEstablecimiento
	 */
	public String getSecuencialEstablecimiento() {
		return secuencialEstablecimiento;
	}

	/**
	 * @param secuencialEstablecimiento the secuencialEstablecimiento to set
	 */
	public void setSecuencialEstablecimiento(String secuencialEstablecimiento) {
		this.secuencialEstablecimiento = secuencialEstablecimiento;
	}

	/**
	 * @return the secuencialCaja
	 */
	public String getSecuencialCaja() {
		return secuencialCaja;
	}

	/**
	 * @param secuencialCaja the secuencialCaja to set
	 */
	public void setSecuencialCaja(String secuencialCaja) {
		this.secuencialCaja = secuencialCaja;
	}

	/**
	 * @return the secuencialNumero
	 */
	public String getSecuencialNumero() {
		return secuencialNumero;
	}

	/**
	 * @param secuencialNumero the secuencialNumero to set
	 */
	public void setSecuencialNumero(String secuencialNumero) {
		this.secuencialNumero = secuencialNumero;
	}

	/**
	 * @return the editarSecuencial
	 */
	public boolean isEditarSecuencial() {
		return editarSecuencial;
	}

	/**
	 * @param editarSecuencial the editarSecuencial to set
	 */
	public void setEditarSecuencial(boolean editarSecuencial) {
		this.editarSecuencial = editarSecuencial;
	}

	/**
	 * @return the genTipoDocumentoEnum
	 */
	public GenTipoDocumentoEnum getGenTipoDocumentoEnum() {
		return genTipoDocumentoEnum;
	}

	/**
	 * @param genTipoDocumentoEnum the genTipoDocumentoEnum to set
	 */
	public void setGenTipoDocumentoEnum(GenTipoDocumentoEnum genTipoDocumentoEnum) {
		this.genTipoDocumentoEnum = genTipoDocumentoEnum;
	}

	/**
	 * @return the valorretenidoiva
	 */
	public BigDecimal getValorretenidoiva() {
		return valorretenidoiva;
	}

	/**
	 * @param valorretenidoiva the valorretenidoiva to set
	 */
	public void setValorretenidoiva(BigDecimal valorretenidoiva) {
		this.valorretenidoiva = valorretenidoiva;
	}

	/**
	 * @return the valorretenidorenta
	 */
	public BigDecimal getValorretenidorenta() {
		return valorretenidorenta;
	}

	/**
	 * @param valorretenidorenta the valorretenidorenta to set
	 */
	public void setValorretenidorenta(BigDecimal valorretenidorenta) {
		this.valorretenidorenta = valorretenidorenta;
	}

	/**
	 * @return the valorretenido
	 */
	public BigDecimal getValorretenido() {
		return valorretenido;
	}

	/**
	 * @param valorretenido the valorretenido to set
	 */
	public void setValorretenido(BigDecimal valorretenido) {
		this.valorretenido = valorretenido;
	}

	/**
	 * @return the totalpagar
	 */
	public BigDecimal getTotalpagar() {
		return totalpagar;
	}

	/**
	 * @param totalpagar the totalpagar to set
	 */
	public void setTotalpagar(BigDecimal totalpagar) {
		this.totalpagar = totalpagar;
	}

	/**
	 * @return the envioemail
	 */
	public Integer getEnvioemail() {
		return envioemail;
	}

	/**
	 * @param envioemail the envioemail to set
	 */
	public void setEnvioemail(Integer envioemail) {
		this.envioemail = envioemail;
	}

	/**
	 * @return the resumenpago
	 */
	public String getResumenpago() {
		return resumenpago;
	}

	/**
	 * @param resumenpago the resumenpago to set
	 */
	public void setResumenpago(String resumenpago) {
		this.resumenpago = resumenpago;
	}

	/**
	 * @return the valorapagar
	 */
	public BigDecimal getValorapagar() {
		return valorapagar;
	}

	/**
	 * @param valorapagar the valorapagar to set
	 */
	public void setValorapagar(BigDecimal valorapagar) {
		this.valorapagar = valorapagar;
	}
	
	public BigDecimal getTotalPagadoSum() {
		if(pagoList!=null && !pagoList.isEmpty()) {
			return BigDecimal.valueOf(pagoList.stream().mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
		}
		return BigDecimal.ZERO;
	}

	/**
	 * @return the resumen
	 */
	public String getResumen() {
		return resumen;
	}

	/**
	 * @param resumen the resumen to set
	 */
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	/**
	 * @return the contenido1
	 */
	public String getContenido1() {
		return contenido1;
	}

	/**
	 * @param contenido1 the contenido1 to set
	 */
	public void setContenido1(String contenido1) {
		this.contenido1 = contenido1;
	}

	/**
	 * @return the contenido2
	 */
	public String getContenido2() {
		return contenido2;
	}

	/**
	 * @param contenido2 the contenido2 to set
	 */
	public void setContenido2(String contenido2) {
		this.contenido2 = contenido2;
	}

	/**
	 * @return the fechaVencimiento
	 */
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	/**
	 * @return the envioEmailBol
	 */
	public boolean isEnvioEmailBol() {
		if(envioemail==null) {
			envioEmailBol = false;
		}else {
			envioEmailBol = envioemail==1;
		}
		return envioEmailBol;
	}

	/**
	 * @param envioEmailBol the envioEmailBol to set
	 */
	public void setEnvioEmailBol(boolean envioEmailBol) {
		this.envioemail = envioEmailBol?1:0;
		this.envioEmailBol = envioEmailBol;
	}

	/**
	 * @return the contenido3
	 */
	public String getContenido3() {
		return contenido3;
	}

	/**
	 * @param contenido3 the contenido3 to set
	 */
	public void setContenido3(String contenido3) {
		this.contenido3 = contenido3;
	}

	/**
	 * @return the borrador
	 */
	public boolean isBorrador() {
		return borrador;
	}

	/**
	 * @param borrador the borrador to set
	 */
	public void setBorrador(boolean borrador) {
		this.borrador = borrador;
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 */
	public boolean isComprobanteFactura() {
		if(idcabecera== null) {
			return false;
		}
		
		return !tipocomprobante.getIdentificador().equals(GenTipoDocumentoEnum.FACTURA.getIdentificador());
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 */
	public boolean isComprobanteRecibo() {
		if(idcabecera== null) {
			return false;
		}
		
		return !tipocomprobante.getIdentificador().equals(GenTipoDocumentoEnum.RECIBO.getIdentificador());
	}

	/**
	 * @return the idUsurioTransaccion
	 */
	public String getIdUsurioTransaccion() {
		return idUsurioTransaccion;
	}

	/**
	 * @param idUsurioTransaccion the idUsurioTransaccion to set
	 */
	public void setIdUsurioTransaccion(String idUsurioTransaccion) {
		this.idUsurioTransaccion = idUsurioTransaccion;
	}

	/**
	 * @return the seleccion
	 */
	public boolean isSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the usuarioconsulta
	 */
	public Usuario getUsuarioconsulta() {
		return usuarioconsulta;
	}

	/**
	 * @param usuarioconsulta the usuarioconsulta to set
	 */
	public void setUsuarioconsulta(Usuario usuarioconsulta) {
		this.usuarioconsulta = usuarioconsulta;
	}

	/**
	 * @return the idusuarioautorizacion
	 */
	public String getIdusuarioautorizacion() {
		return idusuarioautorizacion;
	}

	/**
	 * @param idusuarioautorizacion the idusuarioautorizacion to set
	 */
	public void setIdusuarioautorizacion(String idusuarioautorizacion) {
		this.idusuarioautorizacion = idusuarioautorizacion;
	}

	/**
	 * @return the autorizacionBol
	 */
	public boolean isAutorizacionBol() {
		
		if(autorizacion==null) {
			autorizacionBol = false;
		}else {
			autorizacionBol = autorizacion==1;
		}
		
		return autorizacionBol;
	}

	/**
	 * @param autorizacionBol the autorizacionBol to set
	 */
	public void setAutorizacionBol(boolean autorizacionBol) {
		
		this.autorizacion = autorizacionBol?1:0;
		this.autorizacionBol = autorizacionBol;
		
	}

	/**
	 * @return the autorizacion
	 */
	public Integer getAutorizacion() {
		return autorizacion;
	}

	/**
	 * @param autorizacion the autorizacion to set
	 */
	public void setAutorizacion(Integer autorizacion) {
		this.autorizacion = autorizacion;
	}

	/**
	 * @return the etiqueta
	 */
	public Etiqueta getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(Etiqueta etiqueta) {
		this.etiqueta = etiqueta;
	}

	/**
	 * @return the pathLogo
	 */
	public String getPathLogo() {
		return pathLogo;
	}

	/**
	 * @param pathLogo the pathLogo to set
	 */
	public void setPathLogo(String pathLogo) {
		this.pathLogo = pathLogo;
	}

	/**
	 * @return the totalComprobanteList
	 */
	public List<XmlTotalComprobante> getTotalComprobanteList() {
		return totalComprobanteList;
	}

	/**
	 * @param totalComprobanteList the totalComprobanteList to set
	 */
	public void setTotalComprobanteList(List<XmlTotalComprobante> totalComprobanteList) {
		this.totalComprobanteList = totalComprobanteList;
	}
	
	/**
	 * @return the clienteCompleto
	 */
	public String getClienteCompleto() {
		clienteCompleto = (cliente!=null?cliente.getIdentificacion().concat(cliente.getRazonsocial()):"");
		return clienteCompleto;
	}

	/**
	 * @param clienteCompleto the clienteCompleto to set
	 */
	public void setClienteCompleto(String clienteCompleto) {
		this.clienteCompleto = clienteCompleto;
	}

	/**
	 * @return the cantidadDestinatario
	 */
	public int getCantidadDestinatario() {
		return cantidadDestinatario;
	}

	/**
	 * @param cantidadDestinatario the cantidadDestinatario to set
	 */
	public void setCantidadDestinatario(int cantidadDestinatario) {
		this.cantidadDestinatario = cantidadDestinatario;
	}

	/**
	 * @return the regimenrimpe
	 */
	public Integer getRegimenrimpe() {
		return regimenrimpe;
	}

	/**
	 * @param regimenrimpe the regimenrimpe to set
	 */
	public void setRegimenrimpe(Integer regimenrimpe) {
		this.regimenrimpe = regimenrimpe;
	}

	/**
	 * @return the contenidoEmail
	 */
	public String getContenidoEmail() {
		return contenidoEmail;
	}

	/**
	 * @param contenidoEmail the contenidoEmail to set
	 */
	public void setContenidoEmail(String contenidoEmail) {
		this.contenidoEmail = contenidoEmail;
	}

	/**
	 * @return the otrosPagos
	 */
	public BigDecimal getOtrosPagos() {
		return otrosPagos;
	}

	/**
	 * @param otrosPagos the otrosPagos to set
	 */
	public void setOtrosPagos(BigDecimal otrosPagos) {
		this.otrosPagos = otrosPagos;
	}

	/**
	 * @return the totalCredito
	 */
	public BigDecimal getTotalCredito() {
		return totalCredito;
	}

	/**
	 * @param totalCredito the totalCredito to set
	 */
	public void setTotalCredito(BigDecimal totalCredito) {
		this.totalCredito = totalCredito;
	}

	/**
	 * @return the filterComprobanteTransportista
	 */
	public String getFilterComprobanteTransportista() {
		filterComprobanteTransportista = "";
		if(transportista != null) {
			filterComprobanteTransportista += transportista.getIdentificacion();
			filterComprobanteTransportista += transportista.getRazonsocial();
		}
		if(numdocumento != null) {
			filterComprobanteTransportista += numdocumento;
		}
		return filterComprobanteTransportista;
	}

	/**
	 * @param filterComprobanteTransportista the filterComprobanteTransportista to set
	 */
	public void setFilterComprobanteTransportista(String filterComprobanteTransportista) {
		this.filterComprobanteTransportista = filterComprobanteTransportista;
	}
}
