/**
 * 
 */
package com.servitec.common.util;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author cvillarreal
 *
 */
public class PojoUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7895938647771705372L;

	/**
	 * 
	 */
	public PojoUtil() {
	}
	
	public static  <T> String toString(T clase) {

		StringBuffer msg = new StringBuffer();

		Field fields[] = clase.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {

			if (!"serialVersionUID".equals(fields[i].getName())
					&& !fields[i].getName().contains("List")) {

				Field field = fields[i];

				field.setAccessible(true);
				try {

					msg.append(field.getName() + " : "
							+ field.get(clase).toString() + "\n");

				} catch (Throwable e) {
				}
			}
		}

		return msg.toString();
	}
	
	/**
     * 
     * Realiza un CAST generico a partir del nombre del tipo de objeto y el valor del tipo de objeto
     * 
     * @param clase - nombre de la clase incluyendo paquetes
     * @param valor - valor al cual se aplica el CAST
     * @return un objeto con el valor con CAST
     * @throws ClassNotFoundException en caso de que el nombre de la clase no exista
     */
    public static <T> T castValor(String clase,Object valor) throws ClassNotFoundException{
    	if (valor==null || clase==null){
    		return null;
    	}
		@SuppressWarnings("unchecked")
		T var = (T) Class.forName(clase).cast(valor);
		return var;
	}

}
