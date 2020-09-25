package com.mako.test;

import com.mako.util.firma.XAdESBESSignature;

/**
 * @author Jorge
 *
 */
public class Principal
{
	public static void main(String[] args)
	{
		String xmlPath = getPath() + "fact69.xml";
        String pathSignature = getPath() + "CRISTIAN ALEX VILLARREAL ALEMAN 100920085643.p12";
        String passSignature = "vcw2pass";
        String pathOut = getPath();
        String nameFileOut = "factura_sign.xml";
        
//        System.out.println(xmlPath);
//        System.out.println(pathSignature);
//        System.out.println(pathOut);
        
        try
        {
            XAdESBESSignature.firmar(xmlPath, pathSignature, passSignature, pathOut, nameFileOut);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
        }
	}
	
	public static String getPath()
	{
		Principal p = new Principal();
		return p.getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6).replace("bin", "Files");
	}
}