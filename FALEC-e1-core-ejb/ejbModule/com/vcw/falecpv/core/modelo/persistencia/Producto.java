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

import org.hibernate.annotations.Type;

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
    @Column(name = "idproducto", nullable = false, length = 40)
    private String idproducto;
	
    @Basic(optional = true)
    @Column(name = "codigoprincipal", nullable = true, length = 25)
    private String codigoprincipal;
    
    @Size(max = 25)
    @Column(name = "codigoauxiliar", length = 25)
    private String codigoauxiliar;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Size(max = 100)
    @Column(name = "nombregenerico", length = 100)
    private String nombregenerico;
    
    @Size(max = 300)
    @Column(name = "descripcion", length = 300)
    private String descripcion;
    
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "imagen")
    private byte[] imagen;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciounitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal preciounitario;
    
    @Basic(optional = false)    
    @NotNull
    @Column(name = "porcentajedescuento", nullable = false, precision = 12, scale = 2)
    private BigDecimal porcentajedescuento;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciouno", nullable = false, precision = 12, scale = 2)
    private BigDecimal preciouno;
    
    @Column(name = "preciodos", precision = 12, scale = 2)
    private BigDecimal preciodos;
    
    @Column(name = "preciotres", precision = 12, scale = 2)
    private BigDecimal preciotres;
    
    @Column(name = "unidadesporcajaofrasco")
    private Integer unidadesporcajaofrasco;
    
    @Column(name = "unidadesporpaquete")
    private Integer unidadesporpaquete;
    
    @Column(name = "gramosporunidad", precision = 12, scale = 2)
    private BigDecimal gramosporunidad;
    
    @Column(name = "fechavencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechavencimiento;
    
    @Column(name = "fechafabricacion")
    @Temporal(TemporalType.DATE)
    private Date fechafabricacion;
    
    @Column(name = "fechacompra")
    @Temporal(TemporalType.DATE)
    private Date fechacompra;
    
    @Column(name = "stock")
    private Integer stock;
    
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "idusuario", nullable = false, length = 40)
    private String idusuario;
    
    @Column(name = "medida",length = 10)
    private String medida;
    
    @Column(name = "nombreimagen",length = 200)
    private String nombreimagen;
    
    @Column(name = "conversionmedida", precision = 10, scale = 5)
    private BigDecimal conversionmedida;
    
    @JoinColumn(name = "idcategoria", referencedColumnName = "idcategoria", nullable = false)
    @ManyToOne(optional = false)
    private Categoria categoria;
    
    @JoinColumn(name = "idfabricante", referencedColumnName = "idfabricante", nullable = false)
    @ManyToOne(optional = false)
    private Fabricante fabricante;
    
    @JoinColumn(name = "idice", referencedColumnName = "idice")
    @ManyToOne
    private Ice ice;
    
    @JoinColumn(name = "idiva", referencedColumnName = "idiva", nullable = false)
    @ManyToOne(optional = false)
    private Iva iva;
    
    @JoinColumn(name = "idtipoproducto", referencedColumnName = "idtipoproducto", nullable = false)
    @ManyToOne(optional = false)
    private TipoProducto tipoProducto;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "idestablecimiento", nullable = false)
    private Establecimiento establecimiento;
    

	/**
	 * 
	 */
	public Producto() {
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idproducto != null ? idproducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.idproducto == null && other.idproducto != null) || (this.idproducto != null && !this.idproducto.equals(other.idproducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.format("%s[id=%s]",nombregenerico , idproducto);
    }
    
    public String toStringObject() {
        return PojoUtil.toString(this);
    }

	/**
	 * @return the idproducto
	 */
	public String getIdproducto() {
		return idproducto;
	}

	/**
	 * @param idproducto the idproducto to set
	 */
	public void setIdproducto(String idproducto) {
		this.idproducto = idproducto;
	}

	/**
	 * @return the codigoprincipal
	 */
	public String getCodigoprincipal() {
		return codigoprincipal;
	}

	/**
	 * @param codigoprincipal the codigoprincipal to set
	 */
	public void setCodigoprincipal(String codigoprincipal) {
		this.codigoprincipal = codigoprincipal;
	}

	/**
	 * @return the codigoauxiliar
	 */
	public String getCodigoauxiliar() {
		return codigoauxiliar;
	}

	/**
	 * @param codigoauxiliar the codigoauxiliar to set
	 */
	public void setCodigoauxiliar(String codigoauxiliar) {
		this.codigoauxiliar = codigoauxiliar;
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
	 * @return the nombregenerico
	 */
	public String getNombregenerico() {
		return nombregenerico;
	}

	/**
	 * @param nombregenerico the nombregenerico to set
	 */
	public void setNombregenerico(String nombregenerico) {
		this.nombregenerico = nombregenerico;
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
	 * @return the porcentajedescuento
	 */
	public BigDecimal getPorcentajedescuento() {
		return porcentajedescuento;
	}

	/**
	 * @param porcentajedescuento the porcentajedescuento to set
	 */
	public void setPorcentajedescuento(BigDecimal porcentajedescuento) {
		this.porcentajedescuento = porcentajedescuento;
	}

	/**
	 * @return the preciouno
	 */
	public BigDecimal getPreciouno() {
		return preciouno;
	}

	/**
	 * @param preciouno the preciouno to set
	 */
	public void setPreciouno(BigDecimal preciouno) {
		this.preciouno = preciouno;
	}

	/**
	 * @return the preciodos
	 */
	public BigDecimal getPreciodos() {
		return preciodos;
	}

	/**
	 * @param preciodos the preciodos to set
	 */
	public void setPreciodos(BigDecimal preciodos) {
		this.preciodos = preciodos;
	}

	/**
	 * @return the preciotres
	 */
	public BigDecimal getPreciotres() {
		return preciotres;
	}

	/**
	 * @param preciotres the preciotres to set
	 */
	public void setPreciotres(BigDecimal preciotres) {
		this.preciotres = preciotres;
	}

	/**
	 * @return the unidadesporcajaofrasco
	 */
	public Integer getUnidadesporcajaofrasco() {
		return unidadesporcajaofrasco;
	}

	/**
	 * @param unidadesporcajaofrasco the unidadesporcajaofrasco to set
	 */
	public void setUnidadesporcajaofrasco(Integer unidadesporcajaofrasco) {
		this.unidadesporcajaofrasco = unidadesporcajaofrasco;
	}

	/**
	 * @return the unidadesporpaquete
	 */
	public Integer getUnidadesporpaquete() {
		return unidadesporpaquete;
	}

	/**
	 * @param unidadesporpaquete the unidadesporpaquete to set
	 */
	public void setUnidadesporpaquete(Integer unidadesporpaquete) {
		this.unidadesporpaquete = unidadesporpaquete;
	}

	/**
	 * @return the gramosporunidad
	 */
	public BigDecimal getGramosporunidad() {
		return gramosporunidad;
	}

	/**
	 * @param gramosporunidad the gramosporunidad to set
	 */
	public void setGramosporunidad(BigDecimal gramosporunidad) {
		this.gramosporunidad = gramosporunidad;
	}

	/**
	 * @return the fechavencimiento
	 */
	public Date getFechavencimiento() {
		return fechavencimiento;
	}

	/**
	 * @param fechavencimiento the fechavencimiento to set
	 */
	public void setFechavencimiento(Date fechavencimiento) {
		this.fechavencimiento = fechavencimiento;
	}

	/**
	 * @return the fechafabricacion
	 */
	public Date getFechafabricacion() {
		return fechafabricacion;
	}

	/**
	 * @param fechafabricacion the fechafabricacion to set
	 */
	public void setFechafabricacion(Date fechafabricacion) {
		this.fechafabricacion = fechafabricacion;
	}

	/**
	 * @return the fechacompra
	 */
	public Date getFechacompra() {
		return fechacompra;
	}

	/**
	 * @param fechacompra the fechacompra to set
	 */
	public void setFechacompra(Date fechacompra) {
		this.fechacompra = fechacompra;
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
	 * @return the medida
	 */
	public String getMedida() {
		return medida;
	}

	/**
	 * @param medida the medida to set
	 */
	public void setMedida(String medida) {
		this.medida = medida;
	}

	/**
	 * @return the conversionmedida
	 */
	public BigDecimal getConversionmedida() {
		return conversionmedida;
	}

	/**
	 * @param conversionmedida the conversionmedida to set
	 */
	public void setConversionmedida(BigDecimal conversionmedida) {
		this.conversionmedida = conversionmedida;
	}

	/**
	 * @return the nombreimagen
	 */
	public String getNombreimagen() {
		return nombreimagen;
	}

	/**
	 * @param nombreimagen the nombreimagen to set
	 */
	public void setNombreimagen(String nombreimagen) {
		this.nombreimagen = nombreimagen;
	}

	

}
