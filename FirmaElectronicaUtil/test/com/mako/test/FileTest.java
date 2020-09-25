/**
 * 
 */
package com.mako.test;

import org.apache.commons.io.IOUtils;

/**
 * @author Jorge
 *
 */
public class FileTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte[] data = new byte[5/2];
		IOUtils.toInputStream(new String(data));
	}

}
