/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

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
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "idusuario", nullable = false, length = 40)
	private String idusuario;
    
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "idestablecimiento", nullable = false)
    @ManyToOne(optional = false)
    private Establecimiento establecimiento;
    
    @JoinColumn(name = "idproveedor", referencedColumnName = "idproveedor", nullable = false)
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    
    @JoinColumn(name = "idtipocomprobante", referencedColumnName = "idtipocomprobante", nullable = false)
    @ManyToOne(optional = false)
    private Tipocomprobante tipocomprobante;
    
    @JoinColumn(name = "idtipopago", referencedColumnName = "idtipopago", nullable = false)
    @ManyToOne(optional = false)
    private Tipopago tipopago;
    
	/**
	 * 
	 */
	public Adquisicion() {
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
	 * @return the tipopago
	 */
	public Tipopago getTipopago() {
		return tipopago;
	}

	/**
	 * @param tipopago the tipopago to set
	 */
	public void setTipopago(Tipopago tipopago) {
		this.tipopago = tipopago;
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
		switch (estado) {
		case "GEN":
			
			return "markGreen";
		case "ANU":
			
			return "markRed";
		case "ENV":
			
			return "markOrange";	
		default:
			return "markBlack";
		}
	}

}
