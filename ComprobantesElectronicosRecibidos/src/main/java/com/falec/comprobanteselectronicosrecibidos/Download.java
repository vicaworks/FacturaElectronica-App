package com.falec.comprobanteselectronicosrecibidos;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Download 
{
	private static final Logger LOGGER = Logger.getLogger(Download.class);
	private static String ruta_archivo_propiedades;
	private static List<String> rucs;
	private static List<String> ciadicionales;
	private static List<String> claves;
	private static String mes;
	private static String anio;
	
    public static void main( String[] args )
    {
    	System.setProperty("java.util.logging.SimpleFormatter.format", "[%tF %1$tT] [%4$-7s] %5$s %n");
    	ruta_archivo_propiedades = System.getProperty("user.dir") + java.nio.file.FileSystems.getDefault().getSeparator() 
    			+ "comprobanteselectronicosrecibidos.properties";
    	leerArchivoPropiedades(ruta_archivo_propiedades);
    }
    
    public static void leerArchivoPropiedades(String ruta)
    {
    	try
    	{
	    	Properties pr = new Properties();
	    	FileInputStream fis = new FileInputStream(ruta);
	    	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	    	pr.load(isr);
	    	String[] arreglo_rucs = pr.getProperty("rucs").split(";");
	    	rucs = new ArrayList<>();
	    	rucs.addAll(Arrays.asList(arreglo_rucs));
	    	String[] arreglo_ciadicionales = pr.getProperty("ciadicionales").split(";");
	    	ciadicionales = new ArrayList<>();
	    	ciadicionales.addAll(Arrays.asList(arreglo_ciadicionales));
	    	String[] arreglo_claves = pr.getProperty("claves").split(";");
	    	claves = new ArrayList<>();
	    	claves.addAll(Arrays.asList(arreglo_claves));
	    	mes = pr.getProperty("mes");
	    	anio = pr.getProperty("anio");
    	}
    	catch(IOException ex)
    	{
    		
    	}
    }
}