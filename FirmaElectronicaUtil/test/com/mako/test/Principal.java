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
		/*
		 * Si es linux o mac pathInit = true, si es windows false
		 * 
		 * */
		
		boolean pathInit = true;
		
		String xmlPath = getPath(pathInit) + "fact69.xml";
        String pathSignature = getPath(pathInit) + "CRISTIAN ALEX VILLARREAL ALEMAN 100920085643.p12";
        String passSignature = "vcw2pass";
        String pathOut = getPath(pathInit);
        String nameFileOut = "factura_sign.xml";
        
        System.out.println(xmlPath);
        System.out.println(pathSignature);
        System.out.println(pathOut);
        
        try
        {
            XAdESBESSignature.firmarFacade(xmlPath, pathSignature, passSignature, pathOut, nameFileOut);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
        }
	}
	
	public static String getPath(boolean pathInit)
	{
		Principal p = new Principal();
		return  (pathInit?"/":"") + p.getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6).replace("bin", "Files");
	}
}