/**
 * 
 */
package com.servitec.common.dao.datamodel;

import java.util.ArrayList;

/**
 * @author cvillarreal
 *
 */
public class FilaConsulta extends ArrayList<Dato> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 982203416768314560L;

	/**
	 * 
	 */
	public FilaConsulta() {
		super();
	}

    /**
     * Permite retornar el valor asociado al dato
     *
     * @param index Numero de fila desde al cual se consultara
     * @return {@link Object}
     */
    public Object getValor(int index) {
        if (index < 0 || index > this.size() - 1) {
            return null;
        }
        return this.get(index).getValor();
    }

    /**
     * Permite obtener el tipo de dato JAVA asociado al valor
     *
     * @param index Numero de fila desde al cual se consultara
     * @return {@link String}
     */
    public String getTipo(int index) {
        if (index < 0 || index > this.size() - 1) {
            return null;
        }
        return this.get(index).getTipo();
    }

    

}
