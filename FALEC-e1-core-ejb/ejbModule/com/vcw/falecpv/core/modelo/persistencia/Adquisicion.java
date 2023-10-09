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

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "adquisicion")
public class Adquisicion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5676761331710129362L;
	
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idadquisicion", nullable = false, length = 40)
    private String idadquisicion;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Size(max = 150)
    @Column(name = "autorizacion", length = 150)
    private String autorizacion;
    
    @Size(max = 50)
    @Column(name = "numfactura", length = 50)
    private String numfactura;
    
    @Size(max = 20)
    @Column(name = "estado", length = 50)
    private String estado;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totaliva", nullable = false, precision = 12, scale = 2)
    private BigDecimal totaliva;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totaldescuento", nullable = false, precision = 12, scale = 2)
    private BigDecimal totaldescuento;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalfactura", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalfactura;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalretencion", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalretencion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalpagar", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalpagar;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalice", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalice;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorretenidoiva", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorretenidoiva = BigDecimal.ZERO;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorretenidorenta", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorretenidorenta = BigDecimal.ZERO;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idusuario", nullable = false, length = 40)
	private String idusuario;
	
	
	@Column(name = "esgasto", nullable = false)
	private Integer esgasto;
    
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "idestablecimiento", nullable = false)
    @ManyToOne(optional = false)
    private Establecimiento establecimiento;
    
    @JoinColumn(name = "idcliente", referencedColumnName = "idcliente", nullable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;
    
    @JoinColumn(name = "idtipocomprobante", referencedColumnName = "idtipocomprobante", nullable = false)
    @ManyToOne(optional = false)
    private Tipocomprobante tipocomprobante;
    
    @Transient
    private List<Adquisiciondetalle> adquisiciondetalleList;
    
    @Transient
    private List<Pago> pagoList;
    
    @Transient
    private boolean esGastoBol;
    
    @Transient
    private BigDecimal otrosPagos = BigDecimal.ZERO;
    
    @Transient
    private BigDecimal totalCredito = BigDecimal.ZERO;
    
	/**
	 * 
	 */
	public Adquisicion() {
	}
	
	public BigDecimal getTotalPagoSum() {
		
		if(this.pagoList==null || this.pagoList.isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		return BigDecimal.valueOf(pagoList.stream().mapToDouble(p->p.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
		
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idadquisicion != null ? idadquisicion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Adquisicion)) {
            return false;
        }
        Adquisicion other = (Adquisicion) object;
        if ((this.idadquisicion == null && other.idadquisicion != null) || (this.idadquisicion != null && !this.idadquisicion.equals(other.idadquisicion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idadquisicion
	 */
	public String getIdadquisicion() {
		return idadquisicion;
	}

	/**
	 * @param idadquisicion the idadquisicion to set
	 */
	public void setIdadquisicion(String idadquisicion) {
		this.idadquisicion = idadquisicion;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the autorizacion
	 */
	public String getAutorizacion() {
		return autorizacion;
	}

	/**
	 * @param autorizacion the autorizacion to set
	 */
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
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
	 * @return the subtotal
	 */
	public BigDecimal getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
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
	 * @return the totalfactura
	 */
	public BigDecimal getTotalfactura() {
		return totalfactura;
	}

	/**
	 * @param totalfactura the totalfactura to set
	 */
	public void setTotalfactura(BigDecimal totalfactura) {
		this.totalfactura = totalfactura;
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
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
	 * @return
	 */
	public String getStyle() {
		try {
			return ComprobanteEstadoEnum.getStyleEstado(estado);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
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
	 * @return the adquisiciondetalleList
	 */
	public List<Adquisiciondetalle> getAdquisiciondetalleList() {
		return adquisiciondetalleList;
	}

	/**
	 * @param adquisiciondetalleList the adquisiciondetalleList to set
	 */
	public void setAdquisiciondetalleList(List<Adquisiciondetalle> adquisiciondetalleList) {
		this.adquisiciondetalleList = adquisiciondetalleList;
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
	 * @return the esgasto
	 */
	public Integer getEsgasto() {
		return esgasto;
	}

	/**
	 * @param esgasto the esgasto to set
	 */
	public void setEsgasto(Integer esgasto) {
		this.esgasto = esgasto;
	}

	public boolean getEsGastoBol() {
		if(this.esgasto==null) {
			esGastoBol = false;
			return false;
		}
		esGastoBol = this.esgasto==1?true:false;
		return this.esgasto==1?true:false;
	}
	
	/**
	 * @param esGastoBol the esGastoBol to set
	 */
	public void setEsGastoBol(boolean esGastoBol) {
		this.esGastoBol = esGastoBol;
		this.esgasto = esGastoBol?1:0;
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
	
}
