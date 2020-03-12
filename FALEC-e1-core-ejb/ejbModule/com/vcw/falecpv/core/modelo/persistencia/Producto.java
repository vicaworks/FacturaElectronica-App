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
import javax.persistence.Lob;
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
@Table(name = "producto")
public class Producto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2977091353156150858L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idProducto", nullable = false, length = 40)
    private String idProducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "codigoPrincipal", nullable = false, length = 25)
    private String codigoPrincipal;
    @Size(max = 25)
    @Column(name = "codigoAuxiliar", length = 25)
    private String codigoAuxiliar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Size(max = 100)
    @Column(name = "nombreGenerico", length = 100)
    private String nombreGenerico;
    @Size(max = 300)
    @Column(name = "descripcion", length = 300)
    private String descripcion;
    @Lob
    @Column(name = "imagen")
    private byte[] imagen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precioUnitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentajeDescuento", nullable = false, precision = 12, scale = 2)
    private BigDecimal porcentajeDescuento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precioUno", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUno;
    @Column(name = "precioDos", precision = 12, scale = 2)
    private BigDecimal precioDos;
    @Column(name = "precioTres", precision = 12, scale = 2)
    private BigDecimal precioTres;
    @Column(name = "unidadesPorCajaOFrasco")
    private Integer unidadesPorCajaOFrasco;
    @Column(name = "unidadesPorPaquete")
    private Integer unidadesPorPaquete;
    @Column(name = "gramosPorUnidad", precision = 12, scale = 2)
    private BigDecimal gramosPorUnidad;
    @Column(name = "fechaVencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "fechaFabricacion")
    @Temporal(TemporalType.DATE)
    private Date fechaFabricacion;
    @Column(name = "fechaCompra")
    @Temporal(TemporalType.DATE)
    private Date fechaCompra;
    @Column(name = "stock")
    private Integer stock;
    @Size(max = 1)
    @Column(name = "tipo", length = 1)
    private String tipo;
    @Size(max = 800)
    @Column(name = "observacion", length = 800)
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date updated;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idUsuario", nullable = false, length = 40)
    private String idUsuario;
    
    @JoinColumn(name = "idCategoria", referencedColumnName = "idCategoria", nullable = false,insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private Categoria categoria;
    
    @JoinColumn(name = "idFabricante", referencedColumnName = "idFabricante", nullable = false,insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private Fabricante fabricante;
    
    @JoinColumn(name = "idIce", referencedColumnName = "idIce",insertable = false,updatable = false)
    @ManyToOne
    private Ice ice;
    
    @JoinColumn(name = "idIva", referencedColumnName = "idIva", nullable = false,insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private Iva iva;
    
    @JoinColumn(name = "idTipoProducto", referencedColumnName = "idTipoProducto", nullable = false,insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private TipoProducto tipoProducto;

	/**
	 * 
	 */
	public Producto() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idProducto
	 */
	public String getIdProducto() {
		return idProducto;
	}

	/**
	 * @param idProducto the idProducto to set
	 */
	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}

	/**
	 * @return the codigoPrincipal
	 */
	public String getCodigoPrincipal() {
		return codigoPrincipal;
	}

	/**
	 * @param codigoPrincipal the codigoPrincipal to set
	 */
	public void setCodigoPrincipal(String codigoPrincipal) {
		this.codigoPrincipal = codigoPrincipal;
	}

	/**
	 * @return the codigoAuxiliar
	 */
	public String getCodigoAuxiliar() {
		return codigoAuxiliar;
	}

	/**
	 * @param codigoAuxiliar the codigoAuxiliar to set
	 */
	public void setCodigoAuxiliar(String codigoAuxiliar) {
		this.codigoAuxiliar = codigoAuxiliar;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the nombreGenerico
	 */
	public String getNombreGenerico() {
		return nombreGenerico;
	}

	/**
	 * @param nombreGenerico the nombreGenerico to set
	 */
	public void setNombreGenerico(String nombreGenerico) {
		this.nombreGenerico = nombreGenerico;
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
	 * @return the imagen
	 */
	public byte[] getImagen() {
		return imagen;
	}

	/**
	 * @param imagen the imagen to set
	 */
	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	/**
	 * @return the precioUnitario
	 */
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	/**
	 * @param precioUnitario the precioUnitario to set
	 */
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
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
	 * @return the precioUno
	 */
	public BigDecimal getPrecioUno() {
		return precioUno;
	}

	/**
	 * @param precioUno the precioUno to set
	 */
	public void setPrecioUno(BigDecimal precioUno) {
		this.precioUno = precioUno;
	}

	/**
	 * @return the precioDos
	 */
	public BigDecimal getPrecioDos() {
		return precioDos;
	}

	/**
	 * @param precioDos the precioDos to set
	 */
	public void setPrecioDos(BigDecimal precioDos) {
		this.precioDos = precioDos;
	}

	/**
	 * @return the precioTres
	 */
	public BigDecimal getPrecioTres() {
		return precioTres;
	}

	/**
	 * @param precioTres the precioTres to set
	 */
	public void setPrecioTres(BigDecimal precioTres) {
		this.precioTres = precioTres;
	}

	/**
	 * @return the unidadesPorCajaOFrasco
	 */
	public Integer getUnidadesPorCajaOFrasco() {
		return unidadesPorCajaOFrasco;
	}

	/**
	 * @param unidadesPorCajaOFrasco the unidadesPorCajaOFrasco to set
	 */
	public void setUnidadesPorCajaOFrasco(Integer unidadesPorCajaOFrasco) {
		this.unidadesPorCajaOFrasco = unidadesPorCajaOFrasco;
	}

	/**
	 * @return the unidadesPorPaquete
	 */
	public Integer getUnidadesPorPaquete() {
		return unidadesPorPaquete;
	}

	/**
	 * @param unidadesPorPaquete the unidadesPorPaquete to set
	 */
	public void setUnidadesPorPaquete(Integer unidadesPorPaquete) {
		this.unidadesPorPaquete = unidadesPorPaquete;
	}

	/**
	 * @return the gramosPorUnidad
	 */
	public BigDecimal getGramosPorUnidad() {
		return gramosPorUnidad;
	}

	/**
	 * @param gramosPorUnidad the gramosPorUnidad to set
	 */
	public void setGramosPorUnidad(BigDecimal gramosPorUnidad) {
		this.gramosPorUnidad = gramosPorUnidad;
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
	 * @return the fechaFabricacion
	 */
	public Date getFechaFabricacion() {
		return fechaFabricacion;
	}

	/**
	 * @param fechaFabricacion the fechaFabricacion to set
	 */
	public void setFechaFabricacion(Date fechaFabricacion) {
		this.fechaFabricacion = fechaFabricacion;
	}

	/**
	 * @return the fechaCompra
	 */
	public Date getFechaCompra() {
		return fechaCompra;
	}

	/**
	 * @param fechaCompra the fechaCompra to set
	 */
	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	/**
	 * @return the stock
	 */
	public Integer getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
	 * @return the idUsuario
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the categoria
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	/**
	 * @return the fabricante
	 */
	public Fabricante getFabricante() {
		return fabricante;
	}

	/**
	 * @param fabricante the fabricante to set
	 */
	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
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
	 * @return the tipoProducto
	 */
	public TipoProducto getTipoProducto() {
		return tipoProducto;
	}

	/**
	 * @param tipoProducto the tipoProducto to set
	 */
	public void setTipoProducto(TipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

}
