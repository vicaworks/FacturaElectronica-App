/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml.adapter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author cristianvillarreal
 *
 */
public class XmlAdapterSriNumero extends XmlAdapter<String, Double> {

	/**
	 * 
	 */
	public XmlAdapterSriNumero() {
	}

	@Override
	public String marshal(Double arg0) throws Exception {
		if(arg0==null) {
			return "0.00";
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat nf = new DecimalFormat("0.00",otherSymbols); 
		return nf.format(arg0);
	}

	@Override
	public Double unmarshal(String arg0) throws Exception {
		if(arg0==null) return 0.00d;
		return Double.parseDouble(arg0);
	}

}
