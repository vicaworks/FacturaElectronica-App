/**
 * 
 */
package com.servitec.common.dao.datamodel;

import java.io.Serializable;

/**
 * @author cvillarreal
 *
 */
public class Dato implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -554879300147041388L;
	
	/**
	 * 
	 */
	public Dato() {
	}
	
   /**
    * Tipo de datos del campo sonsultado
    */
   private String tipo;
   /**
    * Valor almacenado en el campo consultado
    */
   private Object valor;

   public Dato(String tipo, Object valor) {
       this.tipo = tipo;
       this.valor = valor;
   }

   public String getTipo() {
       return tipo;
   }

   public void setTipo(String tipo) {
       this.tipo = tipo;
   }

   public Object getValor() {
       return valor;
   }

   public void setValor(Object valor) {
       this.valor = valor;
   }

   @Override
   public String toString() {
       return valor == null ? "" : valor.toString();
   }

}
