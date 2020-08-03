package com.falec.comprobantes.recibidos;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Principal 
{
	private static Logger LOGGER = Logger.getLogger(Principal.class);
	private static String rutaArchivoPropiedades;
	private static List<String> rucs;
	private static List<String> cIAdicionales;
	private static List<String> claves;
	private static String mes;
	private static String anio;
	private static List<String> diasEjecutarMesAnterior;
	private static String ejecutarMesAnterior = "no";
	private static String nombreMes = "";
	private static String fechaInicioConsulta = "";
	private static String fechaFinConsulta = "";
	private static String rutaChromeDriver = "";
	
    public static void main( String[] args )
    {
    	System.setProperty("java.util.logging.SimpleFormatter.format", "[%tF %1$tT] [%4$-7s] %5$s %n");
    	rutaArchivoPropiedades = System.getProperty("user.dir") + java.nio.file.FileSystems.getDefault().getSeparator() 
    			+ "comprobantesrecibidos.properties";
    	leerArchivoPropiedades(rutaArchivoPropiedades);
    	descargarReporte();
    }
    
    public static void leerArchivoPropiedades(String ruta)
    {
    	try
    	{
	    	Properties pr = new Properties();
	    	FileInputStream fis = new FileInputStream(ruta);
	    	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	    	pr.load(isr);
	    	
	    	String[] arregloRucs = pr.getProperty("rucs").split(";");
	    	rucs = new ArrayList<>();
	    	rucs.addAll(Arrays.asList(arregloRucs));
	    	
	    	String[] arregloCIAdicionales = pr.getProperty("cIAdicionales").split(";");
	    	cIAdicionales = new ArrayList<>();
	    	cIAdicionales.addAll(Arrays.asList(arregloCIAdicionales));
	    	
	    	String[] arregloClaves = pr.getProperty("claves").split(";");
	    	claves = new ArrayList<>();
	    	claves.addAll(Arrays.asList(arregloClaves));
	    	
	    	mes = pr.getProperty("mes");

	    	anio = pr.getProperty("anio");

	    	String[] arregloDiasMesAnterior = pr.getProperty("diasEjecutarMesAnterior").split(";");
	    	diasEjecutarMesAnterior = new ArrayList<>();
	    	diasEjecutarMesAnterior.addAll(Arrays.asList(arregloDiasMesAnterior));
	    	
	    	rutaChromeDriver = pr.getProperty("rutaChromeDriver");
    	}
    	catch(IOException ex)
    	{
    		LOGGER.info("No se pudo leer el archivo comprobantesrecibidos.properties");
    	}
    }
    
    public static void descargarReporte()
    {
    	obtenerNombreMes();
    	Chrome chrome = new Chrome(rutaChromeDriver);
    	if(ejecutarMesAnterior.compareTo("sí") == 0)
    	{
    		if(mes.compareTo("01") == 0)
    		{
    			mes = "12";
    			Integer anioInt = Integer.parseInt(anio);
    			anioInt -= 1;
    			anio = anioInt.toString();
    		}
    		else
    		{
    			Integer mesInt = Integer.parseInt(mes);
    			mesInt -= 1;
    			mes = mesInt.toString();
    		}
    		obtenerNombreMes();
    	}
    	
    	try
    	{
    		for(int i = 0; i < rucs.size(); i++)
    		{
    			int intento = 1;
    			LOGGER.info("Intento " + intento + "de descarga del reporte del RUC " + rucs.get(i));
    			chrome.inicializarDriver();
    			chrome.iniciarSesion(rucs.get(i), cIAdicionales.get(i), claves.get(i));
    			while(!Chrome.ingresoSRI)
    			{
    				chrome.reintentarIniciarSesion(rucs.get(i), cIAdicionales.get(i), claves.get(i));
    			}
    			Chrome.ingresoSRI = false; // Resetear el valor para el próximo inicio de sesión
    			chrome.irAComprobantesRecibidos();
    			chrome.consultarReporte(nombreMes, anio);
//    			if(chrome.resolverCaptcha())
//    			{
//    				
//    			}
    		}
    	}
    	catch(Exception ex)
    	{
    		LOGGER.error("Error en el proceso de descarga del reporte. ");
    	}
    }
    
    public static void obtenerNombreMes()
    {
    	if(mes.compareTo("10") != 0 && mes.compareTo("11") != 0 && mes.compareTo("12") != 0)
    	{
    		mes = "0" +  mes; // Para los meses de enero a septiembre
    	}
    	
    	fechaInicioConsulta = anio + "-" + mes + "-01";
    	fechaFinConsulta = anio + "-" + mes + "-";
		
    	if(mes.compareTo("01") == 0)
    	{
    		nombreMes = "Enero";
    		fechaFinConsulta += "31";
    	}
    	if(mes.compareTo("02") == 0)
    	{
    		nombreMes = "Febrero";
    		Integer anioInt = Integer.parseInt(anio);
    		GregorianCalendar calendar = new GregorianCalendar();
    		if(calendar.isLeapYear(anioInt)) // Año bisiesto
    			fechaFinConsulta += "29";
    		else // Año no bisiesto
    		{
    			fechaFinConsulta += "28";
    		}
    	}
    	if(mes.compareTo("03") == 0)
    	{
    		nombreMes = "Marzo";
    		fechaFinConsulta += "31";
    	}
    	if(mes.compareTo("04") == 0)
    	{
    		nombreMes = "Abril";
    		fechaFinConsulta += "30";
    	}
    	if(mes.compareTo("05") == 0)
    	{
    		nombreMes = "Mayo";
    		fechaFinConsulta += "31";
    	}
    	if(mes.compareTo("06") == 0)
    	{
    		nombreMes = "Junio";
    		fechaFinConsulta += "30";
    	}
    	if(mes.compareTo("07") == 0)
    	{
    		nombreMes = "Julio";
    		fechaFinConsulta += "31";
    	}
    	if(mes.compareTo("08") == 0)
    	{
    		nombreMes = "Agosto";
    		fechaFinConsulta += "31";
    	}
    	if(mes.compareTo("09") == 0)
    	{
    		nombreMes = "Septiembre";
    		fechaFinConsulta += "30";
    	}
    	if(mes.compareTo("10") == 0)
    	{
    		nombreMes = "Octubre";
    		fechaFinConsulta += "31";
    	}
    	if(mes.compareTo("11") == 0)
    	{
    		nombreMes = "Noviembre";
    		fechaFinConsulta += "30";
    	}
    	if(mes.compareTo("12") == 0)
    	{
    		nombreMes = "Diciembre";
    		fechaFinConsulta += "31";
    	}
    }
}