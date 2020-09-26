/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author cristianvillarreal
 *
 */
public class XmlAdapterSriDate extends XmlAdapter<String, Date> {

	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * 
	 */
	public XmlAdapterSriDate() {
	}

	@Override
	public String marshal(Date arg0) throws Exception {
		if(arg0==null) {
			return null;
		}
		return df.format(arg0);
	}

	@Override
	public Date unmarshal(String arg0) throws Exception {
		if(arg0==null) return null;
		return df.parse(arg0);
	}

}
