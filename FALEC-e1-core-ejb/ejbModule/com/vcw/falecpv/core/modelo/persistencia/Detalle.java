/**
 * 
 */
package com.vcw.falecpv.core.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
@Entity
@Table(name = "detalle")
public class Detalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1656236465825874758L;

	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "iddetalle", nullable = false, length = 40)
    private String iddetalle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descuento", nullable = false, precision = 12, scale = 2)
    private BigDecimal descuento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciototalsinimpuesto", nullable = false, precision = 12, scale = 2)
    private BigDecimal preciototalsinimpuesto;
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciototal")
    private BigDecimal preciototal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciounitario")
    private BigDecimal preciounitario;    
    @NotNull
    @Column(name = "preciocompra")
    private BigDecimal preciocompra = BigDecimal.ZERO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorice")
    private BigDecimal valorice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valoriva")
    private BigDecimal valoriva;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentajedescuento")
    private BigDecimal porcentajeDescuento = BigDecimal.ZERO;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "descripcion")
    private String descripcion;
    @NotNull
    @Column(name = "codproducto")
    private String codproducto;
    @ManyToOne(optional = true)
    @JoinColumn(name = "idice", referencedColumnName = "idice", nullable = true)
    private Ice ice;    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idiva", referencedColumnName = "idiva", nullable = true)
    private Iva iva;
    
    
    @JoinColumn(name = "idcabecera", referencedColumnName = "idcabecera")
    @ManyToOne
    private Cabecera cabecera;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "idproducto", referencedColumnName = "idproducto", nullable = true)
    private Producto producto;
    
    @Transient
    private String precioOpcionSeleccion;
    
    @Transient
    private List<Detalleimpuesto> detalleimpuestoList;
	
    @Transient
    private String idUsuarioEliminacion;
    
    @Transient
    private String accion;
    
    @Transient
    private Integer precioVenta = 1;
    
    @Transient
    private boolean flagCalcularDetForm = false;
    
	/**
	 * 
	 */
	public Detalle() {
	}
	
	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (iddetalle != null ? iddetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Detalle)) {
            return false;
        }
        Detalle other = (Detalle) object;
        if ((this.iddetalle == null && other.iddetalle != null) || (this.iddetalle != null && !this.iddetalle.equals(other.iddetalle))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the iddetalle
	 */
	public String getIddetalle() {
		return iddetalle;
	}

	/**
	 * @param iddetalle the iddetalle to set
	 */
	public void setIddetalle(String iddetalle) {
		this.iddetalle = iddetalle;
	}

	/**
	 * @return the cantidad
	 */
	public BigDecimal getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the descuento
	 */
	public BigDecimal getDescuento() {
		return descuento;
	}

	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	/**
	 * @return the preciototalsinimpuesto
	 */
	public BigDecimal getPreciototalsinimpuesto() {
		return preciototalsinimpuesto;
	}

	/**
	 * @param preciototalsinimpuesto the preciototalsinimpuesto to set
	 */
	public void setPreciototalsinimpuesto(BigDecimal preciototalsinimpuesto) {
		this.preciototalsinimpuesto = preciototalsinimpuesto;
	}

	/**
	 * @return the cabecera
	 */
	public Cabecera getCabecera() {
		return cabecera;
	}

	/**
	 * @param cabecera the cabecera to set
	 */
	public void setCabecera(Cabecera cabecera) {
		this.cabecera = cabecera;
	}

	/**
	 * @return the producto
	 */
	public Producto getProducto() {
		return producto;
	}

	/**
	 * @param producto the producto to set
	 */
	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the preciounitario
	 */
	public BigDecimal getPreciounitario() {
		return preciounitario;
	}

	/**
	 * @param preciounitario the preciounitario to set
	 */
	public void setPreciounitario(BigDecimal preciounitario) {
		this.preciounitario = preciounitario;
	}

	/**
	 * @return the valorice
	 */
	public BigDecimal getValorice() {
		return valorice;
	}

	/**
	 * @param valorice the valorice to set
	 */
	public void setValorice(BigDecimal valorice) {
		this.valorice = valorice;
	}

	/**
	 * @return the valoriva
	 */
	public BigDecimal getValoriva() {
		return valoriva;
	}

	/**
	 * @param valoriva the valoriva to set
	 */
	public void setValoriva(BigDecimal valoriva) {
		this.valoriva = valoriva;
	}

	/**
	 * @return the preciototal
	 */
	public BigDecimal getPreciototal() {
		return preciototal;
	}

	/**
	 * @param preciototal the preciototal to set
	 */
	public void setPreciototal(BigDecimal preciototal) {
		this.preciototal = preciototal;
	}

	/**
	 * @return the precioOpcionSeleccion
	 */
	public String getPrecioOpcionSeleccion() {
		return precioOpcionSeleccion;
	}

	/**
	 * @param precioOpcionSeleccion the precioOpcionSeleccion to set
	 */
	public void setPrecioOpcionSeleccion(String precioOpcionSeleccion) {
		this.precioOpcionSeleccion = precioOpcionSeleccion;
	}

	/**
	 * @return the ice
	 */
	public Ice getIce() {
		return ice;
	}

	/**
	 * @param ice the ice to set
	 */
	public void setIce(Ice ice) {
		this.ice = ice;
	}

	/**
	 * @return the iva
	 */
	public Iva getIva() {
		return iva;
	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(Iva iva) {
		this.iva = iva;
	}

	/**
	 * @return the detalleimpuestoList
	 */
	public List<Detalleimpuesto> getDetalleimpuestoList() {
		return detalleimpuestoList;
	}

	/**
	 * @param detalleimpuestoList the detalleimpuestoList to set
	 */
	public void setDetalleimpuestoList(List<Detalleimpuesto> detalleimpuestoList) {
		this.detalleimpuestoList = detalleimpuestoList;
	}

	/**
	 * @return the idUsuarioEliminacion
	 */
	public String getIdUsuarioEliminacion() {
		return idUsuarioEliminacion;
	}

	/**
	 * @param idUsuarioEliminacion the idUsuarioEliminacion to set
	 */
	public void setIdUsuarioEliminacion(String idUsuarioEliminacion) {
		this.idUsuarioEliminacion = idUsuarioEliminacion;
	}

	/**
	 * @return the accion
	 */
	public String getAccion() {
		return accion;
	}

	/**
	 * @param accion the accion to set
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}

	/**
	 * @return the codproducto
	 */
	public String getCodproducto() {
		return codproducto;
	}

	/**
	 * @param codproducto the codproducto to set
	 */
	public void setCodproducto(String codproducto) {
		this.codproducto = codproducto;
	}

	/**
	 * @return the porcentajeDescuento
	 */
	public BigDecimal getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	/**
	 * @param porcentajeDescuento the porcentajeDescuento to set
	 */
	public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	/**
	 * @return the precioVenta
	 */
	public Integer getPrecioVenta() {
		return precioVenta;
	}

	/**
	 * @param precioVenta the precioVenta to set
	 */
	public void setPrecioVenta(Integer precioVenta) {
		this.precioVenta = precioVenta;
	}

	/**
	 * @return the preciocompra
	 */
	public BigDecimal getPreciocompra() {
		return preciocompra;
	}

	/**
	 * @param preciocompra the preciocompra to set
	 */
	public void setPreciocompra(BigDecimal preciocompra) {
		this.preciocompra = preciocompra;
	}

	/**
	 * @return the flagCalcularDetForm
	 */
	public boolean isFlagCalcularDetForm() {
		return flagCalcularDetForm;
	}

	/**
	 * @param flagCalcularDetForm the flagCalcularDetForm to set
	 */
	public void setFlagCalcularDetForm(boolean flagCalcularDetForm) {
		this.flagCalcularDetForm = flagCalcularDetForm;
	}

	
}
