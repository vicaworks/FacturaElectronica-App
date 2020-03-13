/**
 * 
 */
package com.vcw.falecpv.web.constante;

/**
 * @author phidalgo
 *	 Formato de exportaci&oacute;n del archivo
 *
 * @version 1.0
 */
public enum ExportarFileEnum {

	EXCEL(".xls"),PDF(".pdf"),WORD(".doc"),TXT(".txt");
	
	/**
	 *@author wayala 
	 */
	private String extension;

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	private ExportarFileEnum(String extension) {
		this.extension = extension;
	}
	
	
}
