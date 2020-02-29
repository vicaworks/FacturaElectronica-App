/**
 * 
 */
package com.servitec.common.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author cvillarreal
 *
 */
public abstract class MessageBundleBase {

	protected String bundle;
	
	/**
	 * @return
	 */
	protected ResourceBundle getResourceBundle(){
		return ResourceBundle
		.getBundle(bundle);
	}
	
	/**
	 * 
	 */
	public MessageBundleBase() {
		super();
	}
	
	/**
	 * @param key
	 * @return
	 */
	public  String getString(String key) {
		try {
			return getResourceBundle().getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	/**
	 * @param key
	 * @param params
	 * @return
	 */
	public  String getString(String key, Object... params  ) {
        try {
            return MessageFormat.format(getResourceBundle().getString(key), params);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
