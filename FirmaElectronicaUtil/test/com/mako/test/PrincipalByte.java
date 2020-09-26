package com.mako.test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.mako.util.firma.XAdESBESSignature;

/**
 * @author Jorge
 *
 */
public class PrincipalByte
{
	@SuppressWarnings("resource")
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
        
        System.out.println(xmlPath);
        System.out.println(pathSignature);
        System.out.println(pathOut);
        
        try
        {
        	
        	FileInputStream xmlToSignIS = new FileInputStream(xmlPath);
        	FileInputStream penSignatureIS = new FileInputStream(pathSignature);
        	
        	byte[] docXmlSigned = XAdESBESSignature.firmarFacade(xmlToSignIS.readAllBytes(),penSignatureIS.readAllBytes(),passSignature);
        	
        	InputStream inputStream = new ByteArrayInputStream(docXmlSigned);
        	StringWriter writer = new StringWriter();
            String encoding = StandardCharsets.UTF_8.name();
            IOUtils.copy(inputStream, writer, encoding);
            
            System.out.println(writer.toString());
        	
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
        }
	}
	
	public static String getPath(boolean pathInit)
	{
		PrincipalByte p = new PrincipalByte();
		return  (pathInit?"/":"") + p.getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6).replace("bin", "Files");
	}
}