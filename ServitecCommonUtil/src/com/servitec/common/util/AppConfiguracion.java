/**
 * 
 */
package com.servitec.common.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author cvillarreal
 *
 */
public class AppConfiguracion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8461991056332744369L;
	
	private static final String BUNDLE_NAME = "com.app.resource.ConfigResources"; 
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
    
    
    /**
     * Separador de linea obtenido de los properties
     */
    public static String SALTO_LINEA = System.getProperty("line.separator");
    /**
     * Operador diferente(<>) se usa en consultas
     */
    public static String OPERADOR_DIFERENTE;
    /**
     * Operador (>=) para consultas
     */
    public static String OPERADOR_MAYOR_IGUAL;
    /**
     * Operador (<=) para consultas
     */
    public static String OPERADOR_MENOR_IGUAL;
    /**
     * Operador es (IS) se usa en consultas
     */
    public static String OPERADOR_ES;
    /**
     * Operador igual(=) se usa en consultas
     */
    public static String OPERADOR_IGUAL;
    /**
     * Operador like(LIKE) se usa en consultas
     */
    public static String OPERADOR_LIKE;
    /**
     * Operador no like (NOT LIKE) se usa en consultas
     */
    public static String OPERADOR_NO_LIKE;
    /**
     * Operador no es (IS NOT) se usa en consultas
     */
    public static String OPERADOR_NO_ES;
    /**
     * Valor para dato nulo(NULL)
     */
    public static String DATO_NULO;
    /**
     * Valor para dato en blanco (-VACIO-)
     */
    public static String DATO_EN_BLANCO;
    
    /**
     * Carga de valores iniciales para variables estaticas
     */
    static {
        if (OPERADOR_IGUAL == null) {
            OPERADOR_IGUAL = RESOURCE_BUNDLE.getString("operador.igual");
        }
        if (OPERADOR_DIFERENTE == null) {
            OPERADOR_DIFERENTE = RESOURCE_BUNDLE.getString("operador.diferente");
        }
        if (OPERADOR_LIKE == null) {
            OPERADOR_LIKE = RESOURCE_BUNDLE.getString("operador.like");
        }
        if (OPERADOR_NO_LIKE == null) {
            OPERADOR_NO_LIKE = RESOURCE_BUNDLE.getString("operador.no.like");
        }
        if (OPERADOR_ES == null) {
            OPERADOR_ES = RESOURCE_BUNDLE.getString("operador.es");
        }
        if (OPERADOR_NO_ES == null) {
            OPERADOR_NO_ES = RESOURCE_BUNDLE.getString("operador.no.es");
        }
        if (DATO_NULO == null) {
            DATO_NULO = RESOURCE_BUNDLE.getString("dato.nulo");
        }
        if (DATO_EN_BLANCO == null) {
            DATO_EN_BLANCO = RESOURCE_BUNDLE.getString("dato.blanco");
        }
        
        if (OPERADOR_MENOR_IGUAL == null) {
            OPERADOR_MENOR_IGUAL = RESOURCE_BUNDLE.getString("operador.menor.igual");
        }
        if (OPERADOR_MAYOR_IGUAL == null) {
            OPERADOR_MAYOR_IGUAL = RESOURCE_BUNDLE.getString("operador.mayor.igual");
        }        
    }
    

	/**
	 * 
	 */
	public AppConfiguracion() {
	}
	
	/**
     * Permite la obtención del valor de la clave del archivo de propiedades general
     *
     * @param key	- Clave del archivo de propiedades que se desea obtener
     * @return	- Valor de la clave ingresadaS
     * @throws {@link  MissingResourceException}
     */
    public static String getString(String key) throws MissingResourceException {
        return RESOURCE_BUNDLE.getString(key);
    }

    /**
     * Permite la obtención del valor de la clave del archivo de propiedades general en formato {@link  Integer}
     *
     * @param key	- Clave del archivo de propiedades que se desea obtener
     * @return	- Valor de la clave ingresada
     * @throws {@link  MissingResourceException}
     */
    public static Integer getInteger(String key) throws MissingResourceException {
        try {
            return Integer.valueOf(RESOURCE_BUNDLE.getString(key));
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Permite la obtención del valor de la clave del archivo de propiedades general en formato {@link  Long}
     *
     * @param key	- Clave del archivo de propiedades que se desea obtener
     * @return	- Valor de la clave ingresada
     * @throws {@link  MissingResourceException}
     */
    public static Long getLong(String key) throws MissingResourceException {
        try {
            return Long.valueOf(RESOURCE_BUNDLE.getString(key));
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Permite la obtención del valor de la clave del archivo de propiedades general en formato {@link  Double}
     *
     * @param key	- Clave del archivo de propiedades que se desea obtener
     * @return	- Valor de la clave ingresada
     * @throws {@link  MissingResourceException}
     */
    public static Double getDouble(String key) throws MissingResourceException {
        try {
            return Double.valueOf(RESOURCE_BUNDLE.getString(key));
        } catch (Exception e) {
        }
        return null;
    }
    
    /**
     * Permite la obtención del valor de la clave del archivo de propiedades en formato {@link String}, con sustitución de 
     * parómetros.
     * 
     * @param key - clave de la propiedad en el archivo de propiedades
     * @param params - lista de parómetros para realizar la sustitución, en la invocación del mótodo el formato es metodo(key,param1,param2,param n)
     * @return una cadena de texto con el valor del del archivo de propiedades con la sustitución de los parómetros
     */
    public static String getString(String key, Object... params  ) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

}
