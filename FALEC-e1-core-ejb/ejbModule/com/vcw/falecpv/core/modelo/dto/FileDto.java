/**
 * 
 */
package com.vcw.falecpv.core.modelo.dto;

import java.io.File;
import java.io.Serializable;

/**
 * @author cristianvillarreal
 *
 */
public class FileDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2185514926495769407L;
	
	private String nombre;
	private File file;
	private byte[] fileByte;

	/**
	 * 
	 */
	public FileDto() {
		
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
	 * @return the fileByte
	 */
	public byte[] getFileByte() {
		return fileByte;
	}

	/**
	 * @param fileByte the fileByte to set
	 */
	public void setFileByte(byte[] fileByte) {
		this.fileByte = fileByte;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
