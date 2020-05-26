/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "numfactura", nullable = true, length = 15)
    private String numfactura;
    
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "numdocumento", nullable = true, length = 15)
    private String numdocumento;
    
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
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idproveedor", referencedColumnName = "idproveedor", nullable = true)
    private Proveedor proveedor;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalbaseimponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalbaseimponible = BigDecimal.ZERO;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalretencion", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalretencion = BigDecimal.ZERO;
    
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idtipocomprobanteretencion", referencedColumnName = "idtipocomprobante", nullable = true)
    private Tipocomprobante tipocomprobanteretencion;
    
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcabecera")
    @Transient
    private List<Motivo> motivoList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcabecera")
    @Transient
    private List<Totalimpuesto> totalimpuestoList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcabecera")
    @Transient
    private List<Destinatario> destinatarioList;
//    @OneToMany(mappedBy = "idcabecera")
    @Transient
    private List<Detalle> detalleList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcabecera")
    @Transient
    private List<Infoadicional> infoadicionalList;
    
    @Transient
    private List<Pago> pagoList;
    
    @Transient
    private List<Detalle> detalleEliminarList;
    
    @Transient
    private List<Impuestoretencion> impuestoretencionList;

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
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		return proveedor;
	}

	/**
	 * @param proveedor the proveedor to set
	 */
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
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

}
