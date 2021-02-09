/**
 * 
 */
package com.vcw.falecpv.web.servicio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.exception.EnviarComprobanteSRIException;
import com.vcw.falecpv.core.helper.SriAccesoHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.ErrorsriServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.EstadosriServicio;
import com.vcw.falecpv.core.servicio.FirmaElectronicaServicio;
import com.vcw.falecpv.core.servicio.LogtransferenciasriServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.sri.DocElectronicoProxy;
import com.vcw.falecpv.web.servicio.emailcomprobante.EmailComprobanteServicio;
import com.vcw.falecpv.web.servicio.sri.ComprobanteError;
import com.vcw.falecpv.web.servicio.sri.ComprobanteErrorSri;
import com.vcw.falecpv.web.servicio.sri.ComprobantePendiente;
import com.vcw.falecpv.web.servicio.sri.ComprobanteRecibido;
import com.vcw.falecpv.web.servicio.sri.EnviarComprobanteSRI;
import com.vcw.falecpv.web.servicio.sri.ImplEnviarComprobanteSRI;

/**
 * @author cristianvillarreal
 *
 */
//@Singleton
//@Startup
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Stateless
public class SriDispacher {

	@Inject
	private SriUtilServicio sriUtilServicio;
	
	@Inject
	private DocElectronicoProxy docElectronicoProxy;
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	@Inject
	private CabeceraServicio cabeceraServicio;
	
	@Inject
	private FirmaElectronicaServicio firmaElectronicaServicio;
	
	@Inject
	private SriAccesoHelper sriAccesoHelper;
	
	@Inject
	private EstablecimientoServicio establecimientoServicio;
	
	@Inject
	private EstadosriServicio estadosriServicio;
	
	@Inject
	private LogtransferenciasriServicio logtransferenciasriServicio;
	
	@Inject
	private ErrorsriServicio errorsriServicio;
	
	@Inject
	private EmailComprobanteServicio emailComprobanteServicio;
	
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "java:/jms/queue/SRIride")
	private Queue queue;
	
	
	// estados para enviar los documentos al SRI	
	private static final List<ComprobanteEstadoEnum> estadosEnviosri = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.PENDIENTE,ComprobanteEstadoEnum.ERROR,ComprobanteEstadoEnum.ERROR_SRI,ComprobanteEstadoEnum.RECIBIDO_SRI});
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @throws EnviarComprobanteSRIException
	 * @throws DaoException 
	 */
//	@Lock(LockType.WRITE)
	public void comprobanteSriDispacher(Cabecera cabecera)throws EnviarComprobanteSRIException, DaoException {
		
		// 2. analizar los estados para enviar al SRI
		if(ComprobanteEstadoEnum.getByEstado(cabecera.getEstado())!=null && estadosEnviosri.contains(ComprobanteEstadoEnum.getByEstado(cabecera.getEstado()))) {
			
			// si existe constante estado y esta los estados para envio SRI
			
			EnviarComprobanteSRI enviarComprobanteSRI = new ImplEnviarComprobanteSRI();
			HashMap<String, Object> parametros = new HashMap<>();
			
			parametros.put("docElectronicoProxy", docElectronicoProxy);
			parametros.put("parametroGenericoServicio", parametroGenericoServicio);
			parametros.put("cabeceraServicio", cabeceraServicio);
			parametros.put("firmaElectronicaServicio", firmaElectronicaServicio);
			parametros.put("sriAccesoHelper", sriAccesoHelper);
			parametros.put("establecimientoServicio", establecimientoServicio);
			parametros.put("contadorPkServicio", contadorPkServicio);
			parametros.put("idUsuario", cabecera.getIdUsurioTransaccion());
			parametros.put("estadosriServicio", estadosriServicio);
			parametros.put("sriUtilServicio", sriUtilServicio);
			parametros.put("logtransferenciasriServicio", logtransferenciasriServicio);
			parametros.put("errorsriServicio", errorsriServicio);
			parametros.put("emailComprobanteServicio", emailComprobanteServicio);
			
			HashMap<String, Object> resultado = null;
			boolean flag = true;
			int intentos = 0;
			while(flag) {
				cabecera = cabeceraServicio.consultarByPk(cabecera.getIdcabecera());
				parametros.remove("cabecera");
				parametros.put("cabecera", cabecera);
				switch (ComprobanteEstadoEnum.getByEstado(cabecera.getEstado())) {
				case PENDIENTE:
					EnviarComprobanteSRI comprobantePendiente = new ComprobantePendiente(enviarComprobanteSRI);
					comprobantePendiente.enviarComprobante(parametros);
					flag = false;
					break;
				case RECIBIDO_SRI:
					EnviarComprobanteSRI comprobanteRecibido = new ComprobanteRecibido(enviarComprobanteSRI);
					comprobanteRecibido.enviarComprobante(parametros);
					flag = false;
					break;
				case ERROR:
					EnviarComprobanteSRI omprobanteError = new ComprobanteError(enviarComprobanteSRI);
					resultado = omprobanteError.enviarComprobante(parametros);
					if((boolean)resultado.get("ponerCola") && intentos<2) {
						intentos++;
					}else {
						flag = false;
					}
					break;	
				case ERROR_SRI:
					EnviarComprobanteSRI omprobanteErrorSri = new ComprobanteErrorSri(enviarComprobanteSRI);
					resultado = omprobanteErrorSri.enviarComprobante(parametros);
					if((boolean)resultado.get("ponerCola") && intentos<2) {
						intentos++;
					}else {
						flag = false;
					}
					break;
				default:
					flag = false;
					break;
				}
				
			}
			
			
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @throws DaoException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void queue_comprobanteSriDispacher(Cabecera cabecera)throws DaoException{
		Connection connection = null;
		javax.jms.Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

			MessageProducer messageProducer = session.createProducer(queue);
			// objeto a encolar
			ObjectMessage objectMessage = session.createObjectMessage();
			objectMessage.setObject(cabecera);

			messageProducer.send(objectMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
