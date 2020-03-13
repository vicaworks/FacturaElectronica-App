package com.vcw.falecpv.web.util;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.faces.context.FacesContext;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class VelocityTemplateUtil {

	public static Template obtenerContext(String plantilla) {
		Properties p = new Properties();
		p.setProperty("resource.loader", "file");
		p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		p.setProperty("file.resource.loader.path", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/").concat("/WEB-INF/plantillasCorreo"));
		p.setProperty("file.resource.loader.cache", "false");
		p.setProperty("file.resource.loader.modificationCheckInterval", "0");

		VelocityEngine ve = new VelocityEngine();
		ve.init(p);

		Template template = ve.getTemplate(plantilla);

		return template;
	}
	
	public static Template obtenerContextPath(String path,String plantilla) {
		Properties p = new Properties();
		p.setProperty("resource.loader", "file");
		p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		p.setProperty("file.resource.loader.path", path);
		p.setProperty("file.resource.loader.cache", "false");
		p.setProperty("file.resource.loader.modificationCheckInterval", "0");

		VelocityEngine ve = new VelocityEngine();
		ve.init(p);

		Template template = ve.getTemplate(plantilla);

		return template;
	}
	
	/**
	 * Metodo que permite el consumo de archivos velocity
	 * 
	 * @author wayala
	 * @param nombreArchivo nombre de la plantilla velocity con su extension.vm
	 * @param parametros
	 * @param path direccion donde esta ubicada la plantilla
	 * @return StringWriter
	 */
	public static StringWriter consumoVelocity(String nombreArchivo, Map<String, Object> parametros) {
		
		String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/").concat("/WEB-INF/plantillaXml");
		//Inicializacion de velocity
		VelocityEngine ve = new VelocityEngine();
		//Propiedades necesarias para detectar la ubicacion de la plantilla
		Properties props = new Properties();
		props.put("file.resource.loader.path", path);
		props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
		ve.init(props);

		StringWriter w = new StringWriter();
		VelocityContext context = new VelocityContext();
		
		//Recorrido de parametros a reemplazarse en la plantilla
		for (Entry<String, Object> pp : parametros.entrySet()) {
			context.put(pp.getKey(), pp.getValue());
		}
		
		//Obtencion de la plantilla y escritura del archivo
		Template template = ve.getTemplate(nombreArchivo);
		template.merge(context, w);
		
		return w;
	}
	
}
