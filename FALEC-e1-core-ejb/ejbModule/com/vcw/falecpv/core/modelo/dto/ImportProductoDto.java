/**
 * 
 */
package com.vcw.falecpv.core.modelo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.servitec.common.util.PojoUtil;

/**
 * @author cristianvillarreal
 *
 */
public class ImportProductoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5319671072016809164L;
	
	private int fila;
	private String idFabricante;
	private String fabricante;
	private String idCategoria;
	private String categoria;
	private String idTipoProducto;
	private String tipoProducto;
	private String nombre;
	private String nombreComercial;
	private String codigoPrincipal;
	private String descripcion;
	private BigDecimal precioUnitario;
	private BigDecimal iva;
	private BigDecimal ice;
	private BigDecimal precio1;
	private BigDecimal precio2;
	private BigDecimal precio3;
	private BigDecimal descuento;
	private BigDecimal unidadesCaja;
	private BigDecimal unidadesEmbase;
	private String medida;
	private BigDecimal conversionMedida;
	private Date fechaFabricacion;
	private String nombreImagen;
	private String imagen;
	private boolean error = false;
	private String novedad;
	
	/**
	 * 
	 */
	public ImportProductoDto() {
	}
	
	

	/**
	 * @return the fila
	 */
	public int getFila() {
		return fila;
	}

	/**
	 * @param fila the fila to set
	 */
	public void setFila(int fila) {
		this.fila = fila;
	}

	/**
	 * @return the idFabricante
	 */
	public String getIdFabricante() {
		return idFabricante;
	}

	/**
	 * @param idFabricante the idFabricante to set
	 */
	public void setIdFabricante(String idFabricante) {
		this.idFabricante = idFabricante;
	}

	/**
	 * @return the fabricante
	 */
	public String getFabricante() {
		return fabricante;
	}

	/**
	 * @param fabricante the fabricante to set
	 */
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	/**
	 * @return the idCategoria
	 */
	public String getIdCategoria() {
		return idCategoria;
	}

	/**
	 * @param idCategoria the idCategoria to set
	 */
	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}

	/**
	 * @return the categoria
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * @return the idTipoProducto
	 */
	public String getIdTipoProducto() {
		return idTipoProducto;
	}

	/**
	 * @param idTipoProducto the idTipoProducto to set
	 */
	public void setIdTipoProducto(String idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}

	/**
	 * @return the tipoProducto
	 */
	public String getTipoProducto() {
		return tipoProducto;
	}

	/**
	 * @param tipoProducto the tipoProducto to set
	 */
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
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
	 * @return the nombreComercial
	 */
	public String getNombreComercial() {
		return nombreComercial;
	}

	/**
	 * @param nombreComercial the nombreComercial to set
	 */
	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
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
	 * @return the iva
	 */
	public BigDecimal getIva() {
		return iva;
	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	/**
	 * @return the ice
	 */
	public BigDecimal getIce() {
		return ice;
	}

	/**
	 * @param ice the ice to set
	 */
	public void setIce(BigDecimal ice) {
		this.ice = ice;
	}

	/**
	 * @return the precio1
	 */
	public BigDecimal getPrecio1() {
		return precio1;
	}

	/**
	 * @param precio1 the precio1 to set
	 */
	public void setPrecio1(BigDecimal precio1) {
		this.precio1 = precio1;
	}

	/**
	 * @return the precio2
	 */
	public BigDecimal getPrecio2() {
		return precio2;
	}

	/**
	 * @param precio2 the precio2 to set
	 */
	public void setPrecio2(BigDecimal precio2) {
		this.precio2 = precio2;
	}

	/**
	 * @return the precio3
	 */
	public BigDecimal getPrecio3() {
		return precio3;
	}

	/**
	 * @param precio3 the precio3 to set
	 */
	public void setPrecio3(BigDecimal precio3) {
		this.precio3 = precio3;
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
	 * @return the unidadesCaja
	 */
	public BigDecimal getUnidadesCaja() {
		return unidadesCaja;
	}

	/**
	 * @param unidadesCaja the unidadesCaja to set
	 */
	public void setUnidadesCaja(BigDecimal unidadesCaja) {
		this.unidadesCaja = unidadesCaja;
	}

	/**
	 * @return the unidadesEmbase
	 */
	public BigDecimal getUnidadesEmbase() {
		return unidadesEmbase;
	}

	/**
	 * @param unidadesEmbase the unidadesEmbase to set
	 */
	public void setUnidadesEmbase(BigDecimal unidadesEmbase) {
		this.unidadesEmbase = unidadesEmbase;
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
	 * @return the conversionMedida
	 */
	public BigDecimal getConversionMedida() {
		return conversionMedida;
	}

	/**
	 * @param conversionMedida the conversionMedida to set
	 */
	public void setConversionMedida(BigDecimal conversionMedida) {
		this.conversionMedida = conversionMedida;
	}

	
	
	/**
	 * @return the nombreImagen
	 */
	public String getNombreImagen() {
		return nombreImagen;
	}

	/**
	 * @param nombreImagen the nombreImagen to set
	 */
	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}

	/**
	 * @return the imagen
	 */
	public String getImagen() {
		return imagen;
	}

	/**
	 * @param imagen the imagen to set
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the novedad
	 */
	public String getNovedad() {
		return novedad;
	}

	/**
	 * @param novedad the novedad to set
	 */
	public void setNovedad(String novedad) {
		this.novedad = novedad;
	}



	@Override
	public String toString() {
		return PojoUtil.toString(this);
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



}
