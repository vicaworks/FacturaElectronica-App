package com.vcw.falecpv.web.job;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import com.vcw.falecpv.core.constante.parametrosgenericos.PGJobEnum;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.vista.VComprobantesresumen;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.VComprobantesresumenServicio;
import com.vcw.falecpv.web.servicio.SriDispacher;

/**
 * @author cristianvillarreal
 *
 */
@Singleton
@Startup
public class SriJob {

	
	@EJB
	private SriDispacher sriDispacher;
	
	@EJB
	private VComprobantesresumenServicio comprobantesresumenServicio;
	
	@EJB
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@Timeout
	@Schedule(second = "0", minute = "0", hour = "*/4", dayOfMonth = "*", month = "*", dayOfWeek = "*", year = "*")
	//@Schedule(second = "0", minute = "*/2", hour = "*", dayOfMonth = "*", month = "*", dayOfWeek = "*", year = "*")
	public void processViaTimeout(final Timer timer) {
		try {
			
			System.out.println("===========================================");
			System.out.println("========JOB SRI TRANSFERENCIA==============");
			System.out.println("===========================================");
			
			
			// 1 verifica si el job esa habilitado
			if((boolean)parametroGenericoServicio.consultarParametro(PGJobEnum.SRI_JOB_HABILITADO, TipoRetornoParametroGenerico.BOOLEAN)) {
				
				// 2 lista de comprobantes
				List<VComprobantesresumen> comprobantesresumenList = comprobantesresumenServicio.getComprobantesTransferencia();
				for (VComprobantesresumen c : comprobantesresumenList) {
					
					Cabecera cabecera = new Cabecera();
					cabecera.setIdcabecera(c.getIdcabecera());
					cabecera.setIdUsurioTransaccion(c.getIdusuario());
					cabecera.setEstado(c.getEstado());
					sriDispacher.queue_comprobanteSriDispacher(cabecera);
				}
				
				
			}else {
				System.out.println("====== NO HABILITADO");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
