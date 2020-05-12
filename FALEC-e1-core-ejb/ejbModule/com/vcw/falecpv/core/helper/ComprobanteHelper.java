/**
 * 
 */
package com.vcw.falecpv.core.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;

/**
 * @author cristianvillarreal
 *
 */
public class ComprobanteHelper {

	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 */
	public static String generarAutorizacionFacade(Cabecera cabecerafac,String aleatorio) {
		
		SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyy");
		
		String autorizacion = "";
		//digitos 1 a 8 : fecha ddmmyyyy
		autorizacion += sf.format(cabecerafac.getFechaemision());
		//digitos 9 a 10 : identificador tipo comprobante
		autorizacion += cabecerafac.getTipocomprobante().getIdentificador();
		//digitos 11 a 23 : identificador tipo comprobante
		autorizacion += cabecerafac.getEstablecimiento().getEmpresa().getRuc();
		//digitos 24 : identificador tipo comprobante
		autorizacion += cabecerafac.getEstablecimiento().getAmbiente();
		//digitos 25 a 27 : codigo establecimeinto
		autorizacion += TextoUtil.leftPadTexto(cabecerafac.getEstablecimiento().getCodigoestablecimiento(), 3, "0");
		//digitos 28 a 30 : punto de emision
		autorizacion += TextoUtil.leftPadTexto(cabecerafac.getEstablecimiento().getPuntoemision(), 3, "0");
		//digitos 31 a 39 : punto de emision
		autorizacion += cabecerafac.getSecuencial();
		//digitos 40 a 47 : aleatorio
		autorizacion += TextoUtil.leftPadTexto(aleatorio, 8, "0");
		//digitos 48 : tipo emision
		autorizacion += "1";
		// algoritmo mod11
		autorizacion += mod11(autorizacion);
		
		return autorizacion;
	}
	
	/**
	 * @author jtoaza
	 * @param sequence
	 * @return
	 */
	public static String mod11(String sequence)
    {
         int piso = 2;
         int rutSum = 0;
         int throwput = 1;
         char[] inverseSequence = getInverseSequence(sequence.toCharArray());
         for (int i = 0; i < sequence.length(); i++)
         {
             throwput = Integer.parseInt(String.valueOf(inverseSequence[i])) * piso;
             rutSum += throwput;
             if(piso == 7)
              {
                 piso = 1;
             }
             piso++;
         }
         int module = rutSum % 11;
         String result = String.valueOf(11 - module);
         if (result.equals("11"))
         {
             result = "0";
         }
         else if (result.equals("10")) {
             result = "1";
         }
         return result;
     }
	
	/**
	 * @author jtoaza
	 * @param secuencia
	 * @return
	 */
	public static char[] getInverseSequence(char[] secuencia)
    {
        char[] enteroinverso = new char[secuencia.length];
        int longitudmaxima = secuencia.length;
        for(int i = 0; i < secuencia.length; i++)
        {
            enteroinverso[longitudmaxima - 1] = secuencia[i];
            longitudmaxima--;
        }
        return enteroinverso;
    }
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param detallefacList
	 * @return
	 */
	public static List<Totalimpuesto> determinarIva(List<Detalle> detallefacList){
		
		List<Totalimpuesto> impuestoList = new ArrayList<>();
		
		List<Iva> ivaList = detallefacList.stream().map(x->x.getIva()).filter(x->x.getValor().doubleValue()>0).distinct().collect(Collectors.toList());
		for (Iva iva : ivaList) {
			Totalimpuesto ti = new Totalimpuesto();
			ti.setIva(iva);
			ti.setDescuentoadicional(BigDecimal.ZERO);
			ti.setBaseimponible(BigDecimal
					.valueOf(detallefacList.stream().filter(x -> x.getIva().getIdiva().equals(iva.getIdiva()))
							.mapToDouble(x -> x.getPreciototalsinimpuesto().add(x.getValorice()).doubleValue()).sum())
					.setScale(2, RoundingMode.HALF_UP));
			ti.setValor(BigDecimal
					.valueOf(detallefacList.stream().filter(x -> x.getIva().getIdiva().equals(iva.getIdiva()))
							.mapToDouble(x -> x.getValoriva().doubleValue()).sum())
					.setScale(2, RoundingMode.HALF_UP));
			impuestoList.add(ti);
		}
		
		return impuestoList;
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param detallefacList
	 * @return
	 */
	public static List<Totalimpuesto> determinarIce(List<Detalle> detallefacList){
		
		List<Totalimpuesto> impuestoList = new ArrayList<>();
		
		List<Ice> iceList = detallefacList.stream().map(x->x.getIce()).filter(x->x.getValor().doubleValue()>0).distinct().collect(Collectors.toList());
		for (Ice ice : iceList) {
			Totalimpuesto ti = new Totalimpuesto();
			ti.setIce(ice);
			ti.setDescuentoadicional(BigDecimal.ZERO);
			ti.setBaseimponible(BigDecimal
					.valueOf(detallefacList.stream().filter(x -> x.getIce().getIdice().equals(ice.getIdice()))
							.mapToDouble(x -> x.getPreciototalsinimpuesto().doubleValue()).sum())
					.setScale(2, RoundingMode.HALF_UP));
			ti.setValor(BigDecimal
					.valueOf(detallefacList.stream().filter(x -> x.getIce().getIdice().equals(ice.getIdice()))
							.mapToDouble(x -> x.getValorice().doubleValue()).sum())
					.setScale(2, RoundingMode.HALF_UP));
			impuestoList.add(ti);
		}
		
		return impuestoList;
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param detalleFacList
	 */
	public static void determinarDetalleImpuesto(List<Detalle> detalleFacList) {
		
		for (Detalle df : detalleFacList) {
			df.setDetalleimpuestoList(new ArrayList<>());
			if(df.getValorice().doubleValue()>0) {
				Detalleimpuesto di = new Detalleimpuesto();
				di.setIce(df.getIce());
				di.setTarifa(df.getIce().getValor());
				di.setBaseimponible(df.getPreciototalsinimpuesto());
				di.setValor(df.getValorice());
				df.getDetalleimpuestoList().add(di);
			}
			
			if(df.getValoriva().doubleValue()>0) {
				Detalleimpuesto di = new Detalleimpuesto();
				di.setIva(df.getIva());
				di.setTarifa(df.getIva().getValor());
				di.setBaseimponible(df.getPreciototalsinimpuesto().add(df.getValorice()).setScale(2, RoundingMode.HALF_UP));
				di.setValor(df.getValoriva());
				df.getDetalleimpuestoList().add(di);
			}
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabeceraFac
	 * @return
	 */
	public static List<Infoadicional> determinarInfoAdicional(Cabecera cabeceraFac) {
		List<Infoadicional> infoadicionalList = new ArrayList<>();
		
		if(cabeceraFac.getCliente().getCorreoelectronico()!=null) {
			Infoadicional ia = new Infoadicional();
			ia.setNombre("email");
			ia.setValor(cabeceraFac.getCliente().getCorreoelectronico());
			infoadicionalList.add(ia);
			
		}
		
		return infoadicionalList;
	}
	

}
